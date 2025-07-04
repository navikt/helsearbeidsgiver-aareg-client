package no.nav.helsearbeidsgiver.aareg

import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.mockk.every
import kotlinx.coroutines.delay
import no.nav.helsearbeidsgiver.utils.cache.LocalCache
import no.nav.helsearbeidsgiver.utils.test.mock.mockStatic
import no.nav.helsearbeidsgiver.utils.test.resource.readResource
import kotlin.time.Duration

object MockResponse {
    val arbeidsforhold = "aareg-arbeidsforhold.json".readResource()
    val error = "error.json".readResource()
}

fun mockAaregClient(vararg responses: Pair<HttpStatusCode, String>): AaregClient {
    val mockEngine = MockEngine.create {
        reuseHandlers = false
        requestHandlers.addAll(
            responses.map { (status, content) ->
                {
                    if (content == "timeout") {
                        delay(600)
                    }
                    respond(
                        content = content,
                        status = status,
                        headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString()),
                    )
                }
            },
        )
    }

    val mockHttpClient = HttpClient(mockEngine) { configure() }

    return mockStatic(::createHttpClient) {
        every { createHttpClient() } returns mockHttpClient

        AaregClient("", LocalCache.Config(Duration.ZERO, 1)) { "mock access token" }
    }
}
