package no.nav.helsearbeidsgiver.aareg

import java.time.LocalDate

data class AaregPeriode(
    var fom: LocalDate,
    var tom: LocalDate
) {
    fun overlapperPeriode(
        periode: AaregPeriode,
        dager: Long = MAKS_DAGER_OPPHOLD
    ): Boolean = this.fom.minusDays(dager).isBeforeOrEqual(periode.tom)
}
