---
sidebar_position: 5
---

# Wallet Endpoints

Manage deposits, withdrawals, transfers, and wallet configuration. Most endpoints use the `/sapi/` prefix and require **HMAC-SHA256 signature**.

> Binance API Reference: [Wallet Endpoints](https://developers.binance.com/docs/wallet)

## System Status

Check Binance system status. **No authentication required.**

```scala
val status: F[SystemStatus] = client.systemStatus
// SystemStatus(status: Int, msg: String)
// status 0 = normal, 1 = maintenance
```

| | |
|---|---|
| **Endpoint** | `GET /sapi/v1/system/status` |
| **Auth** | None |

## All Coins Information

Get information on all supported coins including network details.

```scala
val coins: F[List[CoinInfo]] = client.allCoins
```

| | |
|---|---|
| **Endpoint** | `GET /sapi/v1/capital/config/getall` |
| **Auth** | HMAC Signed |
| **Weight** | 10 |

## Deposit History

Query deposit records.

```scala
val deposits: F[List[DepositRecord]] = client.depositHistory(coin = Some("BTC"))
```

| | |
|---|---|
| **Endpoint** | `GET /sapi/v1/capital/deposit/hisrec` |
| **Auth** | HMAC Signed |

## Withdrawal History

Query withdrawal records.

```scala
val withdrawals: F[List[WithdrawRecord]] = client.withdrawHistory(coin = Some("ETH"))
```

| | |
|---|---|
| **Endpoint** | `GET /sapi/v1/capital/withdraw/history` |
| **Auth** | HMAC Signed |

## Deposit Address

Fetch a deposit address for a specific coin and network.

```scala
val addr: F[DepositAddress] = client.depositAddress("BTC", network = Some("BTC"))
```

| | |
|---|---|
| **Endpoint** | `GET /sapi/v1/capital/deposit/address` |
| **Auth** | HMAC Signed |

## Withdraw

Submit a withdrawal request.

```scala
val result: F[WithdrawResult] = client.withdraw(
  coin    = "USDT",
  address = "TN...",
  amount  = BigDecimal("100"),
  network = Some("TRX")
)
```

| | |
|---|---|
| **Endpoint** | `POST /sapi/v1/capital/withdraw/apply` |
| **Auth** | HMAC Signed |

:::warning
Withdrawal requests are irreversible. Double-check the address and network before calling this endpoint.
:::

## Universal Transfer

Transfer funds between wallets (Spot, Margin, Futures, Funding).

```scala
import io.github.rafafrdz.binance4s.domain.TransferType

val transfer: F[TransferResult] = client.transfer(
  TransferType.MAIN_FUNDING, "USDT", BigDecimal("500")
)
```

| | |
|---|---|
| **Endpoint** | `POST /sapi/v1/asset/transfer` |
| **Auth** | HMAC Signed |

## Dust Log

Get the history of small balance conversions to BNB.

```scala
val log: F[DustLog] = client.dustLog
```

| | |
|---|---|
| **Endpoint** | `GET /sapi/v1/asset/dribblet` |
| **Auth** | HMAC Signed |

## API Key Restrictions

Get the permissions and restrictions of the current API key.

```scala
val restrictions: F[ApiRestrictions] = client.apiRestrictions
```

| | |
|---|---|
| **Endpoint** | `GET /sapi/v1/account/apiRestrictions` |
| **Auth** | HMAC Signed |

## Account Snapshot

Get a daily account snapshot (Spot, Margin, or Futures).

```scala
import io.github.rafafrdz.binance4s.api.wallet.AccountSnapshotReq
import io.github.rafafrdz.binance4s.domain.AccountSnapshotType

val snapshot: F[AccountSnapshot] = client.execute(AccountSnapshotReq(AccountSnapshotType.SPOT))
```

| | |
|---|---|
| **Endpoint** | `GET /sapi/v1/accountSnapshot` |
| **Auth** | HMAC Signed |

## Transfer History

Query universal transfer history.

```scala
import io.github.rafafrdz.binance4s.api.wallet.TransferHistoryReq
import io.github.rafafrdz.binance4s.domain.TransferType

val history: F[TransferHistory] = client.execute(TransferHistoryReq(TransferType.MAIN_FUNDING))
```

| | |
|---|---|
| **Endpoint** | `GET /sapi/v1/asset/transfer` |
| **Auth** | HMAC Signed |

## User Asset

Get user assets (balances with valuations).

```scala
import io.github.rafafrdz.binance4s.api.wallet.UserAssetReq

val assets: F[List[UserAsset]] = client.execute(UserAssetReq(asset = Some("BTC")))
```

| | |
|---|---|
| **Endpoint** | `POST /sapi/v3/asset/getUserAsset` |
| **Auth** | HMAC Signed |

## Wallet Balance

Get the balance of all wallets.

```scala
import io.github.rafafrdz.binance4s.api.wallet.WalletBalanceReq

val balances: F[List[WalletBalance]] = client.execute(WalletBalanceReq())
```

| | |
|---|---|
| **Endpoint** | `GET /sapi/v1/asset/wallet/balance` |
| **Auth** | HMAC Signed |

## Asset Detail

Get details about assets (fees, min withdraw amounts, etc.).

```scala
import io.github.rafafrdz.binance4s.api.wallet.AssetDetailReq

val details: F[Map[String, AssetDetail]] = client.execute(AssetDetailReq(asset = Some("BTC")))
```

| | |
|---|---|
| **Endpoint** | `GET /sapi/v1/asset/assetDetail` |
| **Auth** | HMAC Signed |

## Withdraw Quota

Get the withdrawal quota for a specific asset.

```scala
import io.github.rafafrdz.binance4s.api.wallet.WithdrawQuotaReq

val quota: F[WithdrawQuota] = client.execute(WithdrawQuotaReq("BTC"))
```

| | |
|---|---|
| **Endpoint** | `GET /sapi/v1/capital/withdraw/quota` |
| **Auth** | HMAC Signed |

## Delist Schedule

Get the scheduled delistings.

```scala
val schedule: F[List[DelistScheduleItem]] = client.delistSchedule
```

| | |
|---|---|
| **Endpoint** | `GET /sapi/v1/spot/delist-schedule` |
| **Auth** | HMAC Signed |
