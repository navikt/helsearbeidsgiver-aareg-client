package no.nav.helsearbeidsgiver.aareg

import kotlinx.serialization.Serializable

@Serializable
internal data class Arbeidsforhold(
    val arbeidsgiver: Arbeidsgiver,
    val ansettelsesperiode: Ansettelsesperiode,
)

@Serializable
internal data class Arbeidsgiver(
    val organisasjonsnummer: String? = null,
)

@Serializable
internal data class Ansettelsesperiode(
    val periode: Periode,
)
