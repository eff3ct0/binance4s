package io.github.rafafrdz.binance4s.api.market

import io.circe.Decoder

import io.github.rafafrdz.binance4s.domain.{ApiPrefix, ApiVersion, KlineInterval, SecurityType}
import io.github.rafafrdz.binance4s.domain.responses.*
import io.github.rafafrdz.binance4s.endpoint.{BinanceEndpoint, HttpMethod}
import io.github.rafafrdz.binance4s.query.QueryString

// Request types
case class DepthReq(symbol: String, limit: Option[Int] = None)
case class TradesReq(symbol: String, limit: Option[Int] = None)
case class HistoricalTradesReq(symbol: String, limit: Option[Int] = None, fromId: Option[Long] = None)
case class AggTradesReq(
  symbol: String,
  fromId: Option[Long] = None,
  startTime: Option[Long] = None,
  endTime: Option[Long] = None,
  limit: Option[Int] = None
)
case class KlinesReq(
  symbol: String,
  interval: KlineInterval,
  startTime: Option[Long] = None,
  endTime: Option[Long] = None,
  limit: Option[Int] = None
)
case class UiKlinesReq(
  symbol: String,
  interval: KlineInterval,
  startTime: Option[Long] = None,
  endTime: Option[Long] = None,
  limit: Option[Int] = None
)
case class AvgPriceReq(symbol: String)
case class Ticker24hReq(symbol: Option[String] = None, symbols: Option[List[String]] = None)
case class TradingDayTickerReq(symbol: Option[String] = None, symbols: Option[List[String]] = None)
case class TickerPriceReq(symbol: Option[String] = None, symbols: Option[List[String]] = None)
case class BookTickerReq(symbol: Option[String] = None, symbols: Option[List[String]] = None)
case class RollingTickerReq(
  symbol: Option[String] = None,
  symbols: Option[List[String]] = None,
  windowSize: Option[String] = None
)
case class ReferencePriceReq(symbol: String)
case class SingleTickerPriceReq(symbol: String)

// Endpoint instances
given BinanceEndpoint[DepthReq, OrderBook] with
  def method                     = HttpMethod.GET
  def prefix                     = ApiPrefix.Api
  def version                    = ApiVersion.V3
  def path                       = Vector("depth")
  def security                   = SecurityType.None
  def queryParams(req: DepthReq) = QueryString.empty.add("symbol", req.symbol).addOpt("limit", req.limit)
  def decoder                    = Decoder[OrderBook]
  override def weight            = 5

given BinanceEndpoint[TradesReq, List[Trade]] with
  def method                      = HttpMethod.GET
  def prefix                      = ApiPrefix.Api
  def version                     = ApiVersion.V3
  def path                        = Vector("trades")
  def security                    = SecurityType.None
  def queryParams(req: TradesReq) = QueryString.empty.add("symbol", req.symbol).addOpt("limit", req.limit)
  def decoder                     = Decoder[List[Trade]]
  override def weight             = 25

given BinanceEndpoint[HistoricalTradesReq, List[Trade]] with
  def method   = HttpMethod.GET
  def prefix   = ApiPrefix.Api
  def version  = ApiVersion.V3
  def path     = Vector("historicalTrades")
  def security = SecurityType.MarketData
  def queryParams(req: HistoricalTradesReq) =
    QueryString.empty.add("symbol", req.symbol).addOpt("limit", req.limit).addOpt("fromId", req.fromId)
  def decoder         = Decoder[List[Trade]]
  override def weight = 25

given BinanceEndpoint[AggTradesReq, List[AggTrade]] with
  def method   = HttpMethod.GET
  def prefix   = ApiPrefix.Api
  def version  = ApiVersion.V3
  def path     = Vector("aggTrades")
  def security = SecurityType.None
  def queryParams(req: AggTradesReq) =
    QueryString.empty
      .add("symbol", req.symbol)
      .addOpt("fromId", req.fromId)
      .addOpt("startTime", req.startTime)
      .addOpt("endTime", req.endTime)
      .addOpt("limit", req.limit)
  def decoder = Decoder[List[AggTrade]]

given BinanceEndpoint[KlinesReq, List[Kline]] with
  def method   = HttpMethod.GET
  def prefix   = ApiPrefix.Api
  def version  = ApiVersion.V3
  def path     = Vector("klines")
  def security = SecurityType.None
  def queryParams(req: KlinesReq) =
    QueryString.empty
      .add("symbol", req.symbol)
      .add("interval", req.interval.value)
      .addOpt("startTime", req.startTime)
      .addOpt("endTime", req.endTime)
      .addOpt("limit", req.limit)
  def decoder = Decoder[List[Kline]]

given BinanceEndpoint[UiKlinesReq, List[Kline]] with
  def method   = HttpMethod.GET
  def prefix   = ApiPrefix.Api
  def version  = ApiVersion.V3
  def path     = Vector("uiKlines")
  def security = SecurityType.None
  def queryParams(req: UiKlinesReq) =
    QueryString.empty
      .add("symbol", req.symbol)
      .add("interval", req.interval.value)
      .addOpt("startTime", req.startTime)
      .addOpt("endTime", req.endTime)
      .addOpt("limit", req.limit)
  def decoder = Decoder[List[Kline]]

given BinanceEndpoint[AvgPriceReq, AvgPrice] with
  def method                        = HttpMethod.GET
  def prefix                        = ApiPrefix.Api
  def version                       = ApiVersion.V3
  def path                          = Vector("avgPrice")
  def security                      = SecurityType.None
  def queryParams(req: AvgPriceReq) = QueryString.empty.add("symbol", req.symbol)
  def decoder                       = Decoder[AvgPrice]

given BinanceEndpoint[Ticker24hReq, List[Ticker24h]] with
  def method   = HttpMethod.GET
  def prefix   = ApiPrefix.Api
  def version  = ApiVersion.V3
  def path     = Vector("ticker", "24hr")
  def security = SecurityType.None
  def queryParams(req: Ticker24hReq) =
    QueryString.empty
      .addOpt("symbol", req.symbol)
      .addOpt("symbols", req.symbols.map(_.mkString("[\"", "\",\"", "\"]")))
  def decoder = Decoder[List[Ticker24h]]

given BinanceEndpoint[TradingDayTickerReq, List[TradingDayTicker]] with
  def method   = HttpMethod.GET
  def prefix   = ApiPrefix.Api
  def version  = ApiVersion.V3
  def path     = Vector("ticker", "tradingDay")
  def security = SecurityType.None
  def queryParams(req: TradingDayTickerReq) =
    QueryString.empty
      .addOpt("symbol", req.symbol)
      .addOpt("symbols", req.symbols.map(_.mkString("[\"", "\",\"", "\"]")))
  def decoder = Decoder[List[TradingDayTicker]]

given BinanceEndpoint[TickerPriceReq, List[TickerPrice]] with
  def method   = HttpMethod.GET
  def prefix   = ApiPrefix.Api
  def version  = ApiVersion.V3
  def path     = Vector("ticker", "price")
  def security = SecurityType.None
  def queryParams(req: TickerPriceReq) =
    QueryString.empty
      .addOpt("symbol", req.symbol)
      .addOpt("symbols", req.symbols.map(_.mkString("[\"", "\",\"", "\"]")))
  def decoder = Decoder[List[TickerPrice]]

given BinanceEndpoint[BookTickerReq, List[BookTicker]] with
  def method   = HttpMethod.GET
  def prefix   = ApiPrefix.Api
  def version  = ApiVersion.V3
  def path     = Vector("ticker", "bookTicker")
  def security = SecurityType.None
  def queryParams(req: BookTickerReq) =
    QueryString.empty
      .addOpt("symbol", req.symbol)
      .addOpt("symbols", req.symbols.map(_.mkString("[\"", "\",\"", "\"]")))
  def decoder = Decoder[List[BookTicker]]

given BinanceEndpoint[RollingTickerReq, List[RollingTicker]] with
  def method   = HttpMethod.GET
  def prefix   = ApiPrefix.Api
  def version  = ApiVersion.V3
  def path     = Vector("ticker")
  def security = SecurityType.None
  def queryParams(req: RollingTickerReq) =
    QueryString.empty
      .addOpt("symbol", req.symbol)
      .addOpt("symbols", req.symbols.map(_.mkString("[\"", "\",\"", "\"]")))
      .addOpt("windowSize", req.windowSize)
  def decoder = Decoder[List[RollingTicker]]

given BinanceEndpoint[ReferencePriceReq, ReferencePrice] with
  def method                              = HttpMethod.GET
  def prefix                              = ApiPrefix.Api
  def version                             = ApiVersion.V3
  def path                                = Vector("referencePrice")
  def security                            = SecurityType.None
  def queryParams(req: ReferencePriceReq) = QueryString.empty.add("symbol", req.symbol)
  def decoder                             = Decoder[ReferencePrice]

given BinanceEndpoint[SingleTickerPriceReq, TickerPrice] with
  def method                                 = HttpMethod.GET
  def prefix                                 = ApiPrefix.Api
  def version                                = ApiVersion.V3
  def path                                   = Vector("ticker", "price")
  def security                               = SecurityType.None
  def queryParams(req: SingleTickerPriceReq) = QueryString.empty.add("symbol", req.symbol)
  def decoder                                = Decoder[TickerPrice]
