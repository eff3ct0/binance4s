package io.github.rafafrdz.binance4s.ws.client

import cats.effect.{Async, Resource}

import org.http4s.Uri
import org.http4s.client.websocket.*
import org.http4s.jdkhttpclient.JdkWSClient

import io.circe.Json

import fs2.Stream

import io.github.rafafrdz.binance4s.config.BinanceConfig
import io.github.rafafrdz.binance4s.endpoint.WebSocketStream
import io.github.rafafrdz.binance4s.error.BinanceError

trait BinanceWsClient[F[_]]:
  def subscribe[A](stream: WebSocketStream[A]): Stream[F, A]
  def subscribeMulti(streams: List[String]): Stream[F, Json]

object BinanceWsClient:

  def resource[F[_]: Async](config: BinanceConfig): Resource[F, BinanceWsClient[F]] =
    Resource.eval(JdkWSClient.simple[F]).map { wsClient =>
      new BinanceWsClient[F]:
        private val baseUri = s"${config.mode.wsUri}/ws/"

        def subscribe[A](stream: WebSocketStream[A]): Stream[F, A] =
          val uri = Uri.unsafeFromString(s"$baseUri${stream.streamName}")
          Stream.resource(wsClient.connectHighLevel(WSRequest(uri))).flatMap { conn =>
            conn.receiveStream
              .collect { case WSFrame.Text(text, _) => text }
              .evalMap { text =>
                Async[F].fromEither(
                  io.circe.parser
                    .decode[A](text)(using stream.decoder)
                    .left
                    .map(e => BinanceError.WebSocketError(e.getMessage))
                )
              }
          }

        def subscribeMulti(streams: List[String]): Stream[F, Json] =
          val combined = streams.mkString("/")
          val uri      = Uri.unsafeFromString(s"${config.mode.wsUri}/stream?streams=$combined")
          Stream.resource(wsClient.connectHighLevel(WSRequest(uri))).flatMap { conn =>
            conn.receiveStream
              .collect { case WSFrame.Text(text, _) => text }
              .evalMap { text =>
                Async[F].fromEither(
                  io.circe.parser.parse(text).left.map(e => BinanceError.WebSocketError(e.getMessage))
                )
              }
          }
    }
