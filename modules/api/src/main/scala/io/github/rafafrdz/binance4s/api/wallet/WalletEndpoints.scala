package io.github.rafafrdz.binance4s.api.wallet

import io.circe.Decoder

import io.github.rafafrdz.binance4s.domain.{AccountSnapshotType, ApiPrefix, ApiVersion, SecurityType, TransferType}
import io.github.rafafrdz.binance4s.domain.responses.*
import io.github.rafafrdz.binance4s.endpoint.{BinanceEndpoint, HttpMethod}
import io.github.rafafrdz.binance4s.query.QueryString

// Request types
case object SystemStatusReq
case class AllCoinsReq(recvWindow: Option[Long] = None)
case class AccountSnapshotReq(
  `type`: AccountSnapshotType,
  startTime: Option[Long] = None,
  endTime: Option[Long] = None,
  limit: Option[Int] = None,
  recvWindow: Option[Long] = None
)
case class DepositHistoryReq(
  coin: Option[String] = None,
  status: Option[Int] = None,
  startTime: Option[Long] = None,
  endTime: Option[Long] = None,
  offset: Option[Int] = None,
  limit: Option[Int] = None,
  recvWindow: Option[Long] = None
)
case class WithdrawHistoryReq(
  coin: Option[String] = None,
  status: Option[Int] = None,
  startTime: Option[Long] = None,
  endTime: Option[Long] = None,
  offset: Option[Int] = None,
  limit: Option[Int] = None,
  recvWindow: Option[Long] = None
)
case class DepositAddressReq(coin: String, network: Option[String] = None, recvWindow: Option[Long] = None)
case class WithdrawReq(
  coin: String,
  address: String,
  amount: BigDecimal,
  network: Option[String] = None,
  addressTag: Option[String] = None,
  name: Option[String] = None,
  recvWindow: Option[Long] = None
)
case class TransferReq(`type`: TransferType, asset: String, amount: BigDecimal, recvWindow: Option[Long] = None)
case class TransferHistoryReq(
  `type`: TransferType,
  startTime: Option[Long] = None,
  endTime: Option[Long] = None,
  current: Option[Int] = None,
  size: Option[Int] = None,
  recvWindow: Option[Long] = None
)
case class UserAssetReq(asset: Option[String] = None, recvWindow: Option[Long] = None)
case class WalletBalanceReq(recvWindow: Option[Long] = None)
case class DustConvertReq(asset: List[String], recvWindow: Option[Long] = None)
case class DustLogReq(startTime: Option[Long] = None, endTime: Option[Long] = None, recvWindow: Option[Long] = None)
case class DustBtcReq(recvWindow: Option[Long] = None)
case class AssetDetailReq(asset: Option[String] = None, recvWindow: Option[Long] = None)
case class FundingAssetReq(asset: Option[String] = None, recvWindow: Option[Long] = None)
case class ApiRestrictionsReq(recvWindow: Option[Long] = None)
case class WithdrawQuotaReq(asset: String, recvWindow: Option[Long] = None)
case object DelistScheduleReq

// Endpoint instances
given BinanceEndpoint[SystemStatusReq.type, SystemStatus] with
  def method                                 = HttpMethod.GET
  def prefix                                 = ApiPrefix.Sapi
  def version                                = ApiVersion.V1
  def path                                   = Vector("system", "status")
  def security                               = SecurityType.None
  def queryParams(req: SystemStatusReq.type) = QueryString.empty
  def decoder                                = Decoder[SystemStatus]

given BinanceEndpoint[AllCoinsReq, List[CoinInfo]] with
  def method                        = HttpMethod.GET
  def prefix                        = ApiPrefix.Sapi
  def version                       = ApiVersion.V1
  def path                          = Vector("capital", "config", "getall")
  def security                      = SecurityType.UserData
  def queryParams(req: AllCoinsReq) = QueryString.empty.addOpt("recvWindow", req.recvWindow)
  def decoder                       = Decoder[List[CoinInfo]]
  override def weight               = 10

given BinanceEndpoint[AccountSnapshotReq, AccountSnapshot] with
  def method   = HttpMethod.GET
  def prefix   = ApiPrefix.Sapi
  def version  = ApiVersion.V1
  def path     = Vector("accountSnapshot")
  def security = SecurityType.UserData
  def queryParams(req: AccountSnapshotReq) =
    QueryString.empty
      .add("type", req.`type`.toString)
      .addOpt("startTime", req.startTime)
      .addOpt("endTime", req.endTime)
      .addOpt("limit", req.limit)
      .addOpt("recvWindow", req.recvWindow)
  def decoder = Decoder[AccountSnapshot]

given BinanceEndpoint[DepositHistoryReq, List[DepositRecord]] with
  def method   = HttpMethod.GET
  def prefix   = ApiPrefix.Sapi
  def version  = ApiVersion.V1
  def path     = Vector("capital", "deposit", "hisrec")
  def security = SecurityType.UserData
  def queryParams(req: DepositHistoryReq) =
    QueryString.empty
      .addOpt("coin", req.coin)
      .addOpt("status", req.status)
      .addOpt("startTime", req.startTime)
      .addOpt("endTime", req.endTime)
      .addOpt("offset", req.offset)
      .addOpt("limit", req.limit)
      .addOpt("recvWindow", req.recvWindow)
  def decoder = Decoder[List[DepositRecord]]

given BinanceEndpoint[WithdrawHistoryReq, List[WithdrawRecord]] with
  def method   = HttpMethod.GET
  def prefix   = ApiPrefix.Sapi
  def version  = ApiVersion.V1
  def path     = Vector("capital", "withdraw", "history")
  def security = SecurityType.UserData
  def queryParams(req: WithdrawHistoryReq) =
    QueryString.empty
      .addOpt("coin", req.coin)
      .addOpt("status", req.status)
      .addOpt("startTime", req.startTime)
      .addOpt("endTime", req.endTime)
      .addOpt("offset", req.offset)
      .addOpt("limit", req.limit)
      .addOpt("recvWindow", req.recvWindow)
  def decoder = Decoder[List[WithdrawRecord]]

given BinanceEndpoint[DepositAddressReq, DepositAddress] with
  def method   = HttpMethod.GET
  def prefix   = ApiPrefix.Sapi
  def version  = ApiVersion.V1
  def path     = Vector("capital", "deposit", "address")
  def security = SecurityType.UserData
  def queryParams(req: DepositAddressReq) =
    QueryString.empty
      .add("coin", req.coin)
      .addOpt("network", req.network)
      .addOpt("recvWindow", req.recvWindow)
  def decoder = Decoder[DepositAddress]

given BinanceEndpoint[WithdrawReq, WithdrawResult] with
  def method   = HttpMethod.POST
  def prefix   = ApiPrefix.Sapi
  def version  = ApiVersion.V1
  def path     = Vector("capital", "withdraw", "apply")
  def security = SecurityType.UserData
  def queryParams(req: WithdrawReq) =
    QueryString.empty
      .add("coin", req.coin)
      .add("address", req.address)
      .add("amount", req.amount)
      .addOpt("network", req.network)
      .addOpt("addressTag", req.addressTag)
      .addOpt("name", req.name)
      .addOpt("recvWindow", req.recvWindow)
  def decoder = Decoder[WithdrawResult]

given BinanceEndpoint[TransferReq, TransferResult] with
  def method   = HttpMethod.POST
  def prefix   = ApiPrefix.Sapi
  def version  = ApiVersion.V1
  def path     = Vector("asset", "transfer")
  def security = SecurityType.UserData
  def queryParams(req: TransferReq) =
    QueryString.empty
      .add("type", req.`type`.toString)
      .add("asset", req.asset)
      .add("amount", req.amount)
      .addOpt("recvWindow", req.recvWindow)
  def decoder = Decoder[TransferResult]

given BinanceEndpoint[TransferHistoryReq, TransferHistory] with
  def method   = HttpMethod.GET
  def prefix   = ApiPrefix.Sapi
  def version  = ApiVersion.V1
  def path     = Vector("asset", "transfer")
  def security = SecurityType.UserData
  def queryParams(req: TransferHistoryReq) =
    QueryString.empty
      .add("type", req.`type`.toString)
      .addOpt("startTime", req.startTime)
      .addOpt("endTime", req.endTime)
      .addOpt("current", req.current)
      .addOpt("size", req.size)
      .addOpt("recvWindow", req.recvWindow)
  def decoder = Decoder[TransferHistory]

given BinanceEndpoint[UserAssetReq, List[UserAsset]] with
  def method   = HttpMethod.POST
  def prefix   = ApiPrefix.Sapi
  def version  = ApiVersion.V3
  def path     = Vector("asset", "getUserAsset")
  def security = SecurityType.UserData
  def queryParams(req: UserAssetReq) =
    QueryString.empty.addOpt("asset", req.asset).addOpt("recvWindow", req.recvWindow)
  def decoder = Decoder[List[UserAsset]]

given BinanceEndpoint[WalletBalanceReq, List[WalletBalance]] with
  def method                             = HttpMethod.GET
  def prefix                             = ApiPrefix.Sapi
  def version                            = ApiVersion.V1
  def path                               = Vector("asset", "wallet", "balance")
  def security                           = SecurityType.UserData
  def queryParams(req: WalletBalanceReq) = QueryString.empty.addOpt("recvWindow", req.recvWindow)
  def decoder                            = Decoder[List[WalletBalance]]

given BinanceEndpoint[DustLogReq, DustLog] with
  def method   = HttpMethod.GET
  def prefix   = ApiPrefix.Sapi
  def version  = ApiVersion.V1
  def path     = Vector("asset", "dribblet")
  def security = SecurityType.UserData
  def queryParams(req: DustLogReq) =
    QueryString.empty
      .addOpt("startTime", req.startTime)
      .addOpt("endTime", req.endTime)
      .addOpt("recvWindow", req.recvWindow)
  def decoder = Decoder[DustLog]

given BinanceEndpoint[AssetDetailReq, Map[String, AssetDetail]] with
  def method   = HttpMethod.GET
  def prefix   = ApiPrefix.Sapi
  def version  = ApiVersion.V1
  def path     = Vector("asset", "assetDetail")
  def security = SecurityType.UserData
  def queryParams(req: AssetDetailReq) =
    QueryString.empty.addOpt("asset", req.asset).addOpt("recvWindow", req.recvWindow)
  def decoder = Decoder[Map[String, AssetDetail]]

given BinanceEndpoint[ApiRestrictionsReq, ApiRestrictions] with
  def method                               = HttpMethod.GET
  def prefix                               = ApiPrefix.Sapi
  def version                              = ApiVersion.V1
  def path                                 = Vector("account", "apiRestrictions")
  def security                             = SecurityType.UserData
  def queryParams(req: ApiRestrictionsReq) = QueryString.empty.addOpt("recvWindow", req.recvWindow)
  def decoder                              = Decoder[ApiRestrictions]

given BinanceEndpoint[DelistScheduleReq.type, List[DelistScheduleItem]] with
  def method                                   = HttpMethod.GET
  def prefix                                   = ApiPrefix.Sapi
  def version                                  = ApiVersion.V1
  def path                                     = Vector("spot", "delist-schedule")
  def security                                 = SecurityType.UserData
  def queryParams(req: DelistScheduleReq.type) = QueryString.empty
  def decoder                                  = Decoder[List[DelistScheduleItem]]

given BinanceEndpoint[WithdrawQuotaReq, WithdrawQuota] with
  def method   = HttpMethod.GET
  def prefix   = ApiPrefix.Sapi
  def version  = ApiVersion.V1
  def path     = Vector("capital", "withdraw", "quota")
  def security = SecurityType.UserData
  def queryParams(req: WithdrawQuotaReq) =
    QueryString.empty.add("asset", req.asset).addOpt("recvWindow", req.recvWindow)
  def decoder = Decoder[WithdrawQuota]
