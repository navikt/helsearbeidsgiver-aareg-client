package no.nav.helsearbeidsgiver.aareg

import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test

class AaregArbeidsforholdClientTest {

    @Test
    fun `Returnerer gyldig objekt når alt er oK`() {
        val aaregClient = buildClient("aareg-arbeidsforhold.json".loadFromResources(), HttpStatusCode.Accepted)
        val response = runBlocking { aaregClient.hentArbeidsforhold("ident", "call-id") }
        assertNotNull(response)
        assertNotNull(response.find { it.arbeidsgiver.organisasjonsnummer == "896929119" })
    }
}
