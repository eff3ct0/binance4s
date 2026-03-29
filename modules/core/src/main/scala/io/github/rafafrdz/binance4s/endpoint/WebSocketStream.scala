package io.github.rafafrdz.binance4s.endpoint

import io.circe.Decoder

trait WebSocketStream[A]:
  def streamName: String
  def decoder: Decoder[A]
