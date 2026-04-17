package no.nav.helsearbeidsgiver.aareg

import kotlinx.serialization.Serializable

@Serializable
data class Ansettelsesinfo(
    val periode: Periode,
    val yrkeskode: String? = null,
    val stillingsprosent: Double? = null,
)
