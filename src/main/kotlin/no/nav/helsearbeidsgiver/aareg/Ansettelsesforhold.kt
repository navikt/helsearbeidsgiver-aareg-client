@file:UseSerializers(LocalDateSerializer::class)

package no.nav.helsearbeidsgiver.aareg

import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import no.nav.helsearbeidsgiver.utils.json.serializer.LocalDateSerializer
import java.time.LocalDate

@Serializable
data class Ansettelsesforhold(
    val startdato: LocalDate,
    val sluttdato: LocalDate? = null,
    val yrkesKode: String? = null,
    val yrkesBeskrivelse: String? = null,
    val stillingsprosent: Double? = null,
)
