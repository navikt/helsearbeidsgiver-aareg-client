package no.nav.helsearbeidsgiver.aareg

import io.kotest.assertions.throwables.shouldNotThrowAny
import io.kotest.assertions.throwables.shouldThrowExactly
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.maps.shouldContainExactly
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.JsonConvertException
import no.nav.helsearbeidsgiver.utils.json.serializer.list
import no.nav.helsearbeidsgiver.utils.json.toJson
import no.nav.helsearbeidsgiver.utils.test.date.januar
import no.nav.helsearbeidsgiver.utils.test.date.juli
import no.nav.helsearbeidsgiver.utils.test.date.juni
import no.nav.helsearbeidsgiver.utils.test.date.mars
import no.nav.helsearbeidsgiver.utils.test.date.september
import no.nav.helsearbeidsgiver.utils.test.wrapper.genererGyldig
import no.nav.helsearbeidsgiver.utils.wrapper.Fnr
import no.nav.helsearbeidsgiver.utils.wrapper.Orgnr

class AaregClientTest : FunSpec({

    test("gir ikke-tom liste med arbeidsforhold") {
        val response = mockAaregClient(HttpStatusCode.OK to MockResponse.arbeidsforhold)
            .hentAnsettelsesperioder("22018520056", "call-id")

        val expectedAnsettelsesperioder =
            mapOf(
                Orgnr("896929119") to setOf(
                    Periode(fom = 22.januar(2001), tom = null),
                    Periode(fom = 15.mars(2001), tom = null),
                ),
            )

        response shouldContainExactly expectedAnsettelsesperioder
    }

    test("filtrerer ut arbeidsforhold der arbeidsgiver ikke har gylding orgnr") {
        val orgnr = Orgnr.genererGyldig()
        val arbeidsforhold = listOf(
            Arbeidsforhold(Arbeidsgiver(orgnr.verdi), Ansettelsesperiode(Periode(3.januar(2021), 7.september(2021)))),
            Arbeidsforhold(Arbeidsgiver("PRIVAT"), Ansettelsesperiode(Periode(8.juni(2021), 8.juli(2021)))),
        )

        val response = mockAaregClient(HttpStatusCode.OK to arbeidsforhold.toJson(Arbeidsforhold.serializer().list()).toString())
            .hentAnsettelsesperioder(Fnr.genererGyldig().verdi, "call-id")

        val expectedAnsettelsesperioder =
            mapOf(
                orgnr to setOf(
                    Periode(3.januar(2021), 7.september(2021)),
                ),
            )

        response shouldContainExactly expectedAnsettelsesperioder
    }

    test("kaster exception ved uventet JSON") {
        shouldThrowExactly<JsonConvertException> {
            mockAaregClient(HttpStatusCode.OK to MockResponse.error)
                .hentAnsettelsesperioder(Fnr.genererGyldig().verdi, "54-56 That's My Number")
        }
    }

    test("feiler ved 4xx-feil") {
        shouldThrowExactly<ClientRequestException> {
            mockAaregClient(HttpStatusCode.BadRequest to "")
                .hentAnsettelsesperioder(Fnr.genererGyldig().verdi, "mock call-id")
        }
    }

    test("lykkes ved færre 5xx-feil enn max retries (5)") {
        shouldNotThrowAny {
            mockAaregClient(
                HttpStatusCode.InternalServerError to "",
                HttpStatusCode.InternalServerError to "",
                HttpStatusCode.InternalServerError to "",
                HttpStatusCode.InternalServerError to "",
                HttpStatusCode.InternalServerError to "",
                HttpStatusCode.OK to MockResponse.arbeidsforhold,
            ).hentAnsettelsesperioder(Fnr.genererGyldig().verdi, "mock call-id")
        }
    }

    test("feiler ved flere 5xx-feil enn max retries (5)") {
        shouldThrowExactly<ServerResponseException> {
            mockAaregClient(
                HttpStatusCode.InternalServerError to "",
                HttpStatusCode.InternalServerError to "",
                HttpStatusCode.InternalServerError to "",
                HttpStatusCode.InternalServerError to "",
                HttpStatusCode.InternalServerError to "",
                HttpStatusCode.InternalServerError to "",
            ).hentAnsettelsesperioder(Fnr.genererGyldig().verdi, "mock call-id")
        }
    }

    test("kall feiler og prøver på nytt ved timeout") {
        shouldNotThrowAny {
            mockAaregClient(
                HttpStatusCode.OK to "timeout",
                HttpStatusCode.OK to "timeout",
                HttpStatusCode.OK to "timeout",
                HttpStatusCode.OK to "timeout",
                HttpStatusCode.OK to "timeout",
                HttpStatusCode.OK to MockResponse.arbeidsforhold,
            ).hentAnsettelsesperioder(Fnr.genererGyldig().verdi, "mock call-id")
        }
    }
})
