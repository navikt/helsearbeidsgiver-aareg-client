# helsearbeidsgiver-aareg-client

Henter arbeidsforhold fra Arbeidsgiver- og arbeidstakerregisteret ([aareg](https://navikt.github.io/aareg/)).

### Publisere nye versjoner

For å publisere snapshots, push til en branch som starter med `dev/`.
Snapshot-versjonen er basert på `version` i `gradle.properties`. Ved `version=1.2.3` så vil workflow publisere en snapshot `1.2.3-SNAPSHOT`.
Snapshot-versjoner overskrives for hvert push.

For å publisere ny versjon, oppdater `version` i `gradle.properties` og push til branch `main`.
Dersom versjon allerede eksisterer så vil workflow feile med `409 Conflict`.

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
