package io.github.rafafrdz.binance4s.http.client

import cats.effect.Async
import cats.syntax.all.*

import org.http4s.{Header, Headers, Method, Request, Response, Uri}
import org.http4s.circe.*
import org.http4s.client.Client

import io.circe.{Decoder, Json}

import io.github.rafafrdz.binance4s.config.BinanceConfig
import io.github.rafafrdz.binance4s.domain.SecurityType
import io.github.rafafrdz.binance4s.endpoint.{BinanceEndpoint, HttpMethod}
import io.github.rafafrdz.binance4s.error.{BinanceApiErrorResponse, BinanceError}
import io.github.rafafrdz.binance4s.query.QueryString

import org.typelevel.ci.*

private[client] class BinanceHttpClient[F[_]: Async](
  val config: BinanceConfig,
  httpClient: Client[F]
) extends BinanceClient[F]:

  override def execute[Req, Resp](req: Req)(using ep: BinanceEndpoint[Req, Resp]): F[Resp] =
    for
      qs  <- buildQueryString(req, ep)
      uri <- buildUri(ep, qs)
      method  = toHttp4sMethod(ep.method)
      headers = buildHeaders(ep)
      request = Request[F](method = method, uri = uri, headers = headers)
      response <- httpClient.run(request).use(handleResponse[Resp](using ep.decoder))
    yield response

  private def buildQueryString[Req, Resp](req: Req, ep: BinanceEndpoint[Req, Resp]): F[QueryString] =
    Async[F].delay {
      val base = ep.queryParams(req)
      ep.security match
        case SecurityType.Trade | SecurityType.UserData =>
          config.credentials match
            case Some(creds) =>
              val withTs   = base.withTimestamp
              val withRecv = config.recvWindow.fold(withTs)(withTs.withRecvWindow)
              withRecv.withSignature(creds.secretKey)
            case scala.None =>
              base // will fail at header check
        case _ => base
    }

  private def buildUri[Req, Resp](ep: BinanceEndpoint[Req, Resp], qs: QueryString): F[Uri] =
    val base    = config.mode.baseUri
    val path    = ep.pathSegments.mkString("/")
    val query   = if qs.isEmpty then "" else s"?${qs.render}"
    val fullUri = s"$base/$path$query"
    Async[F].fromEither(
      Uri.fromString(fullUri).left.map(e => BinanceError.ConnectionError(new Exception(e.message)))
    )

  private def buildHeaders[Req, Resp](ep: BinanceEndpoint[Req, Resp]): Headers =
    ep.security match
      case SecurityType.None => Headers.empty
      case _ =>
        config.credentials match
          case Some(creds) => Headers(Header.Raw(ci"X-MBX-APIKEY", creds.apiKey))
          case scala.None  => Headers.empty

  private def handleResponse[A](using decoder: Decoder[A])(resp: Response[F]): F[A] =
    if resp.status.isSuccess then
      resp.as[Json].flatMap { json =>
        Async[F].fromEither(
          decoder.decodeJson(json).left.map(e => BinanceError.DecodingError(e.message, json))
        )
      }
    else
      resp.as[String].flatMap { body =>
        val error = io.circe.parser.decode[BinanceApiErrorResponse](body) match
          case Right(apiErr) => BinanceError.ApiError(apiErr.code, apiErr.msg)
          case Left(_) =>
            if resp.status.code == 429 then BinanceError.RateLimitError(scala.None)
            else BinanceError.HttpError(resp.status.code, body)
        Async[F].raiseError(error)
      }

  private def toHttp4sMethod(m: HttpMethod): Method = m match
    case HttpMethod.GET    => Method.GET
    case HttpMethod.POST   => Method.POST
    case HttpMethod.PUT    => Method.PUT
    case HttpMethod.DELETE => Method.DELETE
