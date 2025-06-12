package no.nav.helsearbeidsgiver.aareg

import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.maps.shouldContainExactly
import io.ktor.client.plugins.ServerResponseException
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.JsonConvertException
import no.nav.helsearbeidsgiver.utils.test.date.januar
import no.nav.helsearbeidsgiver.utils.test.date.mars
import no.nav.helsearbeidsgiver.utils.test.wrapper.genererGyldig
import no.nav.helsearbeidsgiver.utils.wrapper.Fnr
import no.nav.helsearbeidsgiver.utils.wrapper.Orgnr

class AaregClientTest : FunSpec({

    test("gir ikke-tom liste med arbeidsforhold") {
        val response = mockAaregClient(MockResponse.arbeidsforhold)
            .hentAnsettelsesperioder(Fnr("22018520056"), "call-id")

        val expectedAnsettelsesperioder =
            mapOf(
                Orgnr("896929119") to setOf(
                    Periode(fom = 22.januar(2001), tom = null),
                    Periode(fom = 15.mars(2001), tom = null),
                ),
            )

        response shouldContainExactly expectedAnsettelsesperioder
    }

    test("kaster exception ved server-feil fra aareg") {
        shouldThrowExactly<ServerResponseException> {
            mockAaregClient("blablabla", HttpStatusCode.InternalServerError)
                .hentAnsettelsesperioder(Fnr.genererGyldig(), "123456")
        }
    }

    test("kaster exception ved uventet JSON") {
        shouldThrowExactly<JsonConvertException> {
            mockAaregClient(MockResponse.error)
                .hentAnsettelsesperioder(Fnr.genererGyldig(), "54-56 That's My Number")
        }
    }
})
