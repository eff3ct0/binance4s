package io.github.rafafrdz.binance4s.domain.responses

import io.circe.{Codec, Decoder, Encoder, Json}

case class OrderBookEntry(price: String, qty: String)

object OrderBookEntry:
  given Decoder[OrderBookEntry] = Decoder.instance { c =>
    for
      price <- c.downN(0).as[String]
      qty   <- c.downN(1).as[String]
    yield OrderBookEntry(price, qty)
  }
  given Encoder[OrderBookEntry] = Encoder.instance(e => Json.arr(Json.fromString(e.price), Json.fromString(e.qty)))

case class OrderBook(
  lastUpdateId: Long,
  bids: List[OrderBookEntry],
  asks: List[OrderBookEntry]
) derives Codec.AsObject

case class Trade(
  id: Long,
  price: String,
  qty: String,
  quoteQty: String,
  time: Long,
  isBuyerMaker: Boolean,
  isBestMatch: Boolean
) derives Codec.AsObject

case class AggTrade(
  a: Long,
  p: String,
  q: String,
  f: Long,
  l: Long,
  T: Long,
  m: Boolean,
  M: Boolean
) derives Codec.AsObject

case class Kline(
  openTime: Long,
  open: String,
  high: String,
  low: String,
  close: String,
  volume: String,
  closeTime: Long,
  quoteAssetVolume: String,
  numberOfTrades: Int,
  takerBuyBaseAssetVolume: String,
  takerBuyQuoteAssetVolume: String
)

object Kline:
  given Decoder[Kline] = Decoder.instance { c =>
    for
      openTime                 <- c.downN(0).as[Long]
      open                     <- c.downN(1).as[String]
      high                     <- c.downN(2).as[String]
      low                      <- c.downN(3).as[String]
      close                    <- c.downN(4).as[String]
      volume                   <- c.downN(5).as[String]
      closeTime                <- c.downN(6).as[Long]
      quoteAssetVolume         <- c.downN(7).as[String]
      numberOfTrades           <- c.downN(8).as[Int]
      takerBuyBaseAssetVolume  <- c.downN(9).as[String]
      takerBuyQuoteAssetVolume <- c.downN(10).as[String]
    yield Kline(
      openTime,
      open,
      high,
      low,
      close,
      volume,
      closeTime,
      quoteAssetVolume,
      numberOfTrades,
      takerBuyBaseAssetVolume,
      takerBuyQuoteAssetVolume
    )
  }
  given Encoder[Kline] = Encoder.instance { k =>
    Json.arr(
      Json.fromLong(k.openTime),
      Json.fromString(k.open),
      Json.fromString(k.high),
      Json.fromString(k.low),
      Json.fromString(k.close),
      Json.fromString(k.volume),
      Json.fromLong(k.closeTime),
      Json.fromString(k.quoteAssetVolume),
      Json.fromInt(k.numberOfTrades),
      Json.fromString(k.takerBuyBaseAssetVolume),
      Json.fromString(k.takerBuyQuoteAssetVolume)
    )
  }

case class AvgPrice(mins: Int, price: String, closeTime: Option[Long] = None) derives Codec.AsObject

case class TickerPrice(symbol: String, price: String) derives Codec.AsObject

case class BookTicker(
  symbol: String,
  bidPrice: String,
  bidQty: String,
  askPrice: String,
  askQty: String
) derives Codec.AsObject

case class Ticker24h(
  symbol: String,
  priceChange: String,
  priceChangePercent: String,
  weightedAvgPrice: String,
  prevClosePrice: String,
  lastPrice: String,
  lastQty: String,
  bidPrice: String,
  bidQty: String,
  askPrice: String,
  askQty: String,
  openPrice: String,
  highPrice: String,
  lowPrice: String,
  volume: String,
  quoteVolume: String,
  openTime: Long,
  closeTime: Long,
  firstId: Long,
  lastId: Long,
  count: Int
) derives Codec.AsObject

case class RollingTicker(
  symbol: String,
  priceChange: String,
  priceChangePercent: String,
  weightedAvgPrice: String,
  openPrice: String,
  highPrice: String,
  lowPrice: String,
  lastPrice: String,
  volume: String,
  quoteVolume: String,
  openTime: Long,
  closeTime: Long,
  firstId: Long,
  lastId: Long,
  count: Int
) derives Codec.AsObject

case class TradingDayTicker(
  symbol: String,
  priceChange: String,
  priceChangePercent: String,
  weightedAvgPrice: String,
  openPrice: String,
  highPrice: String,
  lowPrice: String,
  lastPrice: String,
  volume: String,
  quoteVolume: String,
  openTime: Long,
  closeTime: Long,
  firstId: Long,
  lastId: Long,
  count: Int
) derives Codec.AsObject

case class ReferencePrice(symbol: String, referencePrice: String) derives Codec.AsObject
