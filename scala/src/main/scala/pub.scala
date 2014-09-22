package eu.route20.hft.pub

import eu.route20.hft.common.Notification
import grizzled.slf4j.Logging

trait Pub {
  def pub(n: Notification): Unit
}

trait DummyPub extends Pub {
  override def pub(n: Notification): Unit = {}
}

trait LoggingPub extends Pub with Logging {
  override def pub(n: Notification): Unit = info(n)
}

class JeroMqPub private(addr: String) extends Pub with Logging {

  import org.zeromq.ZMQ

  val ctx = ZMQ.context(1)
  val socket = ctx.socket(ZMQ.PUB)
  socket.bind(addr)

  override def pub(n: Notification) = {
    debug("publishing notification")
    socket.send(n.toString)
  }

  def end() = {
    socket.close()
    ctx.term()
  }
}

object JeroMqPub {
  def apply(addr: String) = new JeroMqPub(addr)
}