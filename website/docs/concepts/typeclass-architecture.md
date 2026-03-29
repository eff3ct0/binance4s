---
sidebar_position: 1
---

# Typeclass Architecture

The core abstraction in Binance4s is the `BinanceEndpoint[Req, Resp]` typeclass. It statically links a request type to its response type, HTTP method, path, security model, and query parameters.

## The Typeclass

```scala
trait BinanceEndpoint[Req, Resp]:
  def method: HttpMethod          // GET, POST, PUT, DELETE
  def prefix: ApiPrefix           // Api (/api) or Sapi (/sapi)
  def version: ApiVersion         // V1, V2, V3
  def path: Vector[String]        // e.g. Vector("ticker", "price")
  def security: SecurityType      // None, UserStream, MarketData, Trade, UserData
  def queryParams(req: Req): QueryString   // builds the query string from the request
  def decoder: Decoder[Resp]      // Circe decoder for the response
  def weight: Int = 1             // API weight for rate limiting
```

## Defining an Endpoint

Each Binance API endpoint is defined as a `given` instance:

```scala
case class AvgPriceReq(symbol: String)

given BinanceEndpoint[AvgPriceReq, AvgPrice] with
  def method   = HttpMethod.GET
  def prefix   = ApiPrefix.Api
  def version  = ApiVersion.V3
  def path     = Vector("avgPrice")
  def security = SecurityType.None
  def queryParams(req: AvgPriceReq) =
    QueryString.empty.add("symbol", req.symbol)
  def decoder  = Decoder[AvgPrice]
```

This generates `GET /api/v3/avgPrice?symbol=BTCUSDT` and decodes the response as `AvgPrice`.

## How It Works

When you call `client.execute(AvgPriceReq("BTCUSDT"))`, the compiler:

1. **Resolves** the `given BinanceEndpoint[AvgPriceReq, AvgPrice]` instance at compile time
2. **Infers** the return type as `F[AvgPrice]`
3. At runtime, the client:
   - Calls `queryParams(req)` to build the query string
   - Adds timestamp + HMAC signature if `security` requires it
   - Constructs the full URI from `prefix`, `version`, `path`, and query
   - Executes the HTTP request with appropriate headers
   - Decodes the JSON response using `decoder`

## Two API Styles

```scala
// Explicit — you see the request type and the compiler resolves the response
val avgPrice: F[AvgPrice] = client.execute(AvgPriceReq("BTCUSDT"))

// Syntax extension — ergonomic shorthand
val avgPrice: F[AvgPrice] = client.avgPrice("BTCUSDT")
```

Both compile to the same code. The syntax extensions are defined as:

```scala
extension [F[_]](client: BinanceClient[F])
  def avgPrice(symbol: String): F[AvgPrice] =
    client.execute(AvgPriceReq(symbol))
```

## Extensibility

Adding a new endpoint requires only a `case class` for the request and a `given` instance. No client modifications needed:

```scala
// Your custom request type
case class MyCustomReq(symbol: String, interval: String)

// Your custom endpoint definition
given BinanceEndpoint[MyCustomReq, io.circe.Json] with
  def method   = HttpMethod.GET
  def prefix   = ApiPrefix.Api
  def version  = ApiVersion.V3
  def path     = Vector("myCustomEndpoint")
  def security = SecurityType.None
  def queryParams(req: MyCustomReq) =
    QueryString.empty.add("symbol", req.symbol).add("interval", req.interval)
  def decoder  = Decoder[io.circe.Json]

// Use it
client.execute(MyCustomReq("BTCUSDT", "1h"))
```

This is the key advantage of the typeclass approach: **the client is open for extension without modification**.
