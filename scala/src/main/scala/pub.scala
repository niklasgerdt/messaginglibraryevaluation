package eu.route20.hft.pub

import eu.route20.hft.common.Notification
import grizzled.slf4j.Logging

trait Pub extends Logging {
  def pub(n: Notification): Unit
}

trait DummyPub extends Pub {
  override def pub(n: Notification): Unit = {}
}

trait LoggingPub extends Pub with Logging {
  override def pub(n: Notification): Unit = info(n)
}

final class JeroMqPublisher private(address: String) extends Pub with Logging {
  import org.zeromq.ZMQ

  val ctx = ZMQ.context(1)
  val socket = ctx.socket(ZMQ.PUB)
  socket.connect(address)

  override def pub(n: Notification): Unit = socket.send(n.body)
}

object JeroMqPublisher {
  def apply(address: String) = new JeroMqPublisher(address)
}
