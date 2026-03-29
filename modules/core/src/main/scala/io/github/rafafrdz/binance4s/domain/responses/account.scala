package io.github.rafafrdz.binance4s.domain.responses

import io.circe.Codec

case class Balance(
    asset: String,
    free: String,
    locked: String
) derives Codec.AsObject

case class AccountInfo(
    makerCommission: Int,
    takerCommission: Int,
    buyerCommission: Int,
    sellerCommission: Int,
    commissionRates: Option[CommissionRates] = None,
    canTrade: Boolean,
    canWithdraw: Boolean,
    canDeposit: Boolean,
    brokered: Option[Boolean] = None,
    requireSelfTradePrevention: Option[Boolean] = None,
    preventSor: Option[Boolean] = None,
    updateTime: Long,
    accountType: String,
    balances: List[Balance],
    permissions: Option[List[String]] = None
) derives Codec.AsObject

case class CommissionRates(
    maker: String,
    taker: String,
    buyer: String,
    seller: String
) derives Codec.AsObject
