package no.nav.helsearbeidsgiver.aareg

import io.ktor.client.call.body
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.contentType
import no.nav.helsearbeidsgiver.utils.cache.LocalCache
import no.nav.helsearbeidsgiver.utils.collection.mapKeysNotNull
import no.nav.helsearbeidsgiver.utils.wrapper.Orgnr

/** Les om API-et til Aareg [her](https://navikt.github.io/aareg/tjenester/integrasjon/api/). */
class AaregClient(
    baseUrl: String,
    cacheConfig: LocalCache.Config,
    private val getAccessToken: () -> String,
) {
    private val url = "$baseUrl/api/v1/arbeidstaker/arbeidsforhold?sporingsinformasjon=false&historikk=false"
    private val httpClient = createHttpClient()
    private val cache = LocalCache<List<Arbeidsforhold>>(cacheConfig)

    suspend fun hentAnsettelsesperioder(fnr: String, callId: String): Map<Orgnr, Set<Periode>> =
        cache.getOrPut(fnr) {
            httpClient.get(url) {
                contentType(ContentType.Application.Json)
                bearerAuth(getAccessToken())
                header("Nav-Personident", fnr)
                header("X-Correlation-ID", callId)
            }.body<List<Arbeidsforhold>>()
        }
            .groupBy { it.arbeidsgiver.organisasjonsnummer }
            .mapKeysNotNull {
                if (it != null && Orgnr.erGyldig(it)) {
                    Orgnr(it)
                } else {
                    null
                }
            }
            .mapValues { (_, arbeidsforholdListe) ->
                arbeidsforholdListe.map { it.ansettelsesperiode.periode }.toSet()
            }
}
