package eu.route20.hft.sub

import grizzled.slf4j.Logging
import org.zeromq.ZMQ

import scala.annotation.tailrec

object JeroMqSub extends App with Logging {
  val ctx = ZMQ.context(1)
  val sub = ctx.socket(ZMQ.SUB)

  info("starting up JeroMQ subscriber")
  sub.connect("tcp://localhost:5901")
  sub.subscribe("".getBytes)
  receive()
  sub.close()
  ctx.term()
  info("JeroMQ down")
  info(EventContainer.eventCount)
  info("First :" + EventContainer.events.head)
  info("Last: " + EventContainer.events.tail)


  @tailrec final def receive(): Unit = {
    val msg = sub.recvStr()
    debug(msg)
    EventContainer.addEvent(msg)
    receive()
  }
}

object EventContainer {
  case class Event(e: String)
  var events: List[Event] = List()
  var eventCount = 0L

  def addEvent(e: String) = {
    eventCount = eventCount + 1
    events = events :+ Event(e)
  }
}