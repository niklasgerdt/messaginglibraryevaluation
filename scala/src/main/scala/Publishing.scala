package eu.route20.hft.publish

import eu.route20.hft.notification.Notification
import grizzled.slf4j.Logging

object LoggingPublisher extends Logging {
  def publish(notification: Notification): Unit = info(notification)
}

object JeroMqPublisher {
  import org.zeromq.ZMQ;
  val ctx = ZMQ.context(1)
  val socket = ctx.socket(ZMQ.PUB)

  def apply(address: String) = socket.connect(address)

  def publish(notification: Notification): Unit = socket.send(notification.msg)
}
