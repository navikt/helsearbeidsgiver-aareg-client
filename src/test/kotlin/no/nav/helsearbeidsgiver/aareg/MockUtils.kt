package no.nav.helsearbeidsgiver.aareg

import io.kotest.core.test.testCoroutineScheduler
import io.kotest.matchers.nulls.shouldNotBeNull
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.HttpRequestRetry
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.mockk.every
import kotlinx.coroutines.ExperimentalCoroutinesApi
import no.nav.helsearbeidsgiver.utils.cache.LocalCache
import no.nav.helsearbeidsgiver.utils.test.mock.mockStatic
import no.nav.helsearbeidsgiver.utils.test.resource.readResource
import kotlin.time.Duration

object MockResponse {
    val arbeidsforhold = "aareg-arbeidsforhold.json".readResource()
    val error = "error.json".readResource()
}

@OptIn(ExperimentalStdlibApi::class, ExperimentalCoroutinesApi::class)
fun mockAaregClient(vararg responses: Pair<HttpStatusCode, String>): AaregClient {
    val mockEngine = MockEngine.create {
        reuseHandlers = false
        requestHandlers.addAll(
            responses.map { (status, content) ->
                {
                    if (content == "timeout") {
                        // Skrur den virtuelle klokka fremover, nok til at timeout forårsakes
                        dispatcher.shouldNotBeNull().testCoroutineScheduler.advanceTimeBy(1)
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

    val mockHttpClient = HttpClient(mockEngine) {
        configure()

        // Overstyr delay for å unngå at testene bruker lang tid
        install(HttpRequestRetry) {
            configureRetry()
            constantDelay(
                millis = 1,
                randomizationMs = 1,
            )
        }
    }

    return mockStatic(::createHttpClient) {
        every { createHttpClient() } returns mockHttpClient

        AaregClient("url", LocalCache.Config(Duration.ZERO, 1)) { "mock access token" }
    }
}
