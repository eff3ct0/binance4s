package io.github.rafafrdz.binance4s.examples

import cats.effect.{IO, IOApp}

import io.github.rafafrdz.binance4s.api.ws.*
import io.github.rafafrdz.binance4s.config.BinanceConfig
import io.github.rafafrdz.binance4s.domain.{BinanceMode, KlineInterval}
import io.github.rafafrdz.binance4s.ws.client.BinanceWsClient

object WebSocketExample extends IOApp.Simple:

  val config = BinanceConfig(mode = BinanceMode.Live)

  def run: IO[Unit] =
    BinanceWsClient.resource[IO](config).use { ws =>
      IO.println("=== WebSocket Kline Stream (BTCUSDT 1m) ===") *>
        ws.subscribe(KlineStream("BTCUSDT", KlineInterval.`1m`))
          .take(5)
          .evalMap(k => IO.println(s"Kline close: ${k.k.c}, volume: ${k.k.v}"))
          .compile
          .drain
    }
