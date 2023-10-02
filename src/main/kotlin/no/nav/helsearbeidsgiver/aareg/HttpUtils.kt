package no.nav.helsearbeidsgiver.aareg

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import no.nav.helsearbeidsgiver.utils.json.jsonConfig

internal fun createHttpClient(): HttpClient =
    HttpClient(OkHttp) { configure() }

internal fun HttpClientConfig<*>.configure() {
    install(ContentNegotiation) {
        expectSuccess = true

        json(jsonConfig)
    }
}
