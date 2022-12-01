package no.nav.helsearbeidsgiver.aareg

import io.ktor.client.engine.okhttp.OkHttp
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class AaregClientTest {

    /*
    API Description:
    https://navikt.github.io/aareg/tjenester/integrasjon/api/
    */
    @Test
    fun `Returnerer gyldig objekt når alt er oK`() {
        val mockEngine = mockSuccessResponse(MockResponse.arbeidsforhold)
        val client = AaregClient(mockEngine, "url", { "fake token" })
        val response = runBlocking {
            client.hentArbeidsforhold("ident", "call-id")
        }
        assertTrue(response.any { it.arbeidsgiver.organisasjonsnummer == "896929119" })
    }

    @Test
    fun testRetry() {
        val mockEngine = mockErrorResponse()
        val client = AaregClient(mockEngine, "url", { "fake token" }, retryDelayInMs = 5L)
        val response = runBlocking {
            client.hentArbeidsforhold("hei", "54-56 That's My Number")
        }
        assertEquals(4, mockEngine.requestHistory.size)
        val empty = emptyList<Arbeidsforhold>()
        assertEquals(empty, response)
    }

    @Test
    fun realDeal() {
        val client = AaregClient(OkHttp.create(), "blah", { "tja" }, retryTimes = 0)
        val response = runBlocking { client.hentArbeidsforhold("hei", "Number 2") }
        assertEquals(emptyList<Arbeidsforhold>(), response)
    }
}
