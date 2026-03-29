package io.github.rafafrdz.binance4s.api.userdata

import io.circe.{Decoder, Json}

import io.github.rafafrdz.binance4s.domain.{ApiPrefix, ApiVersion, SecurityType}
import io.github.rafafrdz.binance4s.domain.responses.ListenKey
import io.github.rafafrdz.binance4s.endpoint.{BinanceEndpoint, HttpMethod}
import io.github.rafafrdz.binance4s.query.QueryString

// Request types
case object CreateListenKeyReq
case class KeepAliveListenKeyReq(listenKey: String)
case class CloseListenKeyReq(listenKey: String)

// Endpoint instances
given BinanceEndpoint[CreateListenKeyReq.type, ListenKey] with
  def method   = HttpMethod.POST
  def prefix   = ApiPrefix.Api
  def version  = ApiVersion.V3
  def path     = Vector("userDataStream")
  def security = SecurityType.UserStream
  def queryParams(req: CreateListenKeyReq.type) = QueryString.empty
  def decoder  = Decoder[ListenKey]

given BinanceEndpoint[KeepAliveListenKeyReq, Json] with
  def method   = HttpMethod.PUT
  def prefix   = ApiPrefix.Api
  def version  = ApiVersion.V3
  def path     = Vector("userDataStream")
  def security = SecurityType.UserStream
  def queryParams(req: KeepAliveListenKeyReq) = QueryString.empty.add("listenKey", req.listenKey)
  def decoder  = Decoder[Json]

given BinanceEndpoint[CloseListenKeyReq, Json] with
  def method   = HttpMethod.DELETE
  def prefix   = ApiPrefix.Api
  def version  = ApiVersion.V3
  def path     = Vector("userDataStream")
  def security = SecurityType.UserStream
  def queryParams(req: CloseListenKeyReq) = QueryString.empty.add("listenKey", req.listenKey)
  def decoder  = Decoder[Json]
