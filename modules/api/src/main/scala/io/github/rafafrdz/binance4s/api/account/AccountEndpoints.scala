package io.github.rafafrdz.binance4s.api.account

import io.circe.Decoder

import io.github.rafafrdz.binance4s.domain.{ApiPrefix, ApiVersion, SecurityType}
import io.github.rafafrdz.binance4s.domain.responses.*
import io.github.rafafrdz.binance4s.endpoint.{BinanceEndpoint, HttpMethod}
import io.github.rafafrdz.binance4s.query.QueryString

// Request types
case class AccountInfoReq(recvWindow: Option[Long] = None)
case class OrderStatusReq(
  symbol: String,
  orderId: Option[Long] = None,
  origClientOrderId: Option[String] = None,
  recvWindow: Option[Long] = None
)
case class OpenOrdersReq(symbol: Option[String] = None, recvWindow: Option[Long] = None)
case class AllOrdersReq(
  symbol: String,
  orderId: Option[Long] = None,
  startTime: Option[Long] = None,
  endTime: Option[Long] = None,
  limit: Option[Int] = None,
  recvWindow: Option[Long] = None
)
case class OrderListStatusReq(
  orderListId: Option[Long] = None,
  origClientOrderId: Option[String] = None,
  recvWindow: Option[Long] = None
)
case class AllOrderListReq(
  fromId: Option[Long] = None,
  startTime: Option[Long] = None,
  endTime: Option[Long] = None,
  limit: Option[Int] = None,
  recvWindow: Option[Long] = None
)
case class OpenOrderListReq(recvWindow: Option[Long] = None)
case class MyTradesReq(
  symbol: String,
  orderId: Option[Long] = None,
  startTime: Option[Long] = None,
  endTime: Option[Long] = None,
  fromId: Option[Long] = None,
  limit: Option[Int] = None,
  recvWindow: Option[Long] = None
)
case class RateLimitOrderReq(recvWindow: Option[Long] = None)
case class PreventedMatchesReq(
  symbol: String,
  preventedMatchId: Option[Long] = None,
  orderId: Option[Long] = None,
  fromPreventedMatchId: Option[Long] = None,
  limit: Option[Int] = None,
  recvWindow: Option[Long] = None
)
case class AllocationsReq(
  symbol: String,
  startTime: Option[Long] = None,
  endTime: Option[Long] = None,
  fromAllocationId: Option[Long] = None,
  limit: Option[Int] = None,
  orderId: Option[Long] = None,
  recvWindow: Option[Long] = None
)
case class CommissionReq(symbol: String)

// Endpoint instances
given BinanceEndpoint[AccountInfoReq, AccountInfo] with
  def method                           = HttpMethod.GET
  def prefix                           = ApiPrefix.Api
  def version                          = ApiVersion.V3
  def path                             = Vector("account")
  def security                         = SecurityType.UserData
  def queryParams(req: AccountInfoReq) = QueryString.empty.addOpt("recvWindow", req.recvWindow)
  def decoder                          = Decoder[AccountInfo]
  override def weight                  = 20

given BinanceEndpoint[OrderStatusReq, OrderInfo] with
  def method   = HttpMethod.GET
  def prefix   = ApiPrefix.Api
  def version  = ApiVersion.V3
  def path     = Vector("order")
  def security = SecurityType.UserData
  def queryParams(req: OrderStatusReq) =
    QueryString.empty
      .add("symbol", req.symbol)
      .addOpt("orderId", req.orderId)
      .addOpt("origClientOrderId", req.origClientOrderId)
      .addOpt("recvWindow", req.recvWindow)
  def decoder = Decoder[OrderInfo]

given BinanceEndpoint[OpenOrdersReq, List[OrderInfo]] with
  def method   = HttpMethod.GET
  def prefix   = ApiPrefix.Api
  def version  = ApiVersion.V3
  def path     = Vector("openOrders")
  def security = SecurityType.UserData
  def queryParams(req: OpenOrdersReq) =
    QueryString.empty.addOpt("symbol", req.symbol).addOpt("recvWindow", req.recvWindow)
  def decoder = Decoder[List[OrderInfo]]

given BinanceEndpoint[AllOrdersReq, List[OrderInfo]] with
  def method   = HttpMethod.GET
  def prefix   = ApiPrefix.Api
  def version  = ApiVersion.V3
  def path     = Vector("allOrders")
  def security = SecurityType.UserData
  def queryParams(req: AllOrdersReq) =
    QueryString.empty
      .add("symbol", req.symbol)
      .addOpt("orderId", req.orderId)
      .addOpt("startTime", req.startTime)
      .addOpt("endTime", req.endTime)
      .addOpt("limit", req.limit)
      .addOpt("recvWindow", req.recvWindow)
  def decoder         = Decoder[List[OrderInfo]]
  override def weight = 20

given BinanceEndpoint[OrderListStatusReq, OrderListResponse] with
  def method   = HttpMethod.GET
  def prefix   = ApiPrefix.Api
  def version  = ApiVersion.V3
  def path     = Vector("orderList")
  def security = SecurityType.UserData
  def queryParams(req: OrderListStatusReq) =
    QueryString.empty
      .addOpt("orderListId", req.orderListId)
      .addOpt("origClientOrderId", req.origClientOrderId)
      .addOpt("recvWindow", req.recvWindow)
  def decoder = Decoder[OrderListResponse]

given BinanceEndpoint[AllOrderListReq, List[OrderListResponse]] with
  def method   = HttpMethod.GET
  def prefix   = ApiPrefix.Api
  def version  = ApiVersion.V3
  def path     = Vector("allOrderList")
  def security = SecurityType.UserData
  def queryParams(req: AllOrderListReq) =
    QueryString.empty
      .addOpt("fromId", req.fromId)
      .addOpt("startTime", req.startTime)
      .addOpt("endTime", req.endTime)
      .addOpt("limit", req.limit)
      .addOpt("recvWindow", req.recvWindow)
  def decoder         = Decoder[List[OrderListResponse]]
  override def weight = 20

given BinanceEndpoint[OpenOrderListReq, List[OrderListResponse]] with
  def method   = HttpMethod.GET
  def prefix   = ApiPrefix.Api
  def version  = ApiVersion.V3
  def path     = Vector("openOrderList")
  def security = SecurityType.UserData
  def queryParams(req: OpenOrderListReq) =
    QueryString.empty.addOpt("recvWindow", req.recvWindow)
  def decoder = Decoder[List[OrderListResponse]]

given BinanceEndpoint[MyTradesReq, List[MyTrade]] with
  def method   = HttpMethod.GET
  def prefix   = ApiPrefix.Api
  def version  = ApiVersion.V3
  def path     = Vector("myTrades")
  def security = SecurityType.UserData
  def queryParams(req: MyTradesReq) =
    QueryString.empty
      .add("symbol", req.symbol)
      .addOpt("orderId", req.orderId)
      .addOpt("startTime", req.startTime)
      .addOpt("endTime", req.endTime)
      .addOpt("fromId", req.fromId)
      .addOpt("limit", req.limit)
      .addOpt("recvWindow", req.recvWindow)
  def decoder = Decoder[List[MyTrade]]

given BinanceEndpoint[RateLimitOrderReq, List[RateLimitOrder]] with
  def method   = HttpMethod.GET
  def prefix   = ApiPrefix.Api
  def version  = ApiVersion.V3
  def path     = Vector("rateLimit", "order")
  def security = SecurityType.UserData
  def queryParams(req: RateLimitOrderReq) =
    QueryString.empty.addOpt("recvWindow", req.recvWindow)
  def decoder         = Decoder[List[RateLimitOrder]]
  override def weight = 40

given BinanceEndpoint[PreventedMatchesReq, List[PreventedMatch]] with
  def method   = HttpMethod.GET
  def prefix   = ApiPrefix.Api
  def version  = ApiVersion.V3
  def path     = Vector("myPreventedMatches")
  def security = SecurityType.UserData
  def queryParams(req: PreventedMatchesReq) =
    QueryString.empty
      .add("symbol", req.symbol)
      .addOpt("preventedMatchId", req.preventedMatchId)
      .addOpt("orderId", req.orderId)
      .addOpt("fromPreventedMatchId", req.fromPreventedMatchId)
      .addOpt("limit", req.limit)
      .addOpt("recvWindow", req.recvWindow)
  def decoder = Decoder[List[PreventedMatch]]

given BinanceEndpoint[AllocationsReq, List[SorAllocation]] with
  def method   = HttpMethod.GET
  def prefix   = ApiPrefix.Api
  def version  = ApiVersion.V3
  def path     = Vector("myAllocations")
  def security = SecurityType.UserData
  def queryParams(req: AllocationsReq) =
    QueryString.empty
      .add("symbol", req.symbol)
      .addOpt("startTime", req.startTime)
      .addOpt("endTime", req.endTime)
      .addOpt("fromAllocationId", req.fromAllocationId)
      .addOpt("limit", req.limit)
      .addOpt("orderId", req.orderId)
      .addOpt("recvWindow", req.recvWindow)
  def decoder         = Decoder[List[SorAllocation]]
  override def weight = 20

given BinanceEndpoint[CommissionReq, Commission] with
  def method                          = HttpMethod.GET
  def prefix                          = ApiPrefix.Api
  def version                         = ApiVersion.V3
  def path                            = Vector("account", "commission")
  def security                        = SecurityType.UserData
  def queryParams(req: CommissionReq) = QueryString.empty.add("symbol", req.symbol)
  def decoder                         = Decoder[Commission]
  override def weight                 = 20
