package eu.route20.hft.publish

import eu.route20.hft.notification.Notification
import grizzled.slf4j.Logging

trait Publisher {
  def publish(notification: Notification): Unit
}

class LoggingPublisher extends Publisher with Logging {
  def publish(notification: Notification): Unit = {
    info(notification)
  }
}

class DummyPublisher extends Publisher {
  def publish(notification: Notification): Unit = {
  }
}

class JeroMqPublisher(address: String) extends Publisher with Logging {
  import org.zeromq.ZMQ;
  val ctx = ZMQ.context(1)
  val socket = ctx.socket(ZMQ.PUB)
  socket.connect(address)

  def publish(notification: Notification): Unit = {
    socket.send(notification.msg)
  }
}

