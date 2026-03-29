package io.github.rafafrdz.binance4s.auth

class HmacSha256Suite extends munit.FunSuite:

  // Known test vector from Binance API documentation
  test("HMAC-SHA256 produces correct signature for Binance example") {
    val secret =
      "NhqPtmdSJYdKjVHjA7PZj4Mge3R5YNiP1e3UZjInClVN65XAbvqqM6A7H5fATj0j"
    val data =
      "symbol=LTCBTC&side=BUY&type=LIMIT&timeInForce=GTC&quantity=1&price=0.1&recvWindow=5000&timestamp=1499827319559"
    val expected =
      "c8db56825ae71d6d79447849e617115f4a920fa2acdcab2b053c4b2838bd6b71"
    assertEquals(HmacSha256.sign(data, secret), expected)
  }

  test("empty data produces valid signature") {
    val result = HmacSha256.sign("", "secret")
    assert(result.nonEmpty)
    assert(result.forall(c => c.isDigit || ('a' to 'f').contains(c)))
  }

  test("different secrets produce different signatures") {
    val sig1 = HmacSha256.sign("data", "secret1")
    val sig2 = HmacSha256.sign("data", "secret2")
    assertNotEquals(sig1, sig2)
  }
