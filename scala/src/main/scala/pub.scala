package eu.route20.hft.pub

import eu.route20.hft.common.{Header, Notification}
import grizzled.slf4j.Logging
import org.zeromq.ZMQ

trait Pub {
  def pub(n: Notification): Unit
}

trait DummyPub extends Pub {
  override def pub(n: Notification): Unit = {}
}

trait LoggingPub extends Pub with Logging {
  override def pub(n: Notification): Unit = info(n)
}

object LocalPub extends Pub with Logging {
  var events: List[Notification] = List()
  var eventCount = 0L
  val MILLION: Int = 1000000

  override def pub(n: Notification) = {
    eventCount = eventCount + 1
    val e = Notification(Header("", n.header.createdNano, System.nanoTime()), "")
    events = events :+ e
    if (events.size == 10000) {
      asyncLog(events)
      events = List()
    }
  }

  def asyncLog(e: List[Notification]): Unit = {
    new Thread(new Runnable {
      override def run(): Unit = for (n <- e) info(n)
    }).start()
  }
}

class JeroMqPub private(addr: String) extends Pub with Logging {
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

object SimpleJeroMqPub extends App {
  val ctx = ZMQ.context(1)
  val pub = ctx.socket(ZMQ.PUB)
  pub.bind("tcp://169.254.5.233:5500")

  println("send event (Max 10 events, x/X terminates)")
  run(10)

  pub.close()
  ctx.term()

  def run(i: Int): Unit = {
    if (i > 0) {
      val x = Console.in.readLine()
      pub.send(x)
      if (!x.equalsIgnoreCase("x")) {
        run(i - 1)
      }
    }
  }
}