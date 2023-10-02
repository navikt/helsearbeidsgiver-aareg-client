@file:UseSerializers(LocalDateSerializer::class, LocalDateTimeSerializer::class)

package no.nav.helsearbeidsgiver.aareg

import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import no.nav.helsearbeidsgiver.utils.json.serializer.LocalDateSerializer
import no.nav.helsearbeidsgiver.utils.json.serializer.LocalDateTimeSerializer
import java.time.LocalDate
import java.time.LocalDateTime

@Serializable
data class Arbeidsforhold(
    val arbeidsgiver: Arbeidsgiver,
    val opplysningspliktig: Opplysningspliktig,
    val arbeidsavtaler: List<Arbeidsavtale>,
    val ansettelsesperiode: Ansettelsesperiode,
    val registrert: LocalDateTime,
)

@Serializable
data class Arbeidsavtale(
    val stillingsprosent: Double?,
    val gyldighetsperiode: Periode,
)

@Serializable
data class Ansettelsesperiode(
    val periode: Periode,
)

@Serializable
data class Arbeidsgiver(
    val type: String,
    val organisasjonsnummer: String?,
)

@Serializable
data class Periode(
    val fom: LocalDate?,
    val tom: LocalDate? = null,
)

@Serializable
data class Opplysningspliktig(
    val type: String,
    val organisasjonsnummer: String?,
)
