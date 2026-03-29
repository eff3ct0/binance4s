package io.github.rafafrdz.binance4s.api.ws

import io.circe.Decoder

import io.github.rafafrdz.binance4s.domain.KlineInterval
import io.github.rafafrdz.binance4s.domain.responses.*
import io.github.rafafrdz.binance4s.endpoint.WebSocketStream

// Stream type definitions
case class AggTradeStream(symbol: String) extends WebSocketStream[WsAggTrade]:
  def streamName = s"${symbol.toLowerCase}@aggTrade"
  def decoder    = Decoder[WsAggTrade]

case class TradeStream(symbol: String) extends WebSocketStream[WsTrade]:
  def streamName = s"${symbol.toLowerCase}@trade"
  def decoder    = Decoder[WsTrade]

case class KlineStream(symbol: String, interval: KlineInterval) extends WebSocketStream[WsKline]:
  def streamName = s"${symbol.toLowerCase}@kline_${interval.value}"
  def decoder    = Decoder[WsKline]

case class MiniTickerStream(symbol: String) extends WebSocketStream[WsMiniTicker]:
  def streamName = s"${symbol.toLowerCase}@miniTicker"
  def decoder    = Decoder[WsMiniTicker]

case class AllMiniTickerStream() extends WebSocketStream[List[WsMiniTicker]]:
  def streamName = "!miniTicker@arr"
  def decoder    = Decoder[List[WsMiniTicker]]

case class TickerStream(symbol: String) extends WebSocketStream[WsTicker]:
  def streamName = s"${symbol.toLowerCase}@ticker"
  def decoder    = Decoder[WsTicker]

case class AllTickerStream() extends WebSocketStream[List[WsTicker]]:
  def streamName = "!ticker@arr"
  def decoder    = Decoder[List[WsTicker]]

case class BookTickerStream(symbol: String) extends WebSocketStream[WsBookTicker]:
  def streamName = s"${symbol.toLowerCase}@bookTicker"
  def decoder    = Decoder[WsBookTicker]

case class AvgPriceStream(symbol: String) extends WebSocketStream[WsAvgPrice]:
  def streamName = s"${symbol.toLowerCase}@avgPrice"
  def decoder    = Decoder[WsAvgPrice]

case class DepthStream(symbol: String, levels: Int = 20, speed: String = "1000ms") extends WebSocketStream[WsDepthUpdate]:
  def streamName = if levels > 0 then s"${symbol.toLowerCase}@depth${levels}@${speed}" else s"${symbol.toLowerCase}@depth@${speed}"
  def decoder    = Decoder[WsDepthUpdate]

case class DiffDepthStream(symbol: String, speed: String = "1000ms") extends WebSocketStream[WsDepthUpdate]:
  def streamName = s"${symbol.toLowerCase}@depth@${speed}"
  def decoder    = Decoder[WsDepthUpdate]

case class UserDataStream(listenKey: String) extends WebSocketStream[io.circe.Json]:
  def streamName = listenKey
  def decoder    = Decoder[io.circe.Json]
