---
sidebar_position: 7
---

# Configuration

Binance4s uses a simple case class for configuration. No external config files or frameworks required.

## BinanceConfig

```scala
case class BinanceConfig(
  mode: BinanceMode = BinanceMode.Testnet,
  credentials: Option[Credentials] = None,
  recvWindow: Option[Long] = None
)

case class Credentials(apiKey: String, secretKey: String)
```

## Construction Methods

### Direct

```scala
val config = BinanceConfig(
  mode = BinanceMode.Live,
  credentials = Some(Credentials("api-key", "secret-key")),
  recvWindow = Some(5000L)
)
```

### Builder

```scala
val config = BinanceConfig.builder
  .live                                       // or .testnet
  .credentials("api-key", "secret-key")
  .recvWindow(5000L)
  .build
```

### From Environment Variables

```scala
val config: IO[BinanceConfig] = BinanceConfig.fromEnv[IO]
```

Reads from:

| Variable | Description | Default |
|----------|-------------|---------|
| `BINANCE_MODE` | `live` or `testnet` | `testnet` |
| `BINANCE_API_KEY` | Your API key | None |
| `BINANCE_SECRET_KEY` | Your secret key | None |

## Modes

| Mode | Base URI | WebSocket URI |
|------|----------|---------------|
| `BinanceMode.Live` | `https://api.binance.com` | `wss://stream.binance.com:9443` |
| `BinanceMode.Testnet` | `https://testnet.binance.vision` | `wss://testnet.binance.vision` |

## recvWindow

The `recvWindow` parameter specifies the maximum milliseconds a request can be ahead of the server time. Binance rejects requests where `timestamp + recvWindow < serverTime`. Default is 5000ms on the Binance side.

:::tip
If you get `-1021 Timestamp for this request was 1000ms ahead of the server's time` errors, your system clock may be out of sync. Either sync your clock with NTP or increase `recvWindow`.
:::

## Security

- **Never commit credentials** to version control
- Use environment variables or a secrets manager
- The **Testnet** has separate API keys from production. Generate them at [testnet.binance.vision](https://testnet.binance.vision/)
- Only enable the permissions you need (e.g., read-only for market data)
