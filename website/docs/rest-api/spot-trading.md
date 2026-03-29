---
sidebar_position: 3
---

# Spot Trading Endpoints

Place and manage orders on the Binance Spot exchange. All endpoints require **HMAC-SHA256 signature** (SecurityType: `Trade`).

> Binance API Reference: [Spot Account/Trade](https://developers.binance.com/docs/binance-spot-api-docs/rest-api/spot-account-trade)

:::warning
Trading endpoints modify your account. Always test with the **Testnet** first:
```scala
val config = BinanceConfig(mode = BinanceMode.Testnet)
```
:::

## New Order

Place a new order.

```scala
import io.github.rafafrdz.binance4s.api.spot.*

val order: F[OrderResponse] = client.newOrder(NewOrderReq(
  symbol   = "BTCUSDT",
  side     = OrderSide.BUY,
  `type`   = OrderType.LIMIT,
  timeInForce = Some(TimeInForce.GTC),
  quantity = Some(BigDecimal("0.001")),
  price    = Some(BigDecimal("50000"))
))
```

| | |
|---|---|
| **Endpoint** | `POST /api/v3/order` |
| **Auth** | HMAC Signed |
| **Weight** | 1 |

### Order Types

| Type | Required Fields |
|------|----------------|
| `LIMIT` | timeInForce, quantity, price |
| `MARKET` | quantity OR quoteOrderQty |
| `STOP_LOSS` | quantity, stopPrice |
| `STOP_LOSS_LIMIT` | timeInForce, quantity, price, stopPrice |
| `TAKE_PROFIT` | quantity, stopPrice |
| `TAKE_PROFIT_LIMIT` | timeInForce, quantity, price, stopPrice |
| `LIMIT_MAKER` | quantity, price |

## Test Order

Test new order creation without actually placing it. Same parameters as `newOrder`.

```scala
val test: F[Json] = client.testOrder(TestOrderReq(
  symbol = "BTCUSDT",
  side   = OrderSide.BUY,
  `type` = OrderType.MARKET,
  quantity = Some(BigDecimal("0.001"))
))
```

| | |
|---|---|
| **Endpoint** | `POST /api/v3/order/test` |
| **Auth** | HMAC Signed |
| **Weight** | 1 |

## Cancel Order

Cancel an active order.

```scala
val cancelled: F[CancelOrderResponse] =
  client.cancelOrder("BTCUSDT", orderId = Some(12345L))
```

| | |
|---|---|
| **Endpoint** | `DELETE /api/v3/order` |
| **Auth** | HMAC Signed |
| **Weight** | 1 |

## Cancel All Open Orders

Cancel all open orders on a symbol.

```scala
val cancelled: F[List[CancelOrderResponse]] =
  client.cancelAllOpenOrders("BTCUSDT")
```

| | |
|---|---|
| **Endpoint** | `DELETE /api/v3/openOrders` |
| **Auth** | HMAC Signed |
| **Weight** | 1 |

## Cancel and Replace (Atomic)

Atomically cancel an existing order and place a new one.

```scala
val result: F[CancelReplaceResponse] = client.cancelReplaceOrder(
  CancelReplaceOrderReq(
    symbol = "BTCUSDT",
    side = OrderSide.BUY,
    `type` = OrderType.LIMIT,
    cancelReplaceMode = CancelReplaceMode.STOP_ON_FAILURE,
    cancelOrderId = Some(12345L),
    quantity = Some(BigDecimal("0.002")),
    price = Some(BigDecimal("51000")),
    timeInForce = Some(TimeInForce.GTC)
  )
)
```

| | |
|---|---|
| **Endpoint** | `POST /api/v3/order/cancelReplace` |
| **Auth** | HMAC Signed |
| **Weight** | 1 |

## OCO Order

Place a new OCO (One-Cancels-the-Other) order.

```scala
val oco: F[OrderListResponse] = client.newOco(NewOcoReq(
  symbol    = "BTCUSDT",
  side      = OrderSide.SELL,
  quantity  = BigDecimal("0.001"),
  price     = BigDecimal("70000"),
  stopPrice = BigDecimal("60000")
))
```

| | |
|---|---|
| **Endpoint** | `POST /api/v3/orderList/oco` |
| **Auth** | HMAC Signed |
| **Weight** | 1 |

## SOR Order (Smart Order Routing)

Place a new order using Smart Order Routing.

```scala
val sor: F[OrderResponse] = client.sorOrder(SorOrderReq(
  symbol = "BTCUSDT",
  side   = OrderSide.BUY,
  `type` = OrderType.LIMIT,
  quantity = BigDecimal("0.001"),
  price = Some(BigDecimal("50000")),
  timeInForce = Some(TimeInForce.GTC)
))
```

| | |
|---|---|
| **Endpoint** | `POST /api/v3/sor/order` |
| **Auth** | HMAC Signed |
| **Weight** | 1 |
