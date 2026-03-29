---
sidebar_position: 1
---

# General Endpoints

Test connectivity and retrieve exchange metadata. These endpoints require no authentication.

> Binance API Reference: [General Endpoints](https://developers.binance.com/docs/binance-spot-api-docs/rest-api/general-endpoints)

## Ping

Test connectivity to the API.

```scala
val pong: F[Json] = client.ping
```

| | |
|---|---|
| **Endpoint** | `GET /api/v3/ping` |
| **Auth** | None |
| **Weight** | 1 |

## Server Time

Get the current server time.

```scala
val time: F[ServerTime] = client.serverTime
// ServerTime(serverTime: Long)
```

| | |
|---|---|
| **Endpoint** | `GET /api/v3/time` |
| **Auth** | None |
| **Weight** | 1 |

## Exchange Information

Get current exchange trading rules and symbol information.

```scala
// All symbols
val info: F[ExchangeInfo] = client.exchangeInfo

// Single symbol
val btcInfo: F[ExchangeInfo] = client.exchangeInfo("BTCUSDT")
```

| | |
|---|---|
| **Endpoint** | `GET /api/v3/exchangeInfo` |
| **Auth** | None |
| **Weight** | 20 |

**Response model:**

```scala
case class ExchangeInfo(
  timezone: String,
  serverTime: Long,
  rateLimits: List[RateLimit],
  symbols: List[SymbolInfo]
)
```
