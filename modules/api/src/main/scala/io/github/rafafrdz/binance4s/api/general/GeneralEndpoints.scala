package io.github.rafafrdz.binance4s.api.general

import io.circe.{Decoder, Json}

import io.github.rafafrdz.binance4s.domain.{ApiPrefix, ApiVersion, SecurityType}
import io.github.rafafrdz.binance4s.domain.responses.{ExchangeInfo, ServerTime}
import io.github.rafafrdz.binance4s.endpoint.{BinanceEndpoint, HttpMethod}
import io.github.rafafrdz.binance4s.query.QueryString

// Request types
case object Ping
case object ServerTimeReq
case class ExchangeInfoReq(
  symbol: Option[String] = None,
  symbols: Option[List[String]] = None
)

// Endpoint instances
given BinanceEndpoint[Ping.type, Json] with
  def method                      = HttpMethod.GET
  def prefix                      = ApiPrefix.Api
  def version                     = ApiVersion.V3
  def path                        = Vector("ping")
  def security                    = SecurityType.None
  def queryParams(req: Ping.type) = QueryString.empty
  def decoder                     = Decoder[Json]

given BinanceEndpoint[ServerTimeReq.type, ServerTime] with
  def method                               = HttpMethod.GET
  def prefix                               = ApiPrefix.Api
  def version                              = ApiVersion.V3
  def path                                 = Vector("time")
  def security                             = SecurityType.None
  def queryParams(req: ServerTimeReq.type) = QueryString.empty
  def decoder                              = Decoder[ServerTime]

given BinanceEndpoint[ExchangeInfoReq, ExchangeInfo] with
  def method   = HttpMethod.GET
  def prefix   = ApiPrefix.Api
  def version  = ApiVersion.V3
  def path     = Vector("exchangeInfo")
  def security = SecurityType.None
  def queryParams(req: ExchangeInfoReq) =
    QueryString.empty
      .addOpt("symbol", req.symbol)
      .addOpt("symbols", req.symbols.map(_.mkString("[\"", "\",\"", "\"]")))
  def decoder = Decoder[ExchangeInfo]
