package io.github.rafafrdz.binance4s.http.client

import cats.effect.{Async, Resource}
import org.http4s.ember.client.EmberClientBuilder

import io.github.rafafrdz.binance4s.config.BinanceConfig
import io.github.rafafrdz.binance4s.endpoint.BinanceEndpoint

trait BinanceClient[F[_]]:
  def config: BinanceConfig
  def execute[Req, Resp](req: Req)(using ep: BinanceEndpoint[Req, Resp]): F[Resp]

object BinanceClient:
  def resource[F[_]: Async](cfg: BinanceConfig): Resource[F, BinanceClient[F]] =
    EmberClientBuilder.default[F].build.map(httpClient => BinanceHttpClient[F](cfg, httpClient))
