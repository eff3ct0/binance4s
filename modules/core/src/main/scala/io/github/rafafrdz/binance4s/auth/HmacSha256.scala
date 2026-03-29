package io.github.rafafrdz.binance4s.auth

import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

import cats.effect.Sync

object HmacSha256:

  private val Algorithm = "HmacSHA256"

  def sign(data: String, secret: String): String =
    val mac = Mac.getInstance(Algorithm)
    mac.init(new SecretKeySpec(secret.getBytes("UTF-8"), Algorithm))
    mac.doFinal(data.getBytes("UTF-8")).map("%02x".format(_)).mkString

  def signF[F[_]: Sync](data: String, secret: String): F[String] =
    Sync[F].delay(sign(data, secret))
