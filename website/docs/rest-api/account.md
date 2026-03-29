---
sidebar_position: 4
---

# Account Endpoints

Query account information, order status, and trade history. All endpoints require **HMAC-SHA256 signature** (SecurityType: `UserData`).

> Binance API Reference: [Spot Account/Trade](https://developers.binance.com/docs/binance-spot-api-docs/rest-api/spot-account-trade)

## Account Information

Get current account information including balances and permissions.

```scala
val account: F[AccountInfo] = client.accountInfo
```

| | |
|---|---|
| **Endpoint** | `GET /api/v3/account` |
| **Auth** | HMAC Signed |
| **Weight** | 20 |

**Key response fields:**

```scala
case class AccountInfo(
  canTrade: Boolean,
  canWithdraw: Boolean,
  canDeposit: Boolean,
  accountType: String,
  balances: List[Balance],  // Balance(asset, free, locked)
  // ...
)
```

## Query Order

Check the status of an order.

```scala
val order: F[OrderInfo] = client.orderStatus("BTCUSDT", orderId = Some(12345L))
```

| | |
|---|---|
| **Endpoint** | `GET /api/v3/order` |
| **Auth** | HMAC Signed |
| **Weight** | 4 |

## Open Orders

Get all open orders on a symbol or all symbols.

```scala
// Single symbol
val open: F[List[OrderInfo]] = client.openOrders(Some("BTCUSDT"))

// All symbols
val allOpen: F[List[OrderInfo]] = client.openOrders()
```

| | |
|---|---|
| **Endpoint** | `GET /api/v3/openOrders` |
| **Auth** | HMAC Signed |
| **Weight** | 6 (single) / 80 (all) |

## All Orders

Get all orders (active, canceled, filled) for a symbol.

```scala
val orders: F[List[OrderInfo]] = client.allOrders("BTCUSDT", limit = Some(100))
```

| | |
|---|---|
| **Endpoint** | `GET /api/v3/allOrders` |
| **Auth** | HMAC Signed |
| **Weight** | 20 |

## My Trades

Get the account's trade history for a symbol.

```scala
val trades: F[List[MyTrade]] = client.myTrades("BTCUSDT", limit = Some(50))
```

| | |
|---|---|
| **Endpoint** | `GET /api/v3/myTrades` |
| **Auth** | HMAC Signed |
| **Weight** | 20 |

## Order Rate Limit

Get the current unfilled order count for each rate limit interval.

```scala
val limits: F[List[RateLimitOrder]] = client.rateLimitOrder
```

| | |
|---|---|
| **Endpoint** | `GET /api/v3/rateLimit/order` |
| **Auth** | HMAC Signed |
| **Weight** | 40 |

## Commission Rates

Get the commission rates for a specific symbol.

```scala
val comm: F[Commission] = client.commission("BTCUSDT")
```

| | |
|---|---|
| **Endpoint** | `GET /api/v3/account/commission` |
| **Auth** | HMAC Signed |
| **Weight** | 20 |
