package eu.route20.hft.eventsource

import eu.route20.hft.common.{Util, Notification, Header}
import grizzled.slf4j.Logging
import scala.annotation.tailrec
import scala.util.Random

sealed abstract class Config

case class SimulatorConfig(notifications: Int, notificationLength: Int, pauseTime: Long) extends Config

trait Runner extends Logging {

  @tailrec final def run(es: () => Option[Notification], pub: Notification => Unit): Unit = {
    es() match {
      case Some(n) => pub(n); run(es, pub);
      case None =>
    }
  }
}

trait Mapper {
  type EventSource = () => Option[Notification]

  def map(c: Config): EventSource
}

trait SimulatorMapper extends Mapper with Logging {

  override def map(c: Config): EventSource = {
    c match {
      case sc: SimulatorConfig => map(sc)
    }
  }

  def map(sc: SimulatorConfig): EventSource = {
    lazy val msg = Random.nextString(sc.notificationLength)
    var notifications = sc.notifications
    () => {
      Util.pause(System.nanoTime, sc.pauseTime)
      notifications = notifications - 1
      if (notifications > 0) {
        val h = Some(Header("", System.nanoTime, 0L))
        val n = Notification(h, msg)
        debug(n)
        Some(n)
      }
      else None
    }
  }
}