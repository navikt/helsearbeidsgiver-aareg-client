package no.nav.helsearbeidsgiver.aareg

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.ResponseException
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.bearerAuth
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.http.isSuccess
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.net.ConnectException

/**
 * klient for å hente ut aktive arbeidsforhold på en person
 */

class AaregClient(
    engine: HttpClientEngine = OkHttp.create(),
    private val url: String,
    private val getAccessToken: () -> String,
    private val enableHttpLogging: Boolean = false,
    private val retryTimes: Int = 3,
    private val retryDelayInMs: Long = 1000L
) {
    private val sikkerLogg: Logger = LoggerFactory.getLogger("tjenestekall")
    private val aaregClientLogger: Logger = LoggerFactory.getLogger("helsearbeidsgiver-aareg-client")
    private val httpClient = createHttpClient(engine)

    private fun createHttpClient(engine: HttpClientEngine) = HttpClient(engine) {
        @OptIn(ExperimentalSerializationApi::class)
        install(ContentNegotiation) {
            json(
                Json {
                    ignoreUnknownKeys = true
                    explicitNulls = false
                }
            )
        }
        if (enableHttpLogging) {
            install(Logging) {
                logger = object : io.ktor.client.plugins.logging.Logger {
                    override fun log(message: String) {
                        sikkerLogg.info(message)
                    }
                }
            }
        }
        if (retryTimes > 0) {
            install(HttpRequestRetry) {
                maxRetries = retryTimes
                retryIf { _, response ->
                    !response.status.isSuccess()
                }
                delayMillis { retry ->
                    retry * retryDelayInMs
                }
            }
        }
        expectSuccess = true
    }

    suspend fun hentArbeidsforhold(ident: String, callId: String): List<Arbeidsforhold> {
        val token = getAccessToken()
        try {
            val payload = httpClient.get(url) {
                contentType(ContentType.Application.Json)
                bearerAuth(token)
                header("X-Correlation-ID", callId)
                header("Nav-Consumer-Token", "Bearer $token")
                header("Nav-Personident", ident)
            }.also {
                if (enableHttpLogging) {
                    sikkerLogg.info("Svar fra aareg-API: " + it.bodyAsText())
                }
            }.body<List<Arbeidsforhold>>()
            return payload
        } catch (responseException: ResponseException) {
            aaregClientLogger.error("Hente arbeidsforhold callId=[$callId] feilet med http-kode ${responseException.response.status}")
            return emptyList()
        } catch (connectException: ConnectException) {
            aaregClientLogger.error("Hente arbeidsforhold callId=[$callId] feilet:", connectException)
            return emptyList()
        }
    }
}
