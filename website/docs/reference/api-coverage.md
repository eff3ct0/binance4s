---
sidebar_position: 1
---

# API Coverage

Complete list of Binance API endpoints implemented in Binance4s.

## REST Endpoints (56+)

### General (3)

| Endpoint | Method | Path | Auth |
|----------|--------|------|------|
| Ping | GET | `/api/v3/ping` | None |
| Server Time | GET | `/api/v3/time` | None |
| Exchange Info | GET | `/api/v3/exchangeInfo` | None |

### Market Data (14)

| Endpoint | Method | Path | Auth |
|----------|--------|------|------|
| Order Book | GET | `/api/v3/depth` | None |
| Recent Trades | GET | `/api/v3/trades` | None |
| Historical Trades | GET | `/api/v3/historicalTrades` | API Key |
| Aggregate Trades | GET | `/api/v3/aggTrades` | None |
| Klines | GET | `/api/v3/klines` | None |
| UI Klines | GET | `/api/v3/uiKlines` | None |
| Average Price | GET | `/api/v3/avgPrice` | None |
| 24hr Ticker | GET | `/api/v3/ticker/24hr` | None |
| Trading Day Ticker | GET | `/api/v3/ticker/tradingDay` | None |
| Price Ticker | GET | `/api/v3/ticker/price` | None |
| Book Ticker | GET | `/api/v3/ticker/bookTicker` | None |
| Rolling Ticker | GET | `/api/v3/ticker` | None |
| Reference Price | GET | `/api/v3/referencePrice` | None |

### Spot Trading (9)

| Endpoint | Method | Path | Auth |
|----------|--------|------|------|
| New Order | POST | `/api/v3/order` | HMAC |
| Test Order | POST | `/api/v3/order/test` | HMAC |
| Cancel Order | DELETE | `/api/v3/order` | HMAC |
| Cancel All Open Orders | DELETE | `/api/v3/openOrders` | HMAC |
| Cancel Replace | POST | `/api/v3/order/cancelReplace` | HMAC |
| New OCO | POST | `/api/v3/orderList/oco` | HMAC |
| Cancel Order List | DELETE | `/api/v3/orderList` | HMAC |
| SOR Order | POST | `/api/v3/sor/order` | HMAC |

### Account (13)

| Endpoint | Method | Path | Auth |
|----------|--------|------|------|
| Account Info | GET | `/api/v3/account` | HMAC |
| Order Status | GET | `/api/v3/order` | HMAC |
| Open Orders | GET | `/api/v3/openOrders` | HMAC |
| All Orders | GET | `/api/v3/allOrders` | HMAC |
| Order List Status | GET | `/api/v3/orderList` | HMAC |
| All Order Lists | GET | `/api/v3/allOrderList` | HMAC |
| Open Order Lists | GET | `/api/v3/openOrderList` | HMAC |
| My Trades | GET | `/api/v3/myTrades` | HMAC |
| Rate Limit Order | GET | `/api/v3/rateLimit/order` | HMAC |
| Prevented Matches | GET | `/api/v3/myPreventedMatches` | HMAC |
| SOR Allocations | GET | `/api/v3/myAllocations` | HMAC |
| Commission | GET | `/api/v3/account/commission` | HMAC |

### Wallet (17)

| Endpoint | Method | Path | Auth |
|----------|--------|------|------|
| System Status | GET | `/sapi/v1/system/status` | None |
| All Coins | GET | `/sapi/v1/capital/config/getall` | HMAC |
| Account Snapshot | GET | `/sapi/v1/accountSnapshot` | HMAC |
| Deposit History | GET | `/sapi/v1/capital/deposit/hisrec` | HMAC |
| Withdraw History | GET | `/sapi/v1/capital/withdraw/history` | HMAC |
| Deposit Address | GET | `/sapi/v1/capital/deposit/address` | HMAC |
| Withdraw | POST | `/sapi/v1/capital/withdraw/apply` | HMAC |
| Transfer | POST | `/sapi/v1/asset/transfer` | HMAC |
| Transfer History | GET | `/sapi/v1/asset/transfer` | HMAC |
| User Asset | POST | `/sapi/v3/asset/getUserAsset` | HMAC |
| Wallet Balance | GET | `/sapi/v1/asset/wallet/balance` | HMAC |
| Dust Log | GET | `/sapi/v1/asset/dribblet` | HMAC |
| Asset Detail | GET | `/sapi/v1/asset/assetDetail` | HMAC |
| API Restrictions | GET | `/sapi/v1/account/apiRestrictions` | HMAC |
| Delist Schedule | GET | `/sapi/v1/spot/delist-schedule` | HMAC |
| Withdraw Quota | GET | `/sapi/v1/capital/withdraw/quota` | HMAC |

### User Data Stream (3)

| Endpoint | Method | Path | Auth |
|----------|--------|------|------|
| Create Listen Key | POST | `/api/v3/userDataStream` | API Key |
| Keep-Alive | PUT | `/api/v3/userDataStream` | API Key |
| Close | DELETE | `/api/v3/userDataStream` | API Key |

## WebSocket Streams (12)

| Stream | Pattern | Event Type |
|--------|---------|------------|
| Aggregate Trade | `<symbol>@aggTrade` | `WsAggTrade` |
| Trade | `<symbol>@trade` | `WsTrade` |
| Kline | `<symbol>@kline_<interval>` | `WsKline` |
| Mini Ticker | `<symbol>@miniTicker` | `WsMiniTicker` |
| All Mini Tickers | `!miniTicker@arr` | `List[WsMiniTicker]` |
| Ticker | `<symbol>@ticker` | `WsTicker` |
| All Tickers | `!ticker@arr` | `List[WsTicker]` |
| Book Ticker | `<symbol>@bookTicker` | `WsBookTicker` |
| Average Price | `<symbol>@avgPrice` | `WsAvgPrice` |
| Partial Depth | `<symbol>@depth<levels>@<speed>` | `WsDepthUpdate` |
| Diff Depth | `<symbol>@depth@<speed>` | `WsDepthUpdate` |
| User Data | `<listenKey>` | `Json` |
