---
sidebar_position: 2
---

# Getting Started

## Installation

Add the dependency to your `build.sbt`:

```scala
libraryDependencies += "io.github.eff3ct0" %% "binance4s-api" % "2.0.0-SNAPSHOT"
```

This transitively brings in `binance4s-core`, `binance4s-http`, and `binance4s-ws`.

## Your First Request

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
        time <- client.serverTime
        _    <- IO.println(s"Server time: ${time.serverTime}")
      yield ()
    }
```

Key points:

1. **`BinanceConfig`** holds your connection settings (mode, credentials, recvWindow)
2. **`BinanceClient.resource[IO]`** creates a resource-safe HTTP client with a shared connection pool
3. **`.use { client => ... }`** ensures the client is properly closed when done
4. **`import io.github.rafafrdz.binance4s.api.*`** brings the syntax extensions into scope

## Market Data

No authentication required for public endpoints:

```scala
import io.github.rafafrdz.binance4s.domain.KlineInterval

BinanceClient.resource[IO](config).use { client =>
  for
    book  <- client.depth("BTCUSDT", limit = Some(5))
    _     <- IO.println(s"Best bid: ${book.bids.head.price}")
    _     <- IO.println(s"Best ask: ${book.asks.head.price}")
    avg   <- client.avgPrice("ETHUSDT")
    _     <- IO.println(s"ETH avg price: ${avg.price}")
    kl    <- client.klines("BTCUSDT", KlineInterval.`1h`, limit = Some(3))
    _     <- kl.traverse_(k => IO.println(s"O: ${k.open} C: ${k.close}"))
  yield ()
}
```

## Authenticated Requests

For trading and account endpoints, provide API credentials:

```scala
val config = BinanceConfig.builder
  .live
  .credentials("your-api-key", "your-secret-key")
  .build

BinanceClient.resource[IO](config).use { client =>
  for
    account <- client.accountInfo
    _       <- IO.println(s"Can trade: ${account.canTrade}")
    _       <- account.balances
                 .filter(_.free.toDouble > 0)
                 .traverse_(b => IO.println(s"${b.asset}: ${b.free}"))
  yield ()
}
```

:::tip
The library automatically adds the `timestamp` parameter and HMAC-SHA256 signature for endpoints that require authentication. You don't need to manage this manually.
:::

## WebSocket Streams

Subscribe to real-time market data:

```scala
import io.github.rafafrdz.binance4s.ws.client.BinanceWsClient
import io.github.rafafrdz.binance4s.api.ws.*

BinanceWsClient.resource[IO](config).use { ws =>
  ws.subscribe(KlineStream("BTCUSDT", KlineInterval.`1m`))
    .take(10)
    .evalMap(k => IO.println(s"Close: ${k.k.c}, Volume: ${k.k.v}"))
    .compile
    .drain
}
```

## Module Structure

```
binance4s-api      -- All endpoint definitions + syntax (what you import)
  |-- binance4s-http   -- http4s client implementation
  |-- binance4s-ws     -- WebSocket client with fs2
  |-- binance4s-core   -- Domain types, typeclasses, config, auth (zero HTTP deps)
```

You typically only depend on `binance4s-api`, which transitively pulls in everything else.
