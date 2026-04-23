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
    private val url = "$baseUrl/api/v2/arbeidstaker/arbeidsforhold?sporingsinformasjon=false&historikk=false"
    private val httpClient = createHttpClient()
    private val cache = LocalCache<List<Arbeidsforhold>>(cacheConfig)

    suspend fun hentAnsettelsesperioder(
        fnr: String,
        callId: String,
    ): Map<Orgnr, Set<Periode>> =
        hentArbeidsforholdPerOrgnr(fnr, callId)
            .mapValues { (_, arbeidsforholdListe) ->
                arbeidsforholdListe
                    .map { Periode(fom = it.ansettelsesperiode.startdato, tom = it.ansettelsesperiode.sluttdato) }
                    .toSet()
            }

    suspend fun hentAnsettelsesforhold(
        fnr: String,
        callId: String,
    ): Map<Orgnr, Set<Ansettelsesforhold>> =
        hentArbeidsforholdPerOrgnr(fnr, callId)
            .mapValues { (_, arbeidsforholdListe) ->
                arbeidsforholdListe
                    .map {
                        val gjeldendeDetalj =
                            it.ansettelsesdetaljer.first { detalj ->
                                detalj.rapporteringsmaaneder?.til == null
                            }
                        Ansettelsesforhold(
                            startdato = it.ansettelsesperiode.startdato,
                            sluttdato = it.ansettelsesperiode.sluttdato,
                            yrkesKode = gjeldendeDetalj.yrke?.kode,
                            yrkesBeskrivelse = gjeldendeDetalj.yrke?.beskrivelse,
                            stillingsprosent = gjeldendeDetalj.avtaltStillingsprosent,
                        )
                    }.toSet()
            }

    private suspend fun hentArbeidsforholdPerOrgnr(
        fnr: String,
        callId: String,
    ): Map<Orgnr, List<Arbeidsforhold>> =
        cache
            .getOrPut(fnr) {
                httpClient
                    .get(url) {
                        contentType(ContentType.Application.Json)
                        bearerAuth(getAccessToken())
                        header("Nav-Personident", fnr)
                        header("X-Correlation-ID", callId)
                    }.body<List<Arbeidsforhold>>()
            }.groupBy { it.arbeidssted.organisasjonsnummer() }
            .mapKeysNotNull {
                if (it != null && Orgnr.erGyldig(it)) {
                    Orgnr(it)
                } else {
                    null
                }
            }
}
