package io.github.rafafrdz.binance4s.domain.responses

import io.circe.{Codec, Decoder, Encoder}
import io.circe.generic.semiauto.{deriveDecoder, deriveEncoder}

case class WsAggTrade(
  e: String,
  E: Long,
  s: String,
  a: Long,
  p: String,
  q: String,
  f: Long,
  l: Long,
  T: Long,
  m: Boolean,
  M: Boolean
) derives Codec.AsObject

case class WsTrade(
  e: String,
  E: Long,
  s: String,
  t: Long,
  p: String,
  q: String,
  T: Long,
  m: Boolean,
  M: Boolean
) derives Codec.AsObject

case class WsKlineData(
  t: Long,
  T: Long,
  s: String,
  i: String,
  f: Long,
  L: Long,
  o: String,
  c: String,
  h: String,
  l: String,
  v: String,
  n: Int,
  x: Boolean,
  q: String,
  V: String,
  Q: String
) derives Codec.AsObject

case class WsKline(
  e: String,
  E: Long,
  s: String,
  k: WsKlineData
) derives Codec.AsObject

case class WsMiniTicker(
  e: String,
  E: Long,
  s: String,
  c: String,
  o: String,
  h: String,
  l: String,
  v: String,
  q: String
) derives Codec.AsObject

case class WsTicker(
  e: String,
  E: Long,
  s: String,
  p: String,
  P: String,
  w: String,
  x: String,
  c: String,
  Q: String,
  b: String,
  B: String,
  a: String,
  A: String,
  o: String,
  h: String,
  l: String,
  v: String,
  q: String,
  O: Long,
  C: Long,
  F: Long,
  L: Long,
  n: Int
) derives Codec.AsObject

case class WsBookTicker(
  u: Long,
  s: String,
  b: String,
  B: String,
  a: String,
  A: String
) derives Codec.AsObject

case class WsDepthUpdate(
  e: String,
  E: Long,
  s: String,
  U: Long,
  u: Long,
  b: List[OrderBookEntry],
  a: List[OrderBookEntry]
) derives Codec.AsObject

case class WsAvgPrice(
  e: String,
  E: Long,
  s: String,
  i: String,
  w: String,
  T: Long
) derives Codec.AsObject

// User Data Stream events
case class WsAccountUpdate(
  e: String,
  E: Long,
  u: Long,
  B: List[WsBalanceUpdate]
) derives Codec.AsObject

case class WsBalanceUpdate(
  a: String,
  f: String,
  l: String
) derives Codec.AsObject

case class WsBalanceUpdateEvent(
  e: String,
  E: Long,
  a: String,
  d: String,
  T: Long
) derives Codec.AsObject

case class WsExecutionReport(
  e: String,
  E: Long,
  s: String,
  c: String,
  S: String,
  o: String,
  f: String,
  q: String,
  p: String,
  P: String,
  F: String,
  g: Long,
  C: String,
  x: String,
  X: String,
  r: String,
  i: Long,
  l: String,
  z: String,
  L: String,
  n: String,
  N: Option[String],
  T: Long,
  t: Long,
  w: Boolean,
  m: Boolean,
  O: Long,
  Z: String,
  Y: String,
  Q: String
)

object WsExecutionReport:
  given Decoder[WsExecutionReport]          = deriveDecoder
  given Encoder.AsObject[WsExecutionReport] = deriveEncoder

case class WsListStatus(
  e: String,
  E: Long,
  s: String,
  g: Long,
  c: String,
  l: String,
  L: String,
  r: String,
  C: String,
  T: Long,
  O: List[WsListOrder]
) derives Codec.AsObject

case class WsListOrder(
  s: String,
  i: Long,
  c: String
) derives Codec.AsObject
