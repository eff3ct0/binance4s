---
sidebar_position: 3
---

# Error Handling

Binance4s uses a Scala 3 `enum` to represent all possible errors. Every error is typed, pattern-matchable, and extends `Exception` for compatibility with `F.raiseError`.

## The Error ADT

```scala
enum BinanceError(val message: String) extends Exception(message):
  case ApiError(code: Int, msg: String)            // Binance API error (e.g. -1121 Invalid symbol)
  case HttpError(status: Int, body: String)         // Non-2xx HTTP response
  case DecodingError(reason: String, rawJson: Json)  // JSON decoding failure
  case AuthenticationError(detail: String)           // Missing credentials
  case RateLimitError(retryAfter: Option[Long])      // HTTP 429
  case ConnectionError(cause: Throwable)             // Network failure
  case TimeoutError(endpoint: String)                // Request timeout
  case WebSocketError(reason: String)                // WebSocket frame error
```

## Handling Errors

Use `attempt` to catch errors as `Either`, then pattern match:

```scala
client.depth("INVALID_SYMBOL", Some(5)).attempt.flatMap {
  case Left(BinanceError.ApiError(code, msg)) =>
    IO.println(s"Binance error $code: $msg")

  case Left(BinanceError.RateLimitError(retryAfter)) =>
    IO.println(s"Rate limited! Retry after ${retryAfter}ms")

  case Left(BinanceError.HttpError(status, body)) =>
    IO.println(s"HTTP $status: $body")

  case Left(error) =>
    IO.println(s"Unexpected: ${error.message}")

  case Right(book) =>
    IO.println(s"Got ${book.bids.size} bids")
}
```

## API Error Codes

When Binance returns a JSON error response like `{"code": -1121, "msg": "Invalid symbol."}`, the client automatically parses it into `BinanceError.ApiError`.

Common Binance error codes:

| Code | Description |
|------|-------------|
| -1000 | Unknown error |
| -1002 | Unauthorized |
| -1003 | Too many requests (rate limit) |
| -1013 | Invalid quantity |
| -1021 | Timestamp outside recvWindow |
| -1100 | Illegal characters in parameter |
| -1121 | Invalid symbol |
| -2010 | Insufficient balance |
| -2011 | Order cancel rejected |

See the full list in the [Binance API documentation](https://developers.binance.com/docs/binance-spot-api-docs/errors).

## Automatic Signing Errors

If you call an authenticated endpoint without credentials, the HTTP request will proceed but Binance will return an API error. The client maps this to `BinanceError.ApiError`.

:::tip
Configure credentials at client creation time to avoid runtime authentication errors:
```scala
val config = BinanceConfig.builder
  .live
  .credentials("api-key", "secret-key")
  .build
```
:::
