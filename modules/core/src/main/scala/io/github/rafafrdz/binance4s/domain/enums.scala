package io.github.rafafrdz.binance4s.domain

enum BinanceMode(val baseUri: String, val wsUri: String):
  case Live    extends BinanceMode("https://api.binance.com", "wss://stream.binance.com:9443")
  case Testnet extends BinanceMode("https://testnet.binance.vision", "wss://testnet.binance.vision")

enum OrderSide:
  case BUY, SELL

enum OrderType:
  case LIMIT, MARKET, STOP_LOSS, STOP_LOSS_LIMIT, TAKE_PROFIT, TAKE_PROFIT_LIMIT, LIMIT_MAKER

enum TimeInForce:
  case GTC, IOC, FOK

enum NewOrderRespType:
  case ACK, RESULT, FULL

enum SideEffectType:
  case NO_SIDE_EFFECT, MARGIN_BUY, AUTO_REPAY, AUTO_BORROW_REPAY

enum CancelReplaceMode:
  case STOP_ON_FAILURE, ALLOW_FAILURE

enum KlineInterval(val value: String):
  case `1s`  extends KlineInterval("1s")
  case `1m`  extends KlineInterval("1m")
  case `3m`  extends KlineInterval("3m")
  case `5m`  extends KlineInterval("5m")
  case `15m` extends KlineInterval("15m")
  case `30m` extends KlineInterval("30m")
  case `1h`  extends KlineInterval("1h")
  case `2h`  extends KlineInterval("2h")
  case `4h`  extends KlineInterval("4h")
  case `6h`  extends KlineInterval("6h")
  case `8h`  extends KlineInterval("8h")
  case `12h` extends KlineInterval("12h")
  case `1d`  extends KlineInterval("1d")
  case `3d`  extends KlineInterval("3d")
  case `1w`  extends KlineInterval("1w")
  case `1M`  extends KlineInterval("1M")

enum DepositStatus(val code: Int):
  case Pending  extends DepositStatus(0)
  case Credited extends DepositStatus(6)
  case Success  extends DepositStatus(1)

enum WithdrawStatus(val code: Int):
  case EmailSent     extends WithdrawStatus(0)
  case Cancelled     extends WithdrawStatus(1)
  case AwaitApproval extends WithdrawStatus(2)
  case Rejected      extends WithdrawStatus(3)
  case Processing    extends WithdrawStatus(4)
  case Failure       extends WithdrawStatus(5)
  case Completed     extends WithdrawStatus(6)

enum TransferType:
  case MAIN_UMFUTURE, MAIN_CMFUTURE, MAIN_MARGIN,
       UMFUTURE_MAIN, UMFUTURE_MARGIN, CMFUTURE_MAIN,
       CMFUTURE_MARGIN, MARGIN_MAIN, MARGIN_UMFUTURE,
       MARGIN_CMFUTURE, MAIN_FUNDING, FUNDING_MAIN,
       FUNDING_UMFUTURE, FUNDING_CMFUTURE, FUNDING_MARGIN

enum AccountSnapshotType:
  case SPOT, MARGIN, FUTURES

enum SecurityType:
  case None, UserStream, MarketData, Trade, UserData

enum ApiPrefix(val value: String):
  case Api  extends ApiPrefix("api")
  case Sapi extends ApiPrefix("sapi")

enum ApiVersion(val value: String):
  case V1 extends ApiVersion("v1")
  case V2 extends ApiVersion("v2")
  case V3 extends ApiVersion("v3")
