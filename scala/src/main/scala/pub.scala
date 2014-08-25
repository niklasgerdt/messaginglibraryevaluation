package eu.route20.hft.pub

import eu.route20.hft.common.Notification
import grizzled.slf4j.Logging

trait Pub {
  def pub(n: Notification): Unit
}

trait DummyPub extends Pub {
  override def pub(n: Notification) = {}
}

object LoggingPub extends Pub with Logging {
  override def pub(n: Notification) = info(n)
}

trait JeroMqPublisher extends Pub with Logging {
  import org.zeromq.ZMQ;
  val ctx = ZMQ.context(1)
  val socket = ctx.socket(ZMQ.PUB)

  def apply(address: String) = socket.connect(address)

  override def pub(n: Notification): Unit = socket.send(n.msg)
}
