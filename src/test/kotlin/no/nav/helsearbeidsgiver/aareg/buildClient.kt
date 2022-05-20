package no.nav.helsearbeidsgiver.aareg

import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.utils.io.ByteReadChannel
import kotlinx.serialization.ExperimentalSerializationApi
import no.nav.helsearbeidsgiver.tokenprovider.AccessTokenProvider

@OptIn(ExperimentalSerializationApi::class)
fun buildClient(
    response: String,
    status: HttpStatusCode = HttpStatusCode.OK,
    headers: Headers = headersOf(HttpHeaders.ContentType, "application/json")
): AaregArbeidsforholdClient {
    val mockEngine = MockEngine {
        respond(
            content = ByteReadChannel(response),
            status = status,
            headers = headers
        )
    }

    return AaregArbeidsforholdClient(
        "http://localhost",
        MockAccessTokenProvider(),
        HttpClient(mockEngine) {
            install(JsonFeature) {
                serializer = KotlinxSerializer(
                    kotlinx.serialization.json.Json {
                        ignoreUnknownKeys = true
                        // explicitNulls = false
                    }
                )
            }
        }
    )
}

class MockAccessTokenProvider : AccessTokenProvider {
    override fun getToken(): String {
        return "token"
    }
}

fun String.loadFromResources(): String {
    return ClassLoader.getSystemResource(this).readText()
}
