package no.nav.helsearbeidsgiver.aareg

import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf

object MockResponse {
    val arbeidsforhold = "aareg-arbeidsforhold.json".readResource()
}

fun mockSuccessResponse(content: String): MockEngine {
    return MockEngine {
        respond(
            content = content,
            status = HttpStatusCode.OK,
            headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())
        )
    }
}

fun mockErrorResponse() = MockEngine {
    respond(
        content = "Error",
        status = HttpStatusCode.InternalServerError,
        headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())
    )
}

internal fun String.readResource(): String =
    ClassLoader.getSystemResource(this).readText()
