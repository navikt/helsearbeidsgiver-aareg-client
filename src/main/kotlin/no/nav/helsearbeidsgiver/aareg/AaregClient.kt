package no.nav.helsearbeidsgiver.aareg

import io.ktor.client.call.body
import io.ktor.client.plugins.ResponseException
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.JsonConvertException
import no.nav.helsearbeidsgiver.utils.log.logger
import no.nav.helsearbeidsgiver.utils.log.sikkerLogger
import java.net.ConnectException

class AaregClient(
    private val url: String,
    private val getAccessToken: () -> String,
) {
    private val httpClient = createHttpClient()

    private val logger = logger()
    private val sikkerLogger = sikkerLogger()

    suspend fun hentArbeidsforhold(ident: String, callId: String): List<Arbeidsforhold> {
        val token = getAccessToken()
        return try {
            httpClient.get(url) {
                contentType(ContentType.Application.Json)
                bearerAuth(token)
                header("X-Correlation-ID", callId)
                header("Nav-Consumer-Token", "Bearer $token")
                header("Nav-Personident", ident)
            }.also {
                sikkerLogger.debug("Svar fra aareg-API: " + it.bodyAsText())
            }.body<List<Arbeidsforhold>>()
        } catch (responseException: ResponseException) {
            logger.error("Hente arbeidsforhold callId=[$callId] feilet med http-kode ${responseException.response.status}")
            emptyList()
        } catch (connectException: ConnectException) {
            logger.error("Hente arbeidsforhold callId=[$callId] feilet:", connectException)
            emptyList()
        } catch (jsonConvertException: JsonConvertException) {
            logger.error("Hente arbeidsforhold callId=[$callId] feilet, kunne ikke lese JSON")
            sikkerLogger.error("Hente arbeidsforhold callId=[$callId] feilet", jsonConvertException)
            emptyList()
        }
    }
}
