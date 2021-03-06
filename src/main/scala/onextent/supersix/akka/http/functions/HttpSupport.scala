package onextent.supersix.akka.http.functions

import java.io.IOException

import akka.http.scaladsl.model._
import akka.http.scaladsl.model.headers.HttpOriginRange
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.{Directive, ExceptionHandler, RejectionHandler, Route}
import akka.util.Timeout
import ch.megard.akka.http.cors.scaladsl.CorsDirectives._
import ch.megard.akka.http.cors.scaladsl.settings.CorsSettings
import com.typesafe.scalalogging.LazyLogging
import onextent.supersix.akka.Conf._

import scala.concurrent.duration.{Duration, FiniteDuration}

trait HttpSupport extends LazyLogging {

  val corsSettings: CorsSettings.Default = CorsSettings.defaultSettings.copy(
    allowedOrigins = HttpOriginRange(corsOriginList: _*))

  val rejectionHandler
    : RejectionHandler = corsRejectionHandler withFallback RejectionHandler.default

  val exceptionHandler: ExceptionHandler = ExceptionHandler {
    case e: IOException =>
      logger.warn(s"$e")
      complete(StatusCodes.ServiceUnavailable, e.getMessage)
    case e: Exception =>
      logger.warn(s"do not know $e")
      complete(StatusCodes.InternalServerError, e.getMessage)
  }

  val handleErrors
    : Directive[Unit] = handleRejections(rejectionHandler) & handleExceptions(
    exceptionHandler)

  def HealthCheck: Route =
    path("healthcheck") {
      get {
        complete("ok")
      }
    }

  def requestTimeout: Timeout = {
    val t = conf.getString("akka.http.server.request-timeout")
    val d = Duration(t)
    FiniteDuration(d.length, d.unit)
  }

  implicit val timeout: Timeout = requestTimeout
}
