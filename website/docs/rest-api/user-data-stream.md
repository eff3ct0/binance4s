---
sidebar_position: 6
---

# User Data Stream

Manage listen keys for WebSocket user data streams. These endpoints require an **API key** but not a signature.

> Binance API Reference: [User Data Streams](https://developers.binance.com/docs/binance-spot-api-docs/rest-api/user-data-stream-endpoints)

## Create Listen Key

Start a new user data stream. Returns a `listenKey` that can be used to subscribe to WebSocket user data events.

```scala
val key: F[ListenKey] = client.createListenKey
// ListenKey(listenKey: String)
```

| | |
|---|---|
| **Endpoint** | `POST /api/v3/userDataStream` |
| **Auth** | API Key (no signature) |
| **Weight** | 2 |

## Keep-Alive Listen Key

Extend the validity of an existing listen key. Listen keys expire after 60 minutes without a keep-alive.

```scala
val _: F[Json] = client.keepAliveListenKey(key.listenKey)
```

| | |
|---|---|
| **Endpoint** | `PUT /api/v3/userDataStream` |
| **Auth** | API Key (no signature) |
| **Weight** | 2 |

:::tip
For long-running applications, send a keep-alive every 30 minutes:
```scala
import scala.concurrent.duration.*

fs2.Stream
  .fixedRate[IO](30.minutes)
  .evalMap(_ => client.keepAliveListenKey(key.listenKey))
  .compile.drain
```
:::

## Close Listen Key

Close a user data stream.

```scala
val _: F[Json] = client.closeListenKey(key.listenKey)
```

| | |
|---|---|
| **Endpoint** | `DELETE /api/v3/userDataStream` |
| **Auth** | API Key (no signature) |
| **Weight** | 2 |

## Complete User Data Stream Example

Combining listen key management with WebSocket subscription:

```scala
import io.github.rafafrdz.binance4s.ws.client.BinanceWsClient
import io.github.rafafrdz.binance4s.api.ws.UserDataStream

val resources = for
  rest <- BinanceClient.resource[IO](config)
  ws   <- BinanceWsClient.resource[IO](config)
yield (rest, ws)

resources.use { (rest, ws) =>
  for
    key <- rest.createListenKey
    _   <- IO.println(s"Listen key: ${key.listenKey}")
    _   <- ws.subscribe(UserDataStream(key.listenKey))
              .evalMap(event => IO.println(s"User event: $event"))
              .compile.drain
  yield ()
}
```
