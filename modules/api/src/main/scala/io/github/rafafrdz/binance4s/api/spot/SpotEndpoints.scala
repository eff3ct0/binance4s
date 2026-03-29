package io.github.rafafrdz.binance4s.api.spot

import io.circe.Decoder

import io.github.rafafrdz.binance4s.domain.*
import io.github.rafafrdz.binance4s.domain.responses.*
import io.github.rafafrdz.binance4s.endpoint.{BinanceEndpoint, HttpMethod}
import io.github.rafafrdz.binance4s.query.QueryString

// Request types
case class NewOrderReq(
  symbol: String,
  side: OrderSide,
  `type`: OrderType,
  timeInForce: Option[TimeInForce] = None,
  quantity: Option[BigDecimal] = None,
  quoteOrderQty: Option[BigDecimal] = None,
  price: Option[BigDecimal] = None,
  newClientOrderId: Option[String] = None,
  stopPrice: Option[BigDecimal] = None,
  icebergQty: Option[BigDecimal] = None,
  newOrderRespType: Option[NewOrderRespType] = None,
  recvWindow: Option[Long] = None
)

case class TestOrderReq(
  symbol: String,
  side: OrderSide,
  `type`: OrderType,
  timeInForce: Option[TimeInForce] = None,
  quantity: Option[BigDecimal] = None,
  quoteOrderQty: Option[BigDecimal] = None,
  price: Option[BigDecimal] = None,
  newClientOrderId: Option[String] = None,
  stopPrice: Option[BigDecimal] = None,
  recvWindow: Option[Long] = None
)

case class CancelOrderReq(
  symbol: String,
  orderId: Option[Long] = None,
  origClientOrderId: Option[String] = None,
  recvWindow: Option[Long] = None
)

case class CancelAllOpenOrdersReq(symbol: String, recvWindow: Option[Long] = None)

case class CancelReplaceOrderReq(
  symbol: String,
  side: OrderSide,
  `type`: OrderType,
  cancelReplaceMode: CancelReplaceMode,
  cancelOrderId: Option[Long] = None,
  cancelOrigClientOrderId: Option[String] = None,
  timeInForce: Option[TimeInForce] = None,
  quantity: Option[BigDecimal] = None,
  price: Option[BigDecimal] = None,
  stopPrice: Option[BigDecimal] = None,
  recvWindow: Option[Long] = None
)

case class NewOcoReq(
  symbol: String,
  side: OrderSide,
  quantity: BigDecimal,
  price: BigDecimal,
  stopPrice: BigDecimal,
  listClientOrderId: Option[String] = None,
  stopLimitPrice: Option[BigDecimal] = None,
  stopLimitTimeInForce: Option[TimeInForce] = None,
  recvWindow: Option[Long] = None
)

case class NewOtoReq(
  symbol: String,
  listClientOrderId: Option[String] = None,
  workingType: String,
  workingSide: OrderSide,
  workingPrice: BigDecimal,
  workingQuantity: BigDecimal,
  pendingType: String,
  pendingSide: OrderSide,
  pendingQuantity: BigDecimal,
  pendingPrice: Option[BigDecimal] = None,
  recvWindow: Option[Long] = None
)

case class CancelOrderListReq(
  symbol: String,
  orderListId: Option[Long] = None,
  listClientOrderId: Option[String] = None,
  recvWindow: Option[Long] = None
)

case class SorOrderReq(
  symbol: String,
  side: OrderSide,
  `type`: OrderType,
  quantity: BigDecimal,
  timeInForce: Option[TimeInForce] = None,
  price: Option[BigDecimal] = None,
  recvWindow: Option[Long] = None
)

// Helper for building order query strings
private def orderParams(
  symbol: String,
  side: OrderSide,
  tpe: OrderType,
  timeInForce: Option[TimeInForce],
  quantity: Option[BigDecimal],
  quoteOrderQty: Option[BigDecimal],
  price: Option[BigDecimal],
  newClientOrderId: Option[String],
  stopPrice: Option[BigDecimal],
  icebergQty: Option[BigDecimal],
  newOrderRespType: Option[NewOrderRespType],
  recvWindow: Option[Long]
): QueryString =
  QueryString.empty
    .add("symbol", symbol)
    .add("side", side.toString)
    .add("type", tpe.toString)
    .addOpt("timeInForce", timeInForce.map(_.toString))
    .addOpt("quantity", quantity)
    .addOpt("quoteOrderQty", quoteOrderQty)
    .addOpt("price", price)
    .addOpt("newClientOrderId", newClientOrderId)
    .addOpt("stopPrice", stopPrice)
    .addOpt("icebergQty", icebergQty)
    .addOpt("newOrderRespType", newOrderRespType.map(_.toString))
    .addOpt("recvWindow", recvWindow)

// Endpoint instances
given BinanceEndpoint[NewOrderReq, OrderResponse] with
  def method   = HttpMethod.POST
  def prefix   = ApiPrefix.Api
  def version  = ApiVersion.V3
  def path     = Vector("order")
  def security = SecurityType.Trade
  def queryParams(req: NewOrderReq) =
    orderParams(
      req.symbol,
      req.side,
      req.`type`,
      req.timeInForce,
      req.quantity,
      req.quoteOrderQty,
      req.price,
      req.newClientOrderId,
      req.stopPrice,
      req.icebergQty,
      req.newOrderRespType,
      req.recvWindow
    )
  def decoder = Decoder[OrderResponse]

given BinanceEndpoint[TestOrderReq, io.circe.Json] with
  def method   = HttpMethod.POST
  def prefix   = ApiPrefix.Api
  def version  = ApiVersion.V3
  def path     = Vector("order", "test")
  def security = SecurityType.Trade
  def queryParams(req: TestOrderReq) =
    QueryString.empty
      .add("symbol", req.symbol)
      .add("side", req.side.toString)
      .add("type", req.`type`.toString)
      .addOpt("timeInForce", req.timeInForce.map(_.toString))
      .addOpt("quantity", req.quantity)
      .addOpt("quoteOrderQty", req.quoteOrderQty)
      .addOpt("price", req.price)
      .addOpt("newClientOrderId", req.newClientOrderId)
      .addOpt("stopPrice", req.stopPrice)
      .addOpt("recvWindow", req.recvWindow)
  def decoder = Decoder[io.circe.Json]

given BinanceEndpoint[CancelOrderReq, CancelOrderResponse] with
  def method   = HttpMethod.DELETE
  def prefix   = ApiPrefix.Api
  def version  = ApiVersion.V3
  def path     = Vector("order")
  def security = SecurityType.Trade
  def queryParams(req: CancelOrderReq) =
    QueryString.empty
      .add("symbol", req.symbol)
      .addOpt("orderId", req.orderId)
      .addOpt("origClientOrderId", req.origClientOrderId)
      .addOpt("recvWindow", req.recvWindow)
  def decoder = Decoder[CancelOrderResponse]

given BinanceEndpoint[CancelAllOpenOrdersReq, List[CancelOrderResponse]] with
  def method   = HttpMethod.DELETE
  def prefix   = ApiPrefix.Api
  def version  = ApiVersion.V3
  def path     = Vector("openOrders")
  def security = SecurityType.Trade
  def queryParams(req: CancelAllOpenOrdersReq) =
    QueryString.empty.add("symbol", req.symbol).addOpt("recvWindow", req.recvWindow)
  def decoder = Decoder[List[CancelOrderResponse]]

given BinanceEndpoint[CancelReplaceOrderReq, CancelReplaceResponse] with
  def method   = HttpMethod.POST
  def prefix   = ApiPrefix.Api
  def version  = ApiVersion.V3
  def path     = Vector("order", "cancelReplace")
  def security = SecurityType.Trade
  def queryParams(req: CancelReplaceOrderReq) =
    QueryString.empty
      .add("symbol", req.symbol)
      .add("side", req.side.toString)
      .add("type", req.`type`.toString)
      .add("cancelReplaceMode", req.cancelReplaceMode.toString)
      .addOpt("cancelOrderId", req.cancelOrderId)
      .addOpt("cancelOrigClientOrderId", req.cancelOrigClientOrderId)
      .addOpt("timeInForce", req.timeInForce.map(_.toString))
      .addOpt("quantity", req.quantity)
      .addOpt("price", req.price)
      .addOpt("stopPrice", req.stopPrice)
      .addOpt("recvWindow", req.recvWindow)
  def decoder = Decoder[CancelReplaceResponse]

given BinanceEndpoint[NewOcoReq, OrderListResponse] with
  def method   = HttpMethod.POST
  def prefix   = ApiPrefix.Api
  def version  = ApiVersion.V3
  def path     = Vector("orderList", "oco")
  def security = SecurityType.Trade
  def queryParams(req: NewOcoReq) =
    QueryString.empty
      .add("symbol", req.symbol)
      .add("side", req.side.toString)
      .add("quantity", req.quantity)
      .add("price", req.price)
      .add("stopPrice", req.stopPrice)
      .addOpt("listClientOrderId", req.listClientOrderId)
      .addOpt("stopLimitPrice", req.stopLimitPrice)
      .addOpt("stopLimitTimeInForce", req.stopLimitTimeInForce.map(_.toString))
      .addOpt("recvWindow", req.recvWindow)
  def decoder = Decoder[OrderListResponse]

given BinanceEndpoint[CancelOrderListReq, OrderListResponse] with
  def method   = HttpMethod.DELETE
  def prefix   = ApiPrefix.Api
  def version  = ApiVersion.V3
  def path     = Vector("orderList")
  def security = SecurityType.Trade
  def queryParams(req: CancelOrderListReq) =
    QueryString.empty
      .add("symbol", req.symbol)
      .addOpt("orderListId", req.orderListId)
      .addOpt("listClientOrderId", req.listClientOrderId)
      .addOpt("recvWindow", req.recvWindow)
  def decoder = Decoder[OrderListResponse]

given BinanceEndpoint[SorOrderReq, OrderResponse] with
  def method   = HttpMethod.POST
  def prefix   = ApiPrefix.Api
  def version  = ApiVersion.V3
  def path     = Vector("sor", "order")
  def security = SecurityType.Trade
  def queryParams(req: SorOrderReq) =
    QueryString.empty
      .add("symbol", req.symbol)
      .add("side", req.side.toString)
      .add("type", req.`type`.toString)
      .add("quantity", req.quantity)
      .addOpt("timeInForce", req.timeInForce.map(_.toString))
      .addOpt("price", req.price)
      .addOpt("recvWindow", req.recvWindow)
  def decoder = Decoder[OrderResponse]
