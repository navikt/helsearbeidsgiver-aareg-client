@file:UseSerializers(LocalDateSerializer::class)

package no.nav.helsearbeidsgiver.aareg

import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import no.nav.helsearbeidsgiver.utils.json.serializer.LocalDateSerializer
import java.time.LocalDate

@Serializable
data class Periode(
    val fom: LocalDate,
    val tom: LocalDate? = null,
)
