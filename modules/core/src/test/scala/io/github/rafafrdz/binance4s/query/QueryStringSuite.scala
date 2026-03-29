package io.github.rafafrdz.binance4s.query

class QueryStringSuite extends munit.FunSuite:

  test("empty QueryString renders as empty string") {
    assertEquals(QueryString.empty.render, "")
  }

  test("renders parameters in insertion order") {
    val qs = QueryString.empty
      .add("symbol", "BTCUSDT")
      .add("limit", 100)
    assertEquals(qs.render, "symbol=BTCUSDT&limit=100")
  }

  test("addOpt skips None values") {
    val qs = QueryString.empty
      .add("symbol", "BTCUSDT")
      .addOpt[Int]("limit", scala.None)
    assertEquals(qs.render, "symbol=BTCUSDT")
  }

  test("addOpt includes Some values") {
    val qs = QueryString.empty
      .addOpt("limit", Some(50))
    assertEquals(qs.render, "limit=50")
  }

  test("++ merges two QueryStrings preserving order") {
    val qs1 = QueryString.empty.add("a", "1")
    val qs2 = QueryString.empty.add("b", "2")
    assertEquals((qs1 ++ qs2).render, "a=1&b=2")
  }

  test("isEmpty returns true for empty") {
    assert(QueryString.empty.isEmpty)
  }

  test("isEmpty returns false for non-empty") {
    assert(QueryString.empty.add("k", "v").nonEmpty)
  }

  test("withSignature appends HMAC signature") {
    val qs = QueryString.empty
      .add("symbol", "BTCUSDT")
      .add("timestamp", 1234567890L)
      .withSignature("mysecret")
    val rendered = qs.render
    assert(rendered.contains("signature="))
    assert(rendered.startsWith("symbol=BTCUSDT&timestamp=1234567890&signature="))
  }

  test("BigDecimal encodes without scientific notation") {
    val qs = QueryString.empty.add("price", BigDecimal("0.00001234"))
    assertEquals(qs.render, "price=0.00001234")
  }
