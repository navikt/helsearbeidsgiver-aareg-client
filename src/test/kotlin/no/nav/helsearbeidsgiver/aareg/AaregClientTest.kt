package no.nav.helsearbeidsgiver.aareg

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldNotBeEmpty
import io.ktor.http.HttpStatusCode

class AaregClientTest : FunSpec({

    /*
    API Description:
    https://navikt.github.io/aareg/tjenester/integrasjon/api/
     */
    test("gir ikke-tom liste med arbeidsforhold") {
        val response = mockAaregClient(MockResponse.arbeidsforhold)
            .hentArbeidsforhold("ident", "call-id")

        response.filter { it.arbeidsgiver.organisasjonsnummer == "896929119" }.shouldNotBeEmpty()
    }

    test("gir tom liste ved uventet JSON") {
        val response = mockAaregClient(MockResponse.error)
            .hentArbeidsforhold("hei", "54-56 That's My Number")

        response.shouldBeEmpty()
    }

    test("gir tom liste ved server-feil fra aareg") {
        val response = mockAaregClient("blablabla", HttpStatusCode.InternalServerError)
            .hentArbeidsforhold("hei", "123456")

        response.shouldBeEmpty()
    }

    test("gir tom liste ved feil konfigurert klient") {
        val client = AaregClient(url = "blah") { "tja" }
        val response = client.hentArbeidsforhold("hei", "Number 2")

        response.shouldBeEmpty()
    }
})
