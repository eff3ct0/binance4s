---
sidebar_position: 2
---

# User Data Stream

Receive real-time account updates via WebSocket: balance changes, order execution reports, and order list status updates.

> Binance API Reference: [User Data Streams](https://developers.binance.com/docs/binance-spot-api-docs/web-socket-streams/user-data-streams)

## Setup

User data streams require a **listen key** obtained from the REST API:

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

## Event Types

The user data stream sends the following event types as JSON:

### Account Update (`outboundAccountPosition`)

Pushed when account balances change (trade, deposit, withdrawal).

### Balance Update (`balanceUpdate`)

Pushed when balance changes not related to trades (deposits, withdrawals).

### Order Update (`executionReport`)

Pushed on every order state change: new, filled, partially filled, canceled, expired.

### Order List Status (`listStatus`)

Pushed when an OCO order list status changes.

:::note
User data events are returned as raw `Json` from `UserDataStream`. Parse the `e` field to determine the event type and decode accordingly.
:::

## Keep-Alive

Listen keys expire after **60 minutes**. Run a keep-alive in parallel:

```scala
import scala.concurrent.duration.*

val keepAlive = fs2.Stream
  .fixedRate[IO](30.minutes)
  .evalMap(_ => rest.keepAliveListenKey(key.listenKey))
  .compile.drain

val listen = ws.subscribe(UserDataStream(key.listenKey))
  .evalMap(event => IO.println(s"$event"))
  .compile.drain

// Run both concurrently
(IO.race(keepAlive, listen)).void
```
