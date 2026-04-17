@file:UseSerializers(LocalDateSerializer::class)

package no.nav.helsearbeidsgiver.aareg

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.UseSerializers
import no.nav.helsearbeidsgiver.utils.json.serializer.LocalDateSerializer
import java.time.LocalDate

@Serializable
internal data class Arbeidsforhold(
    val arbeidssted: Arbeidssted,
    val ansettelsesperiode: Ansettelsesperiode,
)

@Serializable
internal data class Arbeidssted(
    val type: ArbeidsstedType,
    val identer: List<Ident>,
) {
    fun organisasjonsnummer(): String? =
        if (type == ArbeidsstedType.UNDERENHET) {
            identer.firstOrNull { it.type == IdentType.ORGANISASJONSNUMMER }?.ident
        } else {
            null
        }
}

@Serializable
internal enum class ArbeidsstedType {
    @SerialName("Underenhet")
    UNDERENHET,

    @SerialName("Person")
    PERSON,
}

@Serializable
internal data class Ident(
    val type: IdentType,
    val ident: String,
    val gjeldende: Boolean? = null,
)

@Serializable
internal enum class IdentType {
    AKTORID,
    FOLKEREGISTERIDENT,
    ORGANISASJONSNUMMER,
}

@Serializable
internal data class Ansettelsesperiode(
    val startdato: LocalDate,
    val sluttdato: LocalDate? = null,
)
