package io.github.rafafrdz.binance4s.examples

import cats.effect.{IO, IOApp}

import io.github.rafafrdz.binance4s.config.BinanceConfig
import io.github.rafafrdz.binance4s.domain.BinanceMode
import io.github.rafafrdz.binance4s.http.client.BinanceClient
import io.github.rafafrdz.binance4s.api.*

object GeneralExample extends IOApp.Simple:

  val config = BinanceConfig(mode = BinanceMode.Live)

  def run: IO[Unit] =
    BinanceClient.resource[IO](config).use { client =>
      for
        _    <- IO.println("=== Binance4s General Example ===")
        pong <- client.ping
        _    <- IO.println(s"Ping: $pong")
        time <- client.serverTime
        _    <- IO.println(s"Server time: ${time.serverTime}")
        info <- client.exchangeInfo("BTCUSDT")
        _    <- IO.println(s"Exchange info symbols: ${info.symbols.map(_.symbol).take(5)}")
      yield ()
    }
