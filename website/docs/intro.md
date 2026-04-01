---
sidebar_position: 1
slug: /
title: Binance4s
---

# Binance4s

A **typeclass-driven**, functional Scala 3 library for the [Binance API](https://developers.binance.com/docs/binance-spot-api-docs/rest-api).

Built on **Cats Effect**, **http4s**, **Circe**, and **fs2**.

## Why Binance4s?

The Binance API exposes hundreds of REST endpoints and WebSocket streams. Building a Scala client means dealing with:

- **URI construction** for each endpoint with different paths, query parameters, and authentication requirements
- **Response deserialization** for dozens of different JSON shapes (objects, arrays, arrays of arrays)
- **HMAC-SHA256 signing** for authenticated endpoints with precise query string ordering
- **HTTP client lifecycle** management across many concurrent requests
- **Error handling** for API-specific error codes, rate limits, and network failures

Binance4s solves all of this with a single abstraction.

## The Core Idea

Every Binance endpoint follows the same pattern: a **request** produces a **response** through a specific HTTP method, path, security model, and set of query parameters. This is a textbook use case for **typeclasses**.

```scala
trait BinanceEndpoint[Req, Resp]:
  def method: HttpMethod
  def path: Vector[String]
  def security: SecurityType
  def queryParams(req: Req): QueryString
  def decoder: Decoder[Resp]
```

Each endpoint is a `given` instance. The compiler statically resolves which endpoint to call and what type the response will be — **zero runtime reflection, full type safety**.

```scala
// The compiler knows this returns F[OrderBook]
client.execute(DepthReq("BTCUSDT", Some(10)))

// Equivalent via syntax extension
client.depth("BTCUSDT", limit = Some(10))
```

## Quick Example

```scala
import cats.effect.{IO, IOApp}
import io.github.rafafrdz.binance4s.config.BinanceConfig
import io.github.rafafrdz.binance4s.domain.BinanceMode
import io.github.rafafrdz.binance4s.http.client.BinanceClient
import io.github.rafafrdz.binance4s.api.*

object Main extends IOApp.Simple:
  val config = BinanceConfig(mode = BinanceMode.Live)

  def run: IO[Unit] =
    BinanceClient.resource[IO](config).use { client =>
      for
        time  <- client.serverTime
        _     <- IO.println(s"Server time: ${time.serverTime}")
        price <- client.tickerPrice("BTCUSDT")
        _     <- IO.println(s"BTC price: ${price.price}")
      yield ()
    }
```

## Features

| | |
|---|---|
| **56+ REST endpoints** | General, Market Data, Spot Trading, Account, Wallet, and User Data Stream |
| **12 WebSocket streams** | Real-time market data via `fs2.Stream` — aggTrade, trade, kline, ticker, depth, and more |
| **Typeclass architecture** | `given`/`using` for compile-time endpoint resolution — no runtime reflection |
| **Resource-safe client** | `Resource[F, BinanceClient[F]]` with shared connection pool |
| **Effect-polymorphic** | `F[_]: Async` — works with IO, ZIO interop, etc. |
| **Typed errors** | `BinanceError` enum with exhaustive pattern matching |
| **Zero-boilerplate codecs** | Circe `derives Codec.AsObject` for all response models |
| **HMAC-SHA256 signing** | Automatic for authenticated endpoints, zero external dependencies |

## Module Structure

```
binance4s-api        ← what you depend on
├── binance4s-http   ← http4s ember client with Resource lifecycle
├── binance4s-ws     ← WebSocket client with fs2 streams
└── binance4s-core   ← domain types, typeclasses, config, auth (zero HTTP deps)
```

## Tech Stack

| Library | Version | Purpose |
|---------|---------|---------|
| Scala | 3.3.6 LTS | Language |
| Cats Effect | 3.6.1 | Effect system |
| http4s | 0.23.30 | HTTP client |
| Circe | 0.14.13 | JSON codecs |
| fs2 | 3.12.0 | Streaming |
| munit | 1.1.1 | Testing |

## License

[Apache License 2.0](https://github.com/eff3ct0/binance4s/blob/master/LICENSE)
