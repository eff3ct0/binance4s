package io.github.rafafrdz.binance4s.api

import io.circe.Json

import io.github.rafafrdz.binance4s.domain.*
import io.github.rafafrdz.binance4s.domain.responses.*
import io.github.rafafrdz.binance4s.http.client.BinanceClient
import io.github.rafafrdz.binance4s.api.general.{*, given}
import io.github.rafafrdz.binance4s.api.market.{*, given}
import io.github.rafafrdz.binance4s.api.spot.{*, given}
import io.github.rafafrdz.binance4s.api.account.{*, given}
import io.github.rafafrdz.binance4s.api.wallet.{*, given}
import io.github.rafafrdz.binance4s.api.userdata.{*, given}

extension [F[_]](client: BinanceClient[F])

  // === General ===
  def ping: F[Json]                = client.execute(Ping)
  def serverTime: F[ServerTime]    = client.execute(ServerTimeReq)
  def exchangeInfo: F[ExchangeInfo] = client.execute(ExchangeInfoReq())
  def exchangeInfo(symbol: String): F[ExchangeInfo] = client.execute(ExchangeInfoReq(symbol = Some(symbol)))

  // === Market Data ===
  def depth(symbol: String, limit: Option[Int] = None): F[OrderBook] =
    client.execute(DepthReq(symbol, limit))
  def trades(symbol: String, limit: Option[Int] = None): F[List[Trade]] =
    client.execute(TradesReq(symbol, limit))
  def historicalTrades(symbol: String, limit: Option[Int] = None, fromId: Option[Long] = None): F[List[Trade]] =
    client.execute(HistoricalTradesReq(symbol, limit, fromId))
  def aggTrades(symbol: String, limit: Option[Int] = None): F[List[AggTrade]] =
    client.execute(AggTradesReq(symbol, limit = limit))
  def klines(symbol: String, interval: KlineInterval, limit: Option[Int] = None): F[List[Kline]] =
    client.execute(KlinesReq(symbol, interval, limit = limit))
  def avgPrice(symbol: String): F[AvgPrice] =
    client.execute(AvgPriceReq(symbol))
  def ticker24h(symbol: Option[String] = None): F[List[Ticker24h]] =
    client.execute(Ticker24hReq(symbol))
  def tickerPrice: F[List[TickerPrice]] =
    client.execute(TickerPriceReq())
  def tickerPrice(symbol: String): F[TickerPrice] =
    client.execute(SingleTickerPriceReq(symbol))
  def bookTicker(symbol: Option[String] = None): F[List[BookTicker]] =
    client.execute(BookTickerReq(symbol))
  def referencePrice(symbol: String): F[ReferencePrice] =
    client.execute(ReferencePriceReq(symbol))

  // === Spot Trading ===
  def newOrder(req: NewOrderReq): F[OrderResponse] = client.execute(req)
  def testOrder(req: TestOrderReq): F[Json] = client.execute(req)
  def cancelOrder(symbol: String, orderId: Option[Long] = None, origClientOrderId: Option[String] = None): F[CancelOrderResponse] =
    client.execute(CancelOrderReq(symbol, orderId, origClientOrderId))
  def cancelAllOpenOrders(symbol: String): F[List[CancelOrderResponse]] =
    client.execute(CancelAllOpenOrdersReq(symbol))
  def cancelReplaceOrder(req: CancelReplaceOrderReq): F[CancelReplaceResponse] = client.execute(req)
  def newOco(req: NewOcoReq): F[OrderListResponse] = client.execute(req)
  def cancelOrderList(symbol: String, orderListId: Option[Long] = None): F[OrderListResponse] =
    client.execute(CancelOrderListReq(symbol, orderListId))
  def sorOrder(req: SorOrderReq): F[OrderResponse] = client.execute(req)

  // === Account ===
  def accountInfo: F[AccountInfo] = client.execute(AccountInfoReq())
  def orderStatus(symbol: String, orderId: Option[Long] = None): F[OrderInfo] =
    client.execute(OrderStatusReq(symbol, orderId))
  def openOrders(symbol: Option[String] = None): F[List[OrderInfo]] =
    client.execute(OpenOrdersReq(symbol))
  def allOrders(symbol: String, limit: Option[Int] = None): F[List[OrderInfo]] =
    client.execute(AllOrdersReq(symbol, limit = limit))
  def myTrades(symbol: String, limit: Option[Int] = None): F[List[MyTrade]] =
    client.execute(MyTradesReq(symbol, limit = limit))
  def rateLimitOrder: F[List[RateLimitOrder]] = client.execute(RateLimitOrderReq())
  def commission(symbol: String): F[Commission] = client.execute(CommissionReq(symbol))

  // === Wallet ===
  def systemStatus: F[SystemStatus] = client.execute(SystemStatusReq)
  def allCoins: F[List[CoinInfo]] = client.execute(AllCoinsReq())
  def depositHistory(coin: Option[String] = None): F[List[DepositRecord]] =
    client.execute(DepositHistoryReq(coin = coin))
  def withdrawHistory(coin: Option[String] = None): F[List[WithdrawRecord]] =
    client.execute(WithdrawHistoryReq(coin = coin))
  def depositAddress(coin: String, network: Option[String] = None): F[DepositAddress] =
    client.execute(DepositAddressReq(coin, network))
  def withdraw(coin: String, address: String, amount: BigDecimal, network: Option[String] = None): F[WithdrawResult] =
    client.execute(WithdrawReq(coin, address, amount, network))
  def transfer(tpe: TransferType, asset: String, amount: BigDecimal): F[TransferResult] =
    client.execute(TransferReq(tpe, asset, amount))
  def dustLog: F[DustLog] = client.execute(DustLogReq())
  def apiRestrictions: F[ApiRestrictions] = client.execute(ApiRestrictionsReq())
  def delistSchedule: F[List[DelistScheduleItem]] = client.execute(DelistScheduleReq)

  // === User Data Stream ===
  def createListenKey: F[ListenKey] = client.execute(CreateListenKeyReq)
  def keepAliveListenKey(listenKey: String): F[Json] = client.execute(KeepAliveListenKeyReq(listenKey))
  def closeListenKey(listenKey: String): F[Json] = client.execute(CloseListenKeyReq(listenKey))
