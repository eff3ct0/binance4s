---
sidebar_position: 1
slug: /
title: Binance4s
---

# Binance4s

A **typeclass-driven**, functional Scala 3 library for the [Binance API](https://developers.binance.com/docs/binance-spot-api-docs/rest-api).

Built on **Cats Effect**, **http4s**, **Circe**, and **fs2**.

## The Problem

The Binance API exposes hundreds of REST endpoints and WebSocket streams. Building a Scala client means dealing with:

- **URI construction** for each endpoint with different paths, query parameters, and authentication requirements
- **Response deserialization** for dozens of different JSON shapes (objects, arrays, arrays of arrays)
- **HMAC-SHA256 signing** for authenticated endpoints with precise query string ordering
- **HTTP client lifecycle** management across many concurrent requests
- **Error handling** for API-specific error codes, rate limits, and network failures

## The Insight

Every Binance endpoint follows the same pattern: a **request** produces a **response** through a specific HTTP method, path, security model, and set of query parameters. This is a textbook use case for **typeclasses**.

Binance4s defines a single typeclass `BinanceEndpoint[Req, Resp]` that captures this pattern. Each endpoint is a `given` instance. The compiler statically resolves which endpoint to call and what type the response will be.

```scala
// The compiler knows this returns F[OrderBook]
client.execute(DepthReq("BTCUSDT", Some(10)))

// Equivalent via syntax extension
client.depth("BTCUSDT", limit = Some(10))
```

## Features

- **56+ REST endpoint definitions** covering General, Market Data, Spot Trading, Account, Wallet, and User Data Stream
- **12 WebSocket stream types** via `fs2.Stream` (aggTrade, trade, kline, ticker, depth, etc.)
- **Typeclass architecture** with `given`/`using` for compile-time endpoint resolution
- **Resource-safe HTTP client** via `Resource[F, BinanceClient[F]]` (shared connection pool)
- **Effect-polymorphic** design with `F[_]: Async` (works with IO, ZIO interop, etc.)
- **Typed error handling** via `BinanceError` enum with pattern matching
- **Zero-boilerplate JSON codecs** with Circe `derives Codec.AsObject`
- **HMAC-SHA256 signing** with zero external dependencies (JDK `javax.crypto.Mac`)

## Tech Stack

| Library | Version | Purpose |
|---------|---------|---------|
| Scala | 3.3.4 LTS | Language |
| Cats Effect | 3.5.7 | Effect system |
| http4s | 0.23.30 | HTTP client |
| Circe | 0.14.10 | JSON codecs |
| fs2 | 3.11.0 | Streaming |
| munit | 1.0.3 | Testing |
