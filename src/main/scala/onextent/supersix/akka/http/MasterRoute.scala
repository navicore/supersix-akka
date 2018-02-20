package onextent.supersix.akka.http

import scala.concurrent.duration._
import akka.Done
import akka.http.scaladsl.model.StatusCodes
import akka.http.scaladsl.server.{Directives, Route}
import akka.stream.scaladsl.{Flow, Keep, Sink, Source}
import com.typesafe.scalalogging.LazyLogging
import onextent.supersix.akka.http.functions.{HttpSupport, KafkaProducerDirective}
import onextent.supersix.akka.models.functions.JsonSupport
import spray.json._
import onextent.supersix.akka.Conf._
import onextent.supersix.akka.models.LoadMasterRequest

import scala.collection.immutable
import scala.concurrent.{Await, Future}

object MasterRoute
    extends JsonSupport
    with LazyLogging
    with Directives
    with KafkaProducerDirective
    with HttpSupport {

  def apply(): Route =
    path(urlpath / "master" / "load") {
      post {
        decodeRequest {
          entity(as[LoadMasterRequest]) { loadReq =>
            logger.debug(s"loadReq: $loadReq")
            val src = Source(immutable.Seq(1, 2, 3))
            val flo = Flow[Int].map(_ * 2)
            val sin = Sink.foreach(println)
            val runFlow = (src via flo).toMat(sin)(Keep.right)
            val f: Future[Done] = runFlow.run()
            Await.result(f, 60 seconds)
            complete(StatusCodes.Accepted)
          }
        }
      }
    }

}
