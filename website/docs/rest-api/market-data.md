---
sidebar_position: 2
---

# Market Data Endpoints

Real-time and historical market data. No authentication required for most endpoints.

> Binance API Reference: [Market Data Endpoints](https://developers.binance.com/docs/binance-spot-api-docs/rest-api/market-data-endpoints)

## Order Book (Depth)

Get the order book for a symbol.

```scala
val book: F[OrderBook] = client.depth("BTCUSDT", limit = Some(10))
// OrderBook(lastUpdateId, bids: List[OrderBookEntry], asks: List[OrderBookEntry])
```

| | |
|---|---|
| **Endpoint** | `GET /api/v3/depth` |
| **Auth** | None |
| **Weight** | 5-50 (depends on limit) |

## Recent Trades

Get the most recent trades for a symbol.

```scala
val trades: F[List[Trade]] = client.trades("BTCUSDT", limit = Some(50))
```

| | |
|---|---|
| **Endpoint** | `GET /api/v3/trades` |
| **Auth** | None |
| **Weight** | 25 |

## Aggregate Trades

Get compressed, aggregated trades.

```scala
val aggTrades: F[List[AggTrade]] = client.aggTrades("BTCUSDT", limit = Some(100))
```

| | |
|---|---|
| **Endpoint** | `GET /api/v3/aggTrades` |
| **Auth** | None |
| **Weight** | 4 |

## Kline / Candlestick Data

Get kline/candlestick bars for a symbol. Klines are uniquely identified by their open time.

```scala
import io.github.rafafrdz.binance4s.domain.KlineInterval

val klines: F[List[Kline]] = client.klines("BTCUSDT", KlineInterval.`1h`, limit = Some(100))
```

Available intervals: `1s`, `1m`, `3m`, `5m`, `15m`, `30m`, `1h`, `2h`, `4h`, `6h`, `8h`, `12h`, `1d`, `3d`, `1w`, `1M`

| | |
|---|---|
| **Endpoint** | `GET /api/v3/klines` |
| **Auth** | None |
| **Weight** | 2 |

**Response model:**

```scala
case class Kline(
  openTime: Long, open: String, high: String, low: String,
  close: String, volume: String, closeTime: Long,
  quoteAssetVolume: String, numberOfTrades: Int,
  takerBuyBaseAssetVolume: String, takerBuyQuoteAssetVolume: String
)
```

:::note
Klines are returned as JSON arrays, not objects. Binance4s includes a custom Circe decoder that maps positional array elements to named fields.
:::

## Current Average Price

Get the current average price for a symbol.

```scala
val avg: F[AvgPrice] = client.avgPrice("BTCUSDT")
// AvgPrice(mins: Int, price: String)
```

| | |
|---|---|
| **Endpoint** | `GET /api/v3/avgPrice` |
| **Auth** | None |
| **Weight** | 2 |

## 24hr Ticker

Get 24-hour rolling window price change statistics.

```scala
val tickers: F[List[Ticker24h]] = client.ticker24h(Some("BTCUSDT"))
```

| | |
|---|---|
| **Endpoint** | `GET /api/v3/ticker/24hr` |
| **Auth** | None |
| **Weight** | 2-80 |

## Symbol Price Ticker

Get the latest price for a symbol.

```scala
// Single symbol (returns object)
val price: F[TickerPrice] = client.tickerPrice("BTCUSDT")

// All symbols (returns array)
val prices: F[List[TickerPrice]] = client.tickerPrice
```

| | |
|---|---|
| **Endpoint** | `GET /api/v3/ticker/price` |
| **Auth** | None |
| **Weight** | 2-4 |

## Book Ticker

Get the best bid/ask price and quantity.

```scala
val tickers: F[List[BookTicker]] = client.bookTicker(Some("BTCUSDT"))
```

| | |
|---|---|
| **Endpoint** | `GET /api/v3/ticker/bookTicker` |
| **Auth** | None |
| **Weight** | 2-4 |

## UI Klines

Get kline/candlestick data optimized for presentation (modified by Binance for display purposes).

```scala
import io.github.rafafrdz.binance4s.api.market.UiKlinesReq

val uiKlines: F[List[Kline]] = client.execute(UiKlinesReq("BTCUSDT", KlineInterval.`1h`, limit = Some(100)))
```

| | |
|---|---|
| **Endpoint** | `GET /api/v3/uiKlines` |
| **Auth** | None |
| **Weight** | 2 |

## Trading Day Ticker

Get price change statistics for the trading day.

```scala
import io.github.rafafrdz.binance4s.api.market.TradingDayTickerReq

val ticker: F[List[TradingDayTicker]] = client.execute(TradingDayTickerReq(symbol = Some("BTCUSDT")))
```

| | |
|---|---|
| **Endpoint** | `GET /api/v3/ticker/tradingDay` |
| **Auth** | None |
| **Weight** | 4 |

## Rolling Window Ticker

Get rolling window price change statistics with configurable window size.

```scala
import io.github.rafafrdz.binance4s.api.market.RollingTickerReq

val ticker: F[List[RollingTicker]] = client.execute(RollingTickerReq(symbol = Some("BTCUSDT"), windowSize = Some("1h")))
```

| | |
|---|---|
| **Endpoint** | `GET /api/v3/ticker` |
| **Auth** | None |
| **Weight** | 4 |

## Reference Price

Get the reference price for a symbol.

```scala
val ref: F[ReferencePrice] = client.referencePrice("BTCUSDT")
```

| | |
|---|---|
| **Endpoint** | `GET /api/v3/referencePrice` |
| **Auth** | None |
| **Weight** | 2 |
