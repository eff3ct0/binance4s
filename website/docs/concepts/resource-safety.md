---
sidebar_position: 2
---

# Resource Safety

Binance4s uses Cats Effect `Resource` to manage HTTP client lifecycle. This guarantees the connection pool is properly created and destroyed, even in the presence of errors or cancellation.

## The Problem

A common anti-pattern in API clients is creating a new HTTP connection for every request:

```scala
// BAD: creates and destroys a connection pool per request
def execute(req: Request): IO[Response] =
  EmberClientBuilder.default[IO].build.use { client =>
    client.run(req).use(_.as[Json])
  }
```

This is catastrophically wasteful. Each call establishes a new TLS handshake, TCP connection, and connection pool.

## The Solution

Binance4s creates the HTTP client once as a `Resource` and shares it across all requests:

```scala
BinanceClient.resource[IO](config).use { client =>
  // All requests share the same connection pool
  for
    a <- client.ping
    b <- client.serverTime
    c <- client.depth("BTCUSDT")
    d <- client.tickerPrice("ETHUSDT")
  yield ()
}
// Connection pool is closed here, even if an error occurred
```

## How It Works

```scala
object BinanceClient:
  def resource[F[_]: Async](cfg: BinanceConfig): Resource[F, BinanceClient[F]] =
    EmberClientBuilder.default[F].build.map { httpClient =>
      BinanceHttpClient[F](cfg, httpClient)
    }
```

The `Resource` monad guarantees:
- The HTTP client is created **exactly once**
- It is available for the entire `use` block
- It is **properly closed** when `use` completes (success, error, or cancellation)
- Connection pooling, keep-alive, and TLS session reuse all work correctly

## Long-Running Applications

For servers or streaming applications, keep the resource open for the application lifetime:

```scala
object MyApp extends IOApp.Simple:
  def run: IO[Unit] =
    BinanceClient.resource[IO](config).use { client =>
      // This stays open until the app shuts down
      streamMarketData(client) *> serveApi(client)
    }
```

## Combining REST and WebSocket

Both `BinanceClient` and `BinanceWsClient` are resources. Combine them:

```scala
val resources = for
  rest <- BinanceClient.resource[IO](config)
  ws   <- BinanceWsClient.resource[IO](config)
yield (rest, ws)

resources.use { (rest, ws) =>
  for
    key <- rest.createListenKey
    _   <- ws.subscribe(UserDataStream(key.listenKey))
              .evalMap(event => IO.println(s"Event: $event"))
              .compile.drain
  yield ()
}
```
