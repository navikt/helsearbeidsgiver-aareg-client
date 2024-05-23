# helsearbeidsgiver-aareg-client

Henter arbeidsforhold fra Arbeidsgiver- og arbeidstakerregisteret ([aareg](https://navikt.github.io/aareg/)).

### Bygg / Release

For å publisere snapshots, lag en branch som starter med dev/
Bump version i gradle.properties og inkluder -SNAPSHOT: version=1.2.3-SNAPSHOT

Github Action vil nå publisere SNAPSHOT-version ved push til branch

For å release:
Fjern "-SNAPSHOT" fra version og merge / push til main-branch.

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
