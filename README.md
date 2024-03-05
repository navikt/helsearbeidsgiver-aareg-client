# helsearbeidsgiver-aareg-client

Henter arbeidsforhold fra Arbeidsgiver- og arbeidstakerregisteret ([aareg](https://navikt.github.io/aareg/)).

### Bruk av helsearbeidsgiver-aareg-client

***gradle.build.kts***
```kts
val aaregClientVersion: String by project

dependencies {
    implementation("no.nav.helsearbeidsgiver:aareg-client:$aaregClientVersion")
}
```
 
### Klienten instansieres slik

```kt
import kotlinx.coroutines.runBlocking
import no.nav.helsearbeidsgiver.aareg.AaregClient

fun main() {
    val url = "https://modapp-q1.adeo.no/aareg-services"

    val aaregClient = AaregClient(url) { "et gyldig token" }

    val arbeidsforhold = runBlocking { aaregClient.hentArbeidsforhold("fnr", "callId") }
    println(arbeidsforhold)
}
```
