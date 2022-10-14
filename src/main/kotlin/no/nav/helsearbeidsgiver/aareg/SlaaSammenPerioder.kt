package no.nav.helsearbeidsgiver.aareg

const val MAKS_DAGER_OPPHOLD = 3L

fun slaaSammenPerioder(perioder: MutableList<AaregPeriode>): List<Periode> {
    if (perioder.size < 2) return perioder.map { Periode(it.fom, it.tom) }

    val sammenslåttePerioder = mutableListOf(perioder.removeFirst())

    perioder.forEach { gjeldendePeriode ->
        sammenslåttePerioder.find {
            gjeldendePeriode.overlapperPeriode(it)
        }?.let { overlappendePeriode ->
            with(overlappendePeriode) {
                fom = minOf(fom, gjeldendePeriode.fom)
                tom = maxOf(tom, gjeldendePeriode.tom)
            }
            return@forEach
        }
        sammenslåttePerioder.add(gjeldendePeriode)
    }

    return sammenslåttePerioder
        .map { Periode(it.fom, it.tom) }
}
