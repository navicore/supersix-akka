package onextent.supersix.akka.http.functions

import java.util.Properties

import akka.http.scaladsl.server.{Directive1, Directives}
import com.typesafe.scalalogging.LazyLogging
import org.apache.kafka.clients.producer.{Callback, KafkaProducer, ProducerRecord, RecordMetadata}
import scala.concurrent.{Future, Promise}
import onextent.supersix.akka.Conf._

/**
  * write strings to default kafka topic
  *
  */
trait KafkaProducerDirective extends LazyLogging with Directives {

  val props = new Properties()
  props.put("bootstrap.servers", bootstrap)
  props.put("client.id", appName)
  props.put("key.serializer",
            "org.apache.kafka.common.serialization.StringSerializer")
  props.put("value.serializer",
            "org.apache.kafka.common.serialization.StringSerializer")

  val producer = new KafkaProducer[String, String](props)

  def write(obj: String, key: String): Directive1[Future[Unit]] = {

    // todo: remove promise use.  direct use of promise is now an anti-pattern :|
    val promise = Promise[Unit]()
    val data = new ProducerRecord[String, String](observationsTopic, key, obj)

    val cb = new Callback {
      override def onCompletion(metadata: RecordMetadata,
                                exception: Exception): Unit = {
        Option(exception) match {
          case Some(_) =>
            logger.error(s"write to kafka $obj failed: $exception")
            promise.failure(exception)
          case None =>
            logger.debug(s"wrote with key $key to kafka")
            promise.success((): Unit)
        }

      }
    }
    producer.send(data, cb)
    provide(promise.future)
  }

}
