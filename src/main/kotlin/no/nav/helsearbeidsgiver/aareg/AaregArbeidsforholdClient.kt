package no.nav.helsearbeidsgiver.aareg

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*
import no.nav.helsearbeidsgiver.tokenprovider.AccessTokenProvider

/**
 * klient for å hente ut aktive arbeidsforhold på en person
 */
class AaregArbeidsforholdClient(
    private val url: String,
    private val stsClient: AccessTokenProvider,
    private val httpClient: HttpClient
) {

    suspend fun hentArbeidsforhold(ident: String, callId: String): List<Arbeidsforhold> {
        val stsToken = stsClient.getToken()
        return httpClient.get<List<Arbeidsforhold>>(url) {
            contentType(ContentType.Application.Json.withCharset(Charsets.UTF_8))
            header("Authorization", "Bearer $stsToken")
            header("X-Correlation-ID", callId)
            header("Nav-Consumer-Token", "Bearer $stsToken")
            header("Nav-Personident", "$ident")
        }
    }
}
