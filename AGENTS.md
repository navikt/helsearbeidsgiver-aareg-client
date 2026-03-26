# AGENTS.md — navikt/helsearbeidsgiver-aareg-client

## Repository Overview

Kotlin-klientbibliotek for å hente arbeidsforhold fra Arbeidsgiver- og arbeidstakerregisteret (Aareg) sitt REST-API.
Se [docs/spec.md](docs/spec.md) for API-spesifikasjon og Swagger-lenker.

## Tech Stack

- Kotlin (JVM 21)
- Ktor client (Apache5 engine)
- kotlinx.serialization for JSON
- Kotest for testing
- Ktlint for linting

## Build & Test Commands

```bash
./gradlew build        # Kompiler og kjør alle tester
./gradlew test         # Kjør tester
./gradlew ktlintCheck  # Kjør linter
./gradlew ktlintFormat # Fiks linting automatisk
```

## Code Standards

- Følg Kotlin-kodekonvensjoner og ktlint-regler
- Token injiseres via lambda `() -> String` – aldri hardkod tokens
- Bruk `bearerAuth()` for å sende token i HTTP-header
- Legg alltid til `X-Correlation-ID`-header for sporing

## Klientmønstre

- `AaregClient` er den eneste public API-klassen
- HTTP-klient konfigureres i `HttpUtils.kt` med retry, timeout og content negotiation
- Cache på FNR-nivå via `LocalCache` fra `helsearbeidsgiver-utils`
- Data-modeller er `@Serializable` data classes

## Testing

- Bruk `ktor-client-mock` for å mocke HTTP-responser
- Mock-responser ligger i `src/test/resources/`
- Bruk `shouldThrowExactly<>` fra Kotest for feilscenarier

## Boundaries

### ✅ Always

- Følg eksisterende kodemønstre
- Kjør `./gradlew build` før commit
- Legg til tester for ny funksjonalitet
- Bruk parameteriserte URL-er, aldri string-konkatenering med brukerdata

### ⚠️ Ask First

- Endre autentiseringsmekanisme
- Endre Aareg API-endepunkt eller query-parametere
- Legge til nye avhengigheter

### 🚫 Never

- Hardkod tokens, secrets eller credentials
- Logg `Authorization`- eller `Nav-Personident`-headere
- Bypass sertifikatvalidering (TLS)
