package no.nav.helsearbeidsgiver.aareg

import io.ktor.client.call.body
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.contentType
import no.nav.helsearbeidsgiver.utils.cache.LocalCache
import no.nav.helsearbeidsgiver.utils.collection.mapKeysNotNull
import no.nav.helsearbeidsgiver.utils.wrapper.Fnr
import no.nav.helsearbeidsgiver.utils.wrapper.Orgnr

/** Les om API-et til Aareg [her](https://navikt.github.io/aareg/tjenester/integrasjon/api/). */
class AaregClient(
    private val url: String,
    cacheConfig: LocalCache.Config,
    private val getAccessToken: () -> String,
) {
    private val httpClient = createHttpClient()
    private val cache = LocalCache<List<Arbeidsforhold>>(cacheConfig)

    suspend fun hentAnsettelsesperioder(fnr: Fnr, callId: String): Map<Orgnr, Set<Periode>> =
        cache.getOrPut(fnr.verdi) {
            httpClient.get(url) {
                contentType(ContentType.Application.Json)
                bearerAuth(getAccessToken())
                header("X-Correlation-ID", callId)
                header("Nav-Personident", fnr.verdi)
            }.body<List<Arbeidsforhold>>()
        }
            .groupBy { it.arbeidsgiver.organisasjonsnummer }
            .mapKeysNotNull { it?.let(::Orgnr) }
            .mapValues { (_, arbeidsforholdListe) ->
                arbeidsforholdListe.map { it.ansettelsesperiode.periode }.toSet()
            }
}
