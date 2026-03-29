# Changelog

## [2.0.0] - 2026-03-29

### Complete rewrite to Scala 3

**Breaking changes** -- This is a ground-up rewrite. The v1.x API is not compatible.

### Added
- Scala 3.3 LTS with typeclass-style architecture
- `BinanceEndpoint[Req, Resp]` typeclass -- compile-time type safety between request and response types
- 56+ REST endpoint definitions covering General, Market Data, Spot Trading, Account, Wallet, and User Data Stream APIs
- 12 WebSocket stream types (aggTrade, trade, kline, miniTicker, ticker, bookTicker, depth, etc.)
- `BinanceClient[F]` with `Resource`-based lifecycle -- shared HTTP connection pool
- `BinanceWsClient[F]` with `fs2.Stream` for real-time WebSocket data
- `BinanceError` enum for typed error handling (ApiError, HttpError, RateLimitError, etc.)
- `QueryString` opaque type preserving insertion order
- HMAC-SHA256 signing via JDK `javax.crypto.Mac` (zero external dependencies)
- Effect-polymorphic design -- works with any `F[_]: Async`
- Circe `derives Codec.AsObject` for all response models
- Extension methods for ergonomic client DSL (`client.ping`, `client.depth(...)`, etc.)
- munit test suite with 21 unit tests
- GitHub Actions CI (Java 17 + 21)
- Examples: GeneralExample, MarketDataExample, WebSocketExample

### Removed
- Scala 2.13 codebase
- `com.outr.hasher` dependency (replaced by JDK crypto)
- `scala-scraper` dependency (unused)
- `http4s-ember-server` dependency (client-only library)
- `typesafe-config` dependency (replaced by case class config)
- `better-monadic-for` compiler plugin (built-in in Scala 3)
- Custom URI DSL (`/`, `\`, `?` operators) -- replaced by type-safe endpoint definitions
- `BinanceTask[T]` type alias -- replaced by `BinanceEndpoint` typeclass

### Fixed
- Critical bug: HTTP client was created and destroyed per request (now shared via `Resource`)
- Zero error handling -- all errors now typed via `BinanceError` enum
- No response models -- all responses now have typed case classes
- Query parameters used `Set` (lost insertion order) -- now `Vector`-based opaque type
