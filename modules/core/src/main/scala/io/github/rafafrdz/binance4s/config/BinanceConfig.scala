package io.github.rafafrdz.binance4s.config

import cats.effect.Sync

import io.github.rafafrdz.binance4s.domain.BinanceMode

case class Credentials(apiKey: String, secretKey: String)

case class BinanceConfig(
    mode: BinanceMode = BinanceMode.Testnet,
    credentials: Option[Credentials] = scala.None,
    recvWindow: Option[Long] = scala.None
)

object BinanceConfig:

  def fromEnv[F[_]: Sync]: F[BinanceConfig] = Sync[F].delay {
    val mode = sys.env.get("BINANCE_MODE") match
      case Some("live") | Some("api") => BinanceMode.Live
      case _                          => BinanceMode.Testnet
    val creds = for
      ak <- sys.env.get("BINANCE_API_KEY")
      sk <- sys.env.get("BINANCE_SECRET_KEY")
    yield Credentials(ak, sk)
    BinanceConfig(mode, creds)
  }

  def builder: ConfigBuilder = ConfigBuilder(BinanceConfig())

  case class ConfigBuilder(config: BinanceConfig):
    def live: ConfigBuilder     = copy(config = config.copy(mode = BinanceMode.Live))
    def testnet: ConfigBuilder  = copy(config = config.copy(mode = BinanceMode.Testnet))
    def credentials(apiKey: String, secretKey: String): ConfigBuilder =
      copy(config = config.copy(credentials = Some(Credentials(apiKey, secretKey))))
    def recvWindow(ms: Long): ConfigBuilder =
      copy(config = config.copy(recvWindow = Some(ms)))
    def build: BinanceConfig = config
