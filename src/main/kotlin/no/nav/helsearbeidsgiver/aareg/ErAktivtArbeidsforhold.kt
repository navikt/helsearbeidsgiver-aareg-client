package no.nav.helsearbeidsgiver.aareg

import java.time.LocalDate.MAX

fun erAktivtArbeidsforhold(
    arbeidsgiverPerioder: List<Periode>,
    virksomhetsnummer: String,
    arbeidsforhold: List<Arbeidsforhold>
): Boolean {
    val ansattPerioder = arbeidsforhold
        .asSequence()
        .filter { it.arbeidsgiver.organisasjonsnummer == virksomhetsnummer }
        .map { it.ansettelsesperiode.periode }
        .map { AaregPeriode(it.fom!!, it.tom ?: MAX) }
        .sortedBy { it.fom }
        .toMutableList()

    val sammenslaattePerioder = slaaSammenPerioder(ansattPerioder)

    return arbeidsgiverPerioder.all { agp ->
        sammenslaattePerioder.any { ansattPeriode ->
            (agp.tom!!.isBeforeOrEqual(ansattPeriode.tom!!)) && (ansattPeriode.fom!!.isBeforeOrEqual(agp.fom!!))
        }
    }
}
