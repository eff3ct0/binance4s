# binance4s

[![CI](https://github.com/rafafrdz/binance4s/actions/workflows/ci.yml/badge.svg)](https://github.com/rafafrdz/binance4s/actions/workflows/ci.yml)
[![Scala 3.3](https://img.shields.io/badge/Scala-3.3-red.svg)](https://www.scala-lang.org/)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://www.apache.org/licenses/LICENSE-2.0)

A **typeclass-driven**, functional Scala 3 library for the [Binance API](https://developers.binance.com/docs/binance-spot-api-docs/rest-api). Built on **Cats Effect**, **http4s**, **Circe**, and **fs2**.

## Features

- **Full Binance Spot API coverage** -- 56+ REST endpoints, 12 WebSocket stream types
- **Typeclass architecture** -- endpoints defined as `given BinanceEndpoint[Req, Resp]` instances, compile-time type safety between request and response
- **Resource-safe HTTP client** -- shared connection pool via `Resource[F, BinanceClient[F]]`
- **Effect-polymorphic** -- works with any `F[_]: Async` (IO, ZIO interop, etc.)
- **WebSocket streams** -- real-time market data via `fs2.Stream[F, A]`
- **Typed error handling** -- `BinanceError` enum with pattern matching
- **Zero-boilerplate codecs** -- Circe `derives Codec.AsObject` for all response models
- **HMAC-SHA256 signing** -- automatic for authenticated endpoints, zero external dependencies

## Quick Start

```scala
//> using dep "io.github.rafafrdz::binance4s-api:2.0.0-SNAPSHOT"

import cats.effect.{IO, IOApp}
import io.github.rafafrdz.binance4s.config.BinanceConfig
import io.github.rafafrdz.binance4s.domain.{BinanceMode, KlineInterval}
import io.github.rafafrdz.binance4s.http.client.BinanceClient
import io.github.rafafrdz.binance4s.api.*

object Main extends IOApp.Simple:
  val config = BinanceConfig(mode = BinanceMode.Live)

  def run: IO[Unit] =
    BinanceClient.resource[IO](config).use { client =>
      for
        time  <- client.serverTime
        _     <- IO.println(s"Server time: ${time.serverTime}")
        book  <- client.depth("BTCUSDT", limit = Some(5))
        _     <- IO.println(s"Best bid: ${book.bids.head.price}")
        price <- client.tickerPrice("ETHUSDT")
        _     <- IO.println(s"ETH price: ${price.price}")
      yield ()
    }
```

## Architecture

```
binance4s/
  modules/
    core/       -- Domain enums, typeclasses, config, auth, error (zero HTTP deps)
    http/       -- http4s client with Resource lifecycle
    ws/         -- WebSocket client with fs2 streams
    api/        -- All endpoint definitions + syntax extensions
    examples/   -- Runnable IOApp demos
```

### Core Typeclass: `BinanceEndpoint[Req, Resp]`

Every API endpoint is a typeclass instance that links a request type to its response:

```scala
trait BinanceEndpoint[Req, Resp]:
  def method: HttpMethod
  def prefix: ApiPrefix
  def version: ApiVersion
  def path: Vector[String]
  def security: SecurityType
  def queryParams(req: Req): QueryString
  def decoder: Decoder[Resp]
  def weight: Int
```

Endpoints are defined as `given` instances -- the compiler resolves them at the call site:

```scala
// Definition
given BinanceEndpoint[AvgPriceReq, AvgPrice] with
  def method   = HttpMethod.GET
  def path     = Vector("avgPrice")
  def security = SecurityType.None
  def queryParams(req: AvgPriceReq) = QueryString.empty.add("symbol", req.symbol)
  def decoder  = Decoder[AvgPrice]
  // ...

// Usage -- compiler infers F[AvgPrice]
client.execute(AvgPriceReq("BTCUSDT"))

// Or via syntax extension
client.avgPrice("BTCUSDT")
```

## API Coverage

### REST Endpoints (56+)

| Category | Endpoints | Auth |
|----------|-----------|------|
| General | ping, serverTime, exchangeInfo | None |
| Market Data | depth, trades, historicalTrades, aggTrades, klines, uiKlines, avgPrice, ticker24h, tradingDayTicker, tickerPrice, bookTicker, rollingTicker, referencePrice | None / API Key |
| Spot Trading | newOrder, testOrder, cancelOrder, cancelAllOpenOrders, cancelReplaceOrder, newOco, cancelOrderList, sorOrder | HMAC Signed |
| Account | accountInfo, orderStatus, openOrders, allOrders, myTrades, rateLimitOrder, commission, preventedMatches, allocations | HMAC Signed |
| Wallet | systemStatus, allCoins, depositHistory, withdrawHistory, depositAddress, withdraw, transfer, dustLog, assetDetail, apiRestrictions, delistSchedule | HMAC Signed |
| User Data Stream | createListenKey, keepAliveListenKey, closeListenKey | API Key |

### WebSocket Streams (12 types)

`aggTrade`, `trade`, `kline`, `miniTicker`, `allMiniTicker`, `ticker`, `allTicker`, `bookTicker`, `avgPrice`, `depth`, `diffDepth`, `userData`

## WebSocket Example

```scala
import io.github.rafafrdz.binance4s.ws.client.BinanceWsClient
import io.github.rafafrdz.binance4s.api.ws.*

BinanceWsClient.resource[IO](config).use { ws =>
  ws.subscribe(KlineStream("BTCUSDT", KlineInterval.`1m`))
    .take(10)
    .evalMap(k => IO.println(s"Close: ${k.k.c}, Volume: ${k.k.v}"))
    .compile.drain
}
```

## Authenticated Endpoints

```scala
val config = BinanceConfig.builder
  .live
  .credentials("your-api-key", "your-secret-key")
  .build

BinanceClient.resource[IO](config).use { client =>
  for
    account <- client.accountInfo
    _       <- IO.println(s"Balances: ${account.balances.filter(_.free.toDouble > 0)}")
    order   <- client.newOrder(NewOrderReq(
      symbol = "BTCUSDT",
      side = OrderSide.BUY,
      `type` = OrderType.MARKET,
      quantity = Some(BigDecimal("0.001"))
    ))
    _ <- IO.println(s"Order placed: ${order.orderId}")
  yield ()
}
```

## Configuration

| Method | Description |
|--------|-------------|
| `BinanceConfig(mode = BinanceMode.Live)` | Direct construction |
| `BinanceConfig.builder.live.credentials(key, secret).build` | Builder pattern |
| `BinanceConfig.fromEnv[IO]` | From `BINANCE_MODE`, `BINANCE_API_KEY`, `BINANCE_SECRET_KEY` env vars |

## Error Handling

All errors are typed via `BinanceError`:

```scala
import io.github.rafafrdz.binance4s.error.BinanceError

client.depth("INVALID", Some(5)).attempt.flatMap {
  case Left(BinanceError.ApiError(code, msg)) => IO.println(s"API error $code: $msg")
  case Left(BinanceError.RateLimitError(_))   => IO.println("Rate limited!")
  case Left(BinanceError.HttpError(status, _)) => IO.println(s"HTTP $status")
  case Left(other)                             => IO.println(s"Error: $other")
  case Right(book)                             => IO.println(s"Got ${book.bids.size} bids")
}
```

## Tech Stack

| Library | Version | Purpose |
|---------|---------|---------|
| Scala | 3.3.4 | Language |
| Cats Effect | 3.5.7 | Effect system |
| http4s | 0.23.30 | HTTP client |
| Circe | 0.14.10 | JSON codecs |
| fs2 | 3.11.0 | Streaming |
| munit | 1.0.3 | Testing |

## License

[Apache License 2.0](LICENSE)
