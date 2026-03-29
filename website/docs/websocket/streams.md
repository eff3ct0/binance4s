---
sidebar_position: 1
---

# WebSocket Streams

Subscribe to real-time market data via WebSocket. All streams return `fs2.Stream[F, A]` with typed event models.

> Binance API Reference: [WebSocket Streams](https://developers.binance.com/docs/binance-spot-api-docs/web-socket-streams)

## Setup

```scala
import io.github.rafafrdz.binance4s.ws.client.BinanceWsClient
import io.github.rafafrdz.binance4s.api.ws.*

BinanceWsClient.resource[IO](config).use { ws =>
  ws.subscribe(KlineStream("BTCUSDT", KlineInterval.`1m`))
    .evalMap(k => IO.println(s"Close: ${k.k.c}"))
    .compile.drain
}
```

## Available Stream Types

### Aggregate Trade

Compressed trade information pushed every time a trade occurs.

```scala
ws.subscribe(AggTradeStream("BTCUSDT"))
// Stream[F, WsAggTrade]
```

| Field | Type | Description |
|-------|------|-------------|
| `e` | String | Event type ("aggTrade") |
| `s` | String | Symbol |
| `p` | String | Price |
| `q` | String | Quantity |
| `T` | Long | Trade time |
| `m` | Boolean | Is buyer the maker? |

### Individual Trade

Raw individual trade event.

```scala
ws.subscribe(TradeStream("ETHUSDT"))
// Stream[F, WsTrade]
```

### Kline / Candlestick

Kline/candlestick update pushed every second.

```scala
ws.subscribe(KlineStream("BTCUSDT", KlineInterval.`1h`))
// Stream[F, WsKline]
```

The kline data is nested under the `k` field:

```scala
stream.evalMap { event =>
  IO.println(s"Symbol: ${event.s}, Close: ${event.k.c}, Volume: ${event.k.v}")
}
```

### Mini Ticker

24hr rolling window mini-ticker for a single symbol.

```scala
ws.subscribe(MiniTickerStream("BTCUSDT"))
// Stream[F, WsMiniTicker]
```

### All Market Mini Tickers

Mini-ticker for all symbols, pushed every second.

```scala
ws.subscribe(AllMiniTickerStream())
// Stream[F, List[WsMiniTicker]]
```

### Individual Symbol Ticker

Full 24hr ticker for a single symbol.

```scala
ws.subscribe(TickerStream("BTCUSDT"))
// Stream[F, WsTicker]
```

### All Market Tickers

Full ticker for all symbols.

```scala
ws.subscribe(AllTickerStream())
// Stream[F, List[WsTicker]]
```

### Book Ticker

Best bid/ask update in real-time.

```scala
ws.subscribe(BookTickerStream("BTCUSDT"))
// Stream[F, WsBookTicker]
```

### Average Price

Average price for a symbol, updated every second.

```scala
ws.subscribe(AvgPriceStream("BTCUSDT"))
// Stream[F, WsAvgPrice]
```

### Partial Book Depth

Top bids and asks at specified depth level.

```scala
ws.subscribe(DepthStream("BTCUSDT", levels = 10, speed = "100ms"))
// Stream[F, WsDepthUpdate]
```

Levels: 5, 10, or 20. Speed: `1000ms` (default) or `100ms`.

### Diff Depth

Order book updates pushed in real-time.

```scala
ws.subscribe(DiffDepthStream("BTCUSDT", speed = "100ms"))
// Stream[F, WsDepthUpdate]
```

## Multi-Stream Subscription

Subscribe to multiple streams over a single connection:

```scala
ws.subscribeMulti(List(
  "btcusdt@trade",
  "ethusdt@trade",
  "bnbusdt@miniTicker"
))
// Stream[F, Json] — raw JSON, parse by stream name
```

## Composing Streams

Since WebSocket streams are `fs2.Stream`, you can compose them with full stream combinators:

```scala
val btcTrades = ws.subscribe(TradeStream("BTCUSDT"))
val ethTrades = ws.subscribe(TradeStream("ETHUSDT"))

// Merge two streams
val allTrades = btcTrades.merge(ethTrades)

// Take first 100 events then stop
allTrades.take(100).compile.toList

// Window into 10-second chunks
allTrades.groupWithin(100, 10.seconds)
```
