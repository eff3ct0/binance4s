package io.github.rafafrdz.binance4s.error

import io.circe.{Codec, Json}

enum BinanceError(val message: String) extends Exception(message):
  case ApiError(code: Int, msg: String)
    extends BinanceError(s"Binance API error $code: $msg")
  case HttpError(status: Int, body: String)
    extends BinanceError(s"HTTP $status: $body")
  case DecodingError(reason: String, rawJson: Json)
    extends BinanceError(s"Failed to decode response: $reason")
  case AuthenticationError(detail: String)
    extends BinanceError(s"Authentication error: $detail")
  case RateLimitError(retryAfter: Option[Long])
    extends BinanceError(s"Rate limited. Retry after: ${retryAfter.getOrElse("unknown")}ms")
  case ConnectionError(cause: Throwable)
    extends BinanceError(s"Connection error: ${cause.getMessage}")
  case TimeoutError(endpoint: String)
    extends BinanceError(s"Request to $endpoint timed out")
  case WebSocketError(reason: String)
    extends BinanceError(s"WebSocket error: $reason")

case class BinanceApiErrorResponse(code: Int, msg: String) derives Codec.AsObject
