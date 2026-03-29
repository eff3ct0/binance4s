package io.github.rafafrdz.binance4s.query

trait QueryParamEncoder[A]:
  def encode(a: A): String

object QueryParamEncoder:
  def apply[A](using ev: QueryParamEncoder[A]): QueryParamEncoder[A] = ev

  given QueryParamEncoder[String]     = identity(_)
  given QueryParamEncoder[Int]        = _.toString
  given QueryParamEncoder[Long]       = _.toString
  given QueryParamEncoder[Double]     = _.toString
  given QueryParamEncoder[Boolean]    = _.toString
  given QueryParamEncoder[BigDecimal] = _.bigDecimal.toPlainString
