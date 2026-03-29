package io.github.rafafrdz.binance4s.endpoint

import io.circe.Decoder

import io.github.rafafrdz.binance4s.domain.{ApiPrefix, ApiVersion, SecurityType}
import io.github.rafafrdz.binance4s.query.QueryString

enum HttpMethod:
  case GET, POST, PUT, DELETE

trait BinanceEndpoint[Req, Resp]:
  def method: HttpMethod
  def prefix: ApiPrefix
  def version: ApiVersion
  def path: Vector[String]
  def security: SecurityType
  def queryParams(req: Req): QueryString
  def decoder: Decoder[Resp]
  def weight: Int = 1

  final def pathSegments: Vector[String] =
    Vector(prefix.value, version.value) ++ path
