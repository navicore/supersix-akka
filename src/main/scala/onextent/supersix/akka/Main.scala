package onextent.supersix.akka

import Conf._
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.{Directives, Route}
import ch.megard.akka.http.cors.scaladsl.CorsDirectives._
import com.typesafe.scalalogging.LazyLogging
import onextent.supersix.akka.http.MasterRoute
import onextent.supersix.akka.http.functions.HttpSupport

object Main extends App with LazyLogging with HttpSupport with Directives {

  val route: Route =
    HealthCheck ~
      logRequest(urlpath) {
        handleErrors {
          cors(corsSettings) {
            MasterRoute()
          }
        }
      }

  Http().bindAndHandle(route, "0.0.0.0", port)

}
