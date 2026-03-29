package io.github.rafafrdz.binance4s.codec

import io.circe.parser.decode

import io.github.rafafrdz.binance4s.domain.responses.*
import io.github.rafafrdz.binance4s.error.BinanceApiErrorResponse

class ResponseCodecSuite extends munit.FunSuite:

  test("ServerTime decodes correctly") {
    val json   = """{"serverTime":1499827319559}"""
    val result = decode[ServerTime](json)
    assertEquals(result, Right(ServerTime(1499827319559L)))
  }

  test("BinanceApiErrorResponse decodes API errors") {
    val json   = """{"code":-1121,"msg":"Invalid symbol."}"""
    val result = decode[BinanceApiErrorResponse](json)
    assertEquals(result, Right(BinanceApiErrorResponse(-1121, "Invalid symbol.")))
  }

  test("TickerPrice decodes correctly") {
    val json   = """{"symbol":"BTCUSDT","price":"50000.00"}"""
    val result = decode[TickerPrice](json)
    assertEquals(result, Right(TickerPrice("BTCUSDT", "50000.00")))
  }

  test("AvgPrice decodes correctly") {
    val json   = """{"mins":5,"price":"50000.00"}"""
    val result = decode[AvgPrice](json)
    assertEquals(result, Right(AvgPrice(5, "50000.00", scala.None)))
  }

  test("OrderBookEntry decodes from JSON array format") {
    val json   = """["0.01234","10.5"]"""
    val result = decode[OrderBookEntry](json)
    assertEquals(result, Right(OrderBookEntry("0.01234", "10.5")))
  }

  test("OrderBook decodes correctly") {
    val json =
      """{
        |  "lastUpdateId": 1027024,
        |  "bids": [["0.01234","10.5"]],
        |  "asks": [["0.01235","20.0"]]
        |}""".stripMargin
    val result = decode[OrderBook](json)
    assert(result.isRight)
    val book = result.toOption.get
    assertEquals(book.lastUpdateId, 1027024L)
    assertEquals(book.bids.head, OrderBookEntry("0.01234", "10.5"))
    assertEquals(book.asks.head, OrderBookEntry("0.01235", "20.0"))
  }

  test("Kline decodes from JSON array format") {
    val json =
      """[1499040000000,"0.01634","0.80000","0.01575","0.01577","148976.11427815",1499644799999,"2434.19055334",308,"1756.87402397","28.46694368"]"""
    val result = decode[Kline](json)
    assert(result.isRight)
    val kline = result.toOption.get
    assertEquals(kline.openTime, 1499040000000L)
    assertEquals(kline.open, "0.01634")
    assertEquals(kline.numberOfTrades, 308)
  }

  test("SystemStatus decodes correctly") {
    val json   = """{"status":0,"msg":"normal"}"""
    val result = decode[SystemStatus](json)
    assertEquals(result, Right(SystemStatus(0, "normal")))
  }

  test("ListenKey decodes correctly") {
    val json   = """{"listenKey":"pqia91ma19a5s61cv6a81va65sdf19v8a65a1a5s61cv6a81va65sdf19v8a65a1"}"""
    val result = decode[ListenKey](json)
    assert(result.isRight)
  }
