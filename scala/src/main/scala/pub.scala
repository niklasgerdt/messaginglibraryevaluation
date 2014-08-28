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

class JeroMqPublisher(address: String) extends Pub with Logging {
  import org.zeromq.ZMQ;
  val ctx = ZMQ.context(1)
  val socket = ctx.socket(ZMQ.PUB)

  override def start(streams: List[(Notification => Unit) => Unit]): Unit = {
    socket.connect(address)
    super.start(streams)
  }

  override def pub(n: Notification): Unit = socket.send(n.body)
}

object JeroMqPublisher {
  def apply(address: String) = new JeroMqPublisher(address)
}
