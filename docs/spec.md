# API-spesifikasjon – Aareg-klienten

Denne klienten integrerer mot **Arbeidsgiver- og arbeidstakerregisteret (Aareg)** sitt REST-API.

## Swagger / OpenAPI

| Miljø | URL |
|-------|-----|
| Dev (intern) | [aareg-services.dev.intern.nav.no/swagger-ui](https://aareg-services.dev.intern.nav.no/swagger-ui/index.html?urls.primaryName=aareg.api.v2) |
| Prod (intern) | `https://aareg-services.intern.nav.no/swagger-ui/index.html?urls.primaryName=aareg.api.v2` |

> **Merk:** Swagger-URLene er kun tilgjengelige fra Nav-internt nett.

Generell API-dokumentasjon er tilgjengelig på [navikt.github.io/aareg](https://navikt.github.io/aareg/tjenester/integrasjon/api/).

## Endepunkt denne klienten bruker

### `GET /api/v2/arbeidstaker/arbeidsforhold`

Henter arbeidsforhold (detaljer) for en arbeidstaker.

**Query-parametere:**

| Parameter | Verdi | Beskrivelse |
|-----------|-------|-------------|
| `sporingsinformasjon` | `false` | Skal sporingsinformasjon inkluderes i respons? |
| `historikk` | `false` | Skal historikk inkluderes i respons? |

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
    "arbeidssted": {
      "type": "Underenhet",
      "identer": [
        { "type": "ORGANISASJONSNUMMER", "ident": "123456789" }
      ]
    },
    "ansettelsesperiode": {
      "startdato": "2021-01-01",
      "sluttdato": null
    }
  }
]
```

## Base-URLer

| Miljø | Base-URL |
|-------|----------|
| Dev | `https://aareg-services.dev.intern.nav.no` |
| Prod | `https://aareg-services.intern.nav.no` |
