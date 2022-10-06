package no.nav.helsearbeidsgiver.aareg

import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class AaregClientTest {

    @Test
    fun `Returnerer gyldig objekt når alt er oK`() {
        val response = runBlocking {
            mockAaregClient(MockResponse.arbeidsforhold)
                .hentArbeidsforhold("ident", "call-id")
        }
        assertTrue(response.any { it.arbeidsgiver.organisasjonsnummer == "896929119" })
    }
}
