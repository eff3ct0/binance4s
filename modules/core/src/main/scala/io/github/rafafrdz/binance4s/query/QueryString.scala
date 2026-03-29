package io.github.rafafrdz.binance4s.query

import io.github.rafafrdz.binance4s.auth.HmacSha256

opaque type QueryString = Vector[(String, String)]

object QueryString:
  val empty: QueryString = Vector.empty

  def apply(pairs: (String, String)*): QueryString = pairs.toVector

  extension (qs: QueryString)
    def add[A: QueryParamEncoder](key: String, value: A): QueryString =
      qs :+ (key -> QueryParamEncoder[A].encode(value))

    def addOpt[A: QueryParamEncoder](key: String, value: Option[A]): QueryString =
      value.fold(qs)(v => qs.add(key, v))

    def ++(other: QueryString): QueryString =
      val qsv: Vector[(String, String)]    = qs
      val otherv: Vector[(String, String)]  = other
      qsv ++ otherv

    def render: String =
      val v: Vector[(String, String)] = qs
      v.map((k, v) => s"$k=$v").mkString("&")

    def isEmpty: Boolean =
      val v: Vector[(String, String)] = qs
      v.isEmpty

    def nonEmpty: Boolean = !qs.isEmpty

    def withTimestamp: QueryString =
      qs.add("timestamp", System.currentTimeMillis())

    def withRecvWindow(ms: Long): QueryString =
      qs.add("recvWindow", ms)

    def withSignature(secretKey: String): QueryString =
      val payload = qs.render
      val sig     = HmacSha256.sign(payload, secretKey)
      qs.add("signature", sig)

    def toVector: Vector[(String, String)] = qs
