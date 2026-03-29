package io.github.rafafrdz.binance4s.domain.responses

import io.circe.Codec

case class SystemStatus(status: Int, msg: String) derives Codec.AsObject

case class CoinNetwork(
  network: String,
  coin: String,
  name: String,
  isDefault: Boolean,
  depositEnable: Boolean,
  withdrawEnable: Boolean,
  depositDesc: Option[String] = None,
  withdrawDesc: Option[String] = None,
  withdrawFee: Option[String] = None,
  withdrawMin: Option[String] = None,
  withdrawMax: Option[String] = None,
  minConfirm: Option[Int] = None,
  unLockConfirm: Option[Int] = None
) derives Codec.AsObject

case class CoinInfo(
  coin: String,
  depositAllEnable: Boolean,
  free: String,
  freeze: String,
  locked: String,
  name: String,
  networkList: List[CoinNetwork],
  withdrawAllEnable: Boolean
) derives Codec.AsObject

case class DepositRecord(
  id: Option[String] = None,
  amount: String,
  coin: String,
  network: String,
  status: Int,
  address: String,
  addressTag: Option[String] = None,
  txId: Option[String] = None,
  insertTime: Long,
  confirmTimes: Option[String] = None
) derives Codec.AsObject

case class WithdrawRecord(
  id: String,
  amount: String,
  transactionFee: Option[String] = None,
  coin: String,
  status: Int,
  address: String,
  txId: Option[String] = None,
  applyTime: Option[String] = None,
  network: Option[String] = None,
  transferType: Option[Int] = None,
  confirmNo: Option[Int] = None
) derives Codec.AsObject

case class DepositAddress(
  address: String,
  coin: String,
  tag: String,
  url: String
) derives Codec.AsObject

case class WithdrawResult(id: String) derives Codec.AsObject

case class TransferResult(tranId: Long) derives Codec.AsObject

case class TransferRecord(
  asset: String,
  amount: String,
  `type`: String,
  status: String,
  tranId: Long,
  timestamp: Long
) derives Codec.AsObject

case class TransferHistory(
  total: Int,
  rows: List[TransferRecord]
) derives Codec.AsObject

case class UserAsset(
  asset: String,
  free: String,
  locked: String,
  freeze: String,
  withdrawing: String,
  ipoable: String,
  btcValuation: String
) derives Codec.AsObject

case class WalletBalance(
  activate: Boolean,
  balance: String,
  walletName: String
) derives Codec.AsObject

case class DustConvertResult(
  totalServiceCharge: String,
  totalTransfered: String,
  transferResult: List[DustTransfer]
) derives Codec.AsObject

case class DustTransfer(
  amount: String,
  fromAsset: String,
  operateTime: Long,
  serviceChargeAmount: String,
  tranId: Long,
  transferedAmount: String
) derives Codec.AsObject

case class DustLog(
  total: Int,
  userAssetDribblets: List[DustDribblet]
) derives Codec.AsObject

case class DustDribblet(
  operateTime: Long,
  totalTransferedAmount: String,
  totalServiceChargeAmount: String,
  transId: Long,
  userAssetDribbletDetails: List[DustDetail]
) derives Codec.AsObject

case class DustDetail(
  transId: Long,
  serviceChargeAmount: String,
  amount: String,
  operateTime: Long,
  transferedAmount: String,
  fromAsset: String
) derives Codec.AsObject

case class DustBtcAsset(
  asset: String,
  assetFullName: String,
  amountFree: String,
  toBTC: String,
  toBNB: String
) derives Codec.AsObject

case class DustBtcResult(
  totalTransferBtc: String,
  totalTransferBNB: String,
  dripiletPercentage: String,
  details: List[DustBtcAsset]
) derives Codec.AsObject

case class AssetDetail(
  minWithdrawAmount: String,
  depositStatus: Boolean,
  withdrawFee: Double,
  withdrawStatus: Boolean,
  depositTip: Option[String] = None
) derives Codec.AsObject

case class FundingAsset(
  asset: String,
  free: String,
  locked: String,
  freeze: String,
  withdrawing: String,
  btcValuation: String
) derives Codec.AsObject

case class AccountSnapshot(
  code: Int,
  msg: String,
  snapshotVos: List[SnapshotVo]
) derives Codec.AsObject

case class SnapshotVo(
  `type`: String,
  updateTime: Long,
  data: io.circe.Json
) derives Codec.AsObject

case class ApiRestrictions(
  ipRestrict: Boolean,
  createTime: Long,
  enableInternalTransfer: Option[Boolean] = None,
  enableFutures: Option[Boolean] = None,
  enableSpotAndMarginTrading: Option[Boolean] = None,
  enableReading: Option[Boolean] = None,
  enableWithdrawals: Option[Boolean] = None,
  permitsUniversalTransfer: Option[Boolean] = None
) derives Codec.AsObject

case class DelistScheduleItem(
  delistTime: Long,
  symbols: List[String]
) derives Codec.AsObject

case class AccountInfo2(
  vipLevel: Int,
  isMarginEnabled: Boolean,
  isFutureEnabled: Boolean
) derives Codec.AsObject

case class WithdrawQuota(
  asset: String,
  available: String,
  dailyLimit: String
) derives Codec.AsObject
