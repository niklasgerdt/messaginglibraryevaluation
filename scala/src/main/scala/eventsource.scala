package eu.route20.hft.eventsource

import eu.route20.hft.common.{Util, Notification, Header}
import grizzled.slf4j.Logging
import scala.annotation.tailrec
import scala.util.Random

sealed abstract class Config

case class SimulatorConfig(notifications: Int, notificationLength: Int, pauseTime: Long) extends Config

trait Mapper {
  type EventSource = () => Unit

  def map(c: Config, pub: Notification => Unit, kill: () => Boolean): EventSource
}

trait SimulatorMapper extends Mapper with Logging {

  override def map(c: Config, pub: Notification => Unit, kill: () => Boolean): EventSource = {
    def map(sc: SimulatorConfig): EventSource = {
      () => {
        @tailrec def simulate(toGo: Int): Unit = {
          if (toGo > 0 && !kill()) {
            val h = Some(Header("", System.nanoTime, 0L))
            val n = Notification(h, msg)
            debug(n)
            pub(n)
            Util.pause(System.nanoTime, sc.pauseTime)
            simulate(toGo - 1)
          }
        }

        info("simulating " + sc.notifications + " msgs, " + sc.notificationLength + " len, " + sc.pauseTime + " pause")
        lazy val msg = Random.nextString(sc.notificationLength)
        simulate(sc.notifications)
        info("done simulating")
      }
    }

    c match {
      case sc: SimulatorConfig => map(sc)
    }
  }
}