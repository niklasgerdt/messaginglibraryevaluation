package eu.route20.hft.pub

import eu.route20.hft.common.Notification
import grizzled.slf4j.Logging

trait Pub extends Logging {
  def pub(n: Notification): Unit

  def start(streams: List[(Notification => Unit) => Unit]): Unit = {
    info("starting publishing on " + streams.size + " streams")
    streams.par.foreach(_(pub))
    info("streams fininished")
  }
}

trait DummyPub extends Pub {
  override def pub(n: Notification): Unit = {}
}

trait LoggingPub extends Pub with Logging {
  override def pub(n: Notification): Unit = info(n)
}

trait JeroMqPublisher extends Pub with Logging {
  import org.zeromq.ZMQ;
  val ctx = ZMQ.context(1)
  val socket = ctx.socket(ZMQ.PUB)

  def apply(address: String): Unit = socket.connect(address)

  override def pub(n: Notification): Unit = socket.send(n.msg)
}
