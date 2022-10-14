package no.nav.helsearbeidsgiver.aareg

import java.time.LocalDate

fun LocalDate.isAfterOrEqual(other: LocalDate): Boolean =
    this.isEqual(other) || this.isAfter(other)

fun LocalDate.isBeforeOrEqual(other: LocalDate): Boolean =
    this.isEqual(other) || this.isBefore(other)
