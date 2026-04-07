package no.nav.helsearbeidsgiver.aareg

import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.apache5.Apache5
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import no.nav.helsearbeidsgiver.utils.json.jsonConfig

private val log = KotlinLogging.logger {}

internal fun createHttpClient(): HttpClient = HttpClient(Apache5) { configure() }

internal fun HttpClientConfig<*>.configure() {
    expectSuccess = true

    install(ContentNegotiation) {
        json(jsonConfig)
    }

    install(Logging) {
        logger =
            object : Logger {
                override fun log(message: String) {
                    log.debug { message }
                }
            }
        level = LogLevel.INFO
        sanitizeHeader { header -> header == HttpHeaders.Authorization || header == "Nav-Personident" }
    }

    install(HttpRequestRetry) {
        retryOnException(
            maxRetries = 3,
            retryOnTimeout = true,
        )
        constantDelay(
            millis = 500,
            randomizationMs = 500,
        )
        modifyRequest { requestBuilder ->
            log.warn { "Retry attempt $retryCount for ${requestBuilder.url}" }
        }
    }

    install(HttpTimeout) {
        connectTimeoutMillis = 10000
        requestTimeoutMillis = 10000
        socketTimeoutMillis = 10000
    }
}
