# helsearbeidsgiver-aareg-client

Henter arbeidsforhold fra Arbeidsgiver- og arbeidstakerregisteret ([aareg](https://navikt.github.io/aareg/)).

### Bruk av helsearbeidsgiver-aareg-client

***gradle.build.kts***
```kts
val tokenproviderVersion: String by project
val aaregClientVersion: String by project

dependencies {
    implementation("no.nav.helsearbeidsgiver:tokenprovider:$tokenproviderVersion")
    implementation("no.nav.helsearbeidsgiver:aareg-client:$aaregClientVersion")
}
```

### Klienten instansieres slik

```kt
import kotlinx.coroutines.runBlocking
import no.nav.helsearbeidsgiver.tokenprovider.OAuth2TokenProvider
import no.nav.helsearbeidsgiver.aareg.AaregClient

fun main() {
    val url = "https://modapp-q1.adeo.no/aareg-services"
    val tokenProvider = OAuth2TokenProvider(
        // Token config
    )

    val aaregClient = AaregClient(url, "callId") { tokenProvider.getToken() }

    val arbeidsforhold = runBlocking { aaregClient.hentArbeidsforhold("fnr") }
    println(arbeidsforhold)
}
```
