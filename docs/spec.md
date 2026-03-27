# API-spesifikasjon – Aareg-klienten

Denne klienten integrerer mot **Arbeidsgiver- og arbeidstakerregisteret (Aareg)** sitt REST-API.

## Swagger / OpenAPI

| Miljø | URL |
|-------|-----|
| Dev (intern) | [aareg-services.dev.intern.nav.no/swagger-ui](https://aareg-services.dev.intern.nav.no/swagger-ui/index.html?urls.primaryName=aareg.api.v1) |
| Prod (intern) | `https://aareg-services.intern.nav.no/swagger-ui/index.html?urls.primaryName=aareg.api.v1` |

> **Merk:** Swagger-URLene er kun tilgjengelige fra Nav-internt nett.

Generell API-dokumentasjon er tilgjengelig på [navikt.github.io/aareg](https://navikt.github.io/aareg/tjenester/integrasjon/api/).

## Endepunkt denne klienten bruker

### `GET /api/v1/arbeidstaker/arbeidsforhold`

Henter arbeidsforhold for en arbeidstaker.

**Query-parametere:**

| Parameter | Verdi | Beskrivelse |
|-----------|-------|-------------|
| `sporingsinformasjon` | `false` | Ekskluderer sporingsinformasjon fra responsen |
| `historikk` | `false` | Ekskluderer historiske arbeidsforhold |

**Request-headere:**

| Header | Beskrivelse |
|--------|-------------|
| `Authorization` | `Bearer <token>` – tilgangstoken (Maskinporten/TokenX/Azure AD) |
| `Nav-Personident` | Fødselsnummer (FNR) til arbeidstakeren |
| `X-Correlation-ID` | Korrelasjons-ID for sporing på tvers av tjenester |

**Eksempel-respons:**

```json
[
  {
    "arbeidsgiver": {
      "organisasjonsnummer": "123456789"
    },
    "ansettelsesperiode": {
      "periode": {
        "fom": "2021-01-01",
        "tom": null
      }
    }
  }
]
```

## Base-URLer

| Miljø | Base-URL |
|-------|----------|
| Dev | `https://aareg-services.dev.intern.nav.no` |
| Prod | `https://aareg-services.intern.nav.no` |
