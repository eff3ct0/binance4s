package io.github.rafafrdz.binance4s.domain.responses

import io.circe.Codec

case class ServerTime(serverTime: Long) derives Codec.AsObject

case class RateLimit(
    rateLimitType: String,
    interval: String,
    intervalNum: Int,
    limit: Int
) derives Codec.AsObject

case class SymbolFilter(filterType: String) derives Codec.AsObject

case class SymbolInfo(
    symbol: String,
    status: String,
    baseAsset: String,
    baseAssetPrecision: Int,
    quoteAsset: String,
    quotePrecision: Int,
    quoteAssetPrecision: Int,
    orderTypes: List[String],
    icebergAllowed: Boolean,
    ocoAllowed: Boolean,
    isSpotTradingAllowed: Boolean,
    isMarginTradingAllowed: Boolean,
    permissions: Option[List[String]] = None
) derives Codec.AsObject

case class ExchangeInfo(
    timezone: String,
    serverTime: Long,
    rateLimits: List[RateLimit],
    symbols: List[SymbolInfo]
) derives Codec.AsObject

case class ListenKey(listenKey: String) derives Codec.AsObject
