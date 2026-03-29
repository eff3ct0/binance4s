package io.github.rafafrdz.binance4s.examples

import cats.effect.{IO, IOApp}
import cats.syntax.all.*

import io.github.rafafrdz.binance4s.config.BinanceConfig
import io.github.rafafrdz.binance4s.domain.{BinanceMode, KlineInterval}
import io.github.rafafrdz.binance4s.http.client.BinanceClient
import io.github.rafafrdz.binance4s.api.*

object MarketDataExample extends IOApp.Simple:

  val config = BinanceConfig(mode = BinanceMode.Live)

  def run: IO[Unit] =
    BinanceClient.resource[IO](config).use { client =>
      for
        _     <- IO.println("=== Market Data Example ===")
        book  <- client.depth("BTCUSDT", limit = Some(5))
        _     <- IO.println(s"Best bid: ${book.bids.headOption.map(_.price)}")
        _     <- IO.println(s"Best ask: ${book.asks.headOption.map(_.price)}")
        avg   <- client.avgPrice("BTCUSDT")
        _     <- IO.println(s"Average price: ${avg.price}")
        kl    <- client.klines("ETHUSDT", KlineInterval.`1h`, limit = Some(3))
        _     <- IO.println(s"Last ${kl.size} hourly klines for ETHUSDT")
        _     <- kl.traverse_(k => IO.println(s"  Open: ${k.open}, Close: ${k.close}, Volume: ${k.volume}"))
        price <- client.tickerPrice("BTCUSDT")
        _     <- IO.println(s"BTC price: ${price.price}")
      yield ()
    }
