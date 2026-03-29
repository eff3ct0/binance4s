package io.github.rafafrdz.binance4s.domain.responses

import io.circe.Codec

case class Fill(
    price: String,
    qty: String,
    commission: String,
    commissionAsset: String,
    tradeId: Long
) derives Codec.AsObject

case class OrderResponse(
    symbol: String,
    orderId: Long,
    orderListId: Long,
    clientOrderId: String,
    transactTime: Option[Long] = None,
    price: Option[String] = None,
    origQty: Option[String] = None,
    executedQty: Option[String] = None,
    cummulativeQuoteQty: Option[String] = None,
    status: Option[String] = None,
    timeInForce: Option[String] = None,
    `type`: Option[String] = None,
    side: Option[String] = None,
    fills: Option[List[Fill]] = None
) derives Codec.AsObject

case class CancelOrderResponse(
    symbol: String,
    origClientOrderId: String,
    orderId: Long,
    orderListId: Long,
    clientOrderId: String,
    transactTime: Option[Long] = None,
    price: Option[String] = None,
    origQty: Option[String] = None,
    executedQty: Option[String] = None,
    cummulativeQuoteQty: Option[String] = None,
    status: Option[String] = None,
    timeInForce: Option[String] = None,
    `type`: Option[String] = None,
    side: Option[String] = None
) derives Codec.AsObject

case class CancelReplaceResponse(
    cancelResult: String,
    newOrderResult: String,
    cancelResponse: Option[CancelOrderResponse] = None,
    newOrderResponse: Option[OrderResponse] = None
) derives Codec.AsObject

case class OrderInfo(
    symbol: String,
    orderId: Long,
    orderListId: Long,
    clientOrderId: String,
    price: String,
    origQty: String,
    executedQty: String,
    cummulativeQuoteQty: String,
    status: String,
    timeInForce: String,
    `type`: String,
    side: String,
    stopPrice: Option[String] = None,
    icebergQty: Option[String] = None,
    time: Long = 0L,
    updateTime: Long = 0L,
    isWorking: Boolean = true,
    origQuoteOrderQty: Option[String] = None
) derives Codec.AsObject

case class OrderListResponse(
    orderListId: Long,
    contingencyType: String,
    listStatusType: String,
    listOrderStatus: String,
    listClientOrderId: String,
    transactionTime: Long,
    symbol: String,
    orders: List[OrderListOrder],
    orderReports: Option[List[OrderResponse]] = None
) derives Codec.AsObject

case class OrderListOrder(
    symbol: String,
    orderId: Long,
    clientOrderId: String
) derives Codec.AsObject

case class MyTrade(
    symbol: String,
    id: Long,
    orderId: Long,
    orderListId: Long,
    price: String,
    qty: String,
    quoteQty: String,
    commission: String,
    commissionAsset: String,
    time: Long,
    isBuyer: Boolean,
    isMaker: Boolean,
    isBestMatch: Boolean
) derives Codec.AsObject

case class RateLimitOrder(
    rateLimitType: String,
    interval: String,
    intervalNum: Int,
    limit: Int,
    count: Int
) derives Codec.AsObject

case class Commission(
    symbol: String,
    standardCommission: StandardCommission,
    taxCommission: TaxCommission,
    discount: Option[CommissionDiscount] = None
) derives Codec.AsObject

case class StandardCommission(
    maker: String,
    taker: String,
    buyer: String,
    seller: String
) derives Codec.AsObject

case class TaxCommission(
    maker: String,
    taker: String,
    buyer: String,
    seller: String
) derives Codec.AsObject

case class CommissionDiscount(
    enabledForAccount: Boolean,
    enabledForSymbol: Boolean,
    discountAsset: String,
    discount: String
) derives Codec.AsObject

case class PreventedMatch(
    symbol: String,
    preventedMatchId: Long,
    takerOrderId: Long,
    makerOrderId: Long,
    tradeGroupId: Long,
    selfTradePreventionMode: String,
    price: String,
    makerPreventedQuantity: String,
    transactTime: Long
) derives Codec.AsObject

case class SorAllocation(
    symbol: String,
    allocationId: Long,
    allocationType: String,
    orderId: Long,
    orderListId: Long,
    price: String,
    qty: String,
    quoteQty: String,
    commission: String,
    commissionAsset: String,
    time: Long,
    isBuyer: Boolean,
    isMaker: Boolean,
    isAllocator: Boolean
) derives Codec.AsObject
