package no.nav.helsearbeidsgiver.aareg

import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.time.LocalDate.of
import java.time.LocalDateTime

internal class ErAktivtArbeidsforholdKtTest {
    val arbeidsgiver = Arbeidsgiver("STARK IND", "123456785")
    val opplysningspliktig = Opplysningspliktig("STARK IND", "1212121212")

    @Test
    fun `Gyldig arbeidsforhold og aktivt arbeidsforhold`() {
        val arbeidsForhold = listOf(
            Arbeidsforhold(
                arbeidsgiver,
                opplysningspliktig,
                emptyList(),
                Ansettelsesperiode(
                    Periode(
                        of(2021, 1, 1),
                        null
                    )
                ),
                LocalDateTime.now()
            )
        )

        val arbeidsgiverPerioder = listOf(
            Periode(
                of(2022, 1, 1),
                of(2022, 1, 10)
            )
        )
        assertTrue(
            erAktivtArbeidsforhold(
                arbeidsgiverPerioder,
                arbeidsgiver.organisasjonsnummer!!,
                arbeidsForhold
            )
        )
    }

    @Test
    fun `Gyldig arbeidsforhold og aktivt definert arbeidsforhold`() {
        val arbeidsForhold = listOf(
            Arbeidsforhold(
                arbeidsgiver,
                opplysningspliktig,
                emptyList(),
                Ansettelsesperiode(
                    Periode(
                        of(2021, 1, 1),
                        of(2022, 3, 1)
                    )
                ),
                LocalDateTime.now()
            )
        )

        val arbeidsgiverPerioder = listOf(
            Periode(
                of(2022, 1, 1),
                of(2022, 1, 10)
            )
        )
        assertTrue(
            erAktivtArbeidsforhold(
                arbeidsgiverPerioder,
                arbeidsgiver.organisasjonsnummer!!,
                arbeidsForhold
            )
        )
    }

    @Test
    fun `Arbeidsforhold utenfor periode`() {
        val arbeidsForhold = listOf(
            Arbeidsforhold(
                arbeidsgiver,
                opplysningspliktig,
                emptyList(),
                Ansettelsesperiode(
                    Periode(
                        of(2021, 1, 1),
                        of(2022, 3, 1)
                    )
                ),
                LocalDateTime.now()
            )
        )

        val arbeidsgiverPerioder = listOf(
            Periode(
                of(2022, 3, 1),
                of(2022, 3, 10)
            )
        )
        assertFalse(
            erAktivtArbeidsforhold(
                arbeidsgiverPerioder,
                arbeidsgiver.organisasjonsnummer!!,
                arbeidsForhold
            )
        )
    }

    @Test
    fun `Ikke arbeidsforhold i gitt virksomhet`() {
        val arbeidsForhold = listOf(
            Arbeidsforhold(
                arbeidsgiver,
                opplysningspliktig,
                emptyList(),
                Ansettelsesperiode(
                    Periode(
                        of(2021, 1, 1),
                        null
                    )
                ),
                LocalDateTime.now()
            )
        )

        val arbeidsgiverPerioder = listOf(
            Periode(
                of(2022, 1, 1),
                of(2022, 1, 10)
            )
        )
        assertFalse(
            erAktivtArbeidsforhold(
                arbeidsgiverPerioder,
                "annen org",
                arbeidsForhold
            )
        )
    }
}
