package no.nav.helsearbeidsgiver.aareg

import io.ktor.client.call.body
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.http.ContentType
import io.ktor.http.contentType

/**
 * klient for å hente ut aktive arbeidsforhold på en person
 */
class AaregClient(
    private val url: String,
    private val getAccessToken: () -> String
) {
    private val httpClient = createHttpClient()

    suspend fun hentArbeidsforhold(ident: String, callId: String): List<Arbeidsforhold> {
        val token = getAccessToken()
        return httpClient.get(url) {
            contentType(ContentType.Application.Json)
            bearerAuth(token)
            header("X-Correlation-ID", callId)
            header("Nav-Consumer-Token", "Bearer $token")
            header("Nav-Personident", ident)
        }.body()
    }
}
