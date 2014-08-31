package eu.route20.hft.eventsource

import eu.route20.hft.common.{Common, Notification, Header}
import eu.route20.hft.simulations.StreamSimulation
import eu.route20.hft.simulations.StreamSimulation._
import grizzled.slf4j.Logging
import scala.annotation.tailrec
import scala.util.Random

sealed abstract class Config

case class SimulatorConfig(notifications: Int, notificationLength: Int, pauseTime: Long) extends Config

object Runner extends Logging {

  @tailrec def run(es: StreamSimulation.EventSource): Unit = {
    val n = es()
    if (n.isDefined) {
      pub(n.get)
      run(es)
    }
  }
}

trait Mapper {
  type EventSource = () => Option[Notification]

  def map(c: Config): EventSource
}

trait SimulatorMapper extends Mapper {

  override def map(c: Config): EventSource = {
    c match {
      case sc: SimulatorConfig => map(sc)
    }
  }

  def map(sc: SimulatorConfig): EventSource = {
    lazy val msg = Random.nextString(sc.notificationLength)
    var notifications = sc.notifications
    () => {
      Common.pause(System.nanoTime, sc.pauseTime)
      notifications = notifications - 1
      if (notifications > 0) {
        val h = Some(Header("", System.nanoTime, 0L))
        Some(Notification(h, msg))
      }
      else None
    }
  }
}