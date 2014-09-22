package eu.route20.hft.simulator

import eu.route20.hft.common._
import grizzled.slf4j.Logging
import scala.annotation.tailrec
import scala.util.Random

case class SimulatorConfig(notifications: Int, notificationLength: Int, pauseTime: Long, pub: Notification => Unit, end: () => Boolean)

trait SimConfValues {
  val ENDLESS = -1
  val tenmillion = 10000000
  val tenthousand = 10000
  val hundred = 100
  val pauseForMillionMsgsPerSec = 1000L
  val pauseForTenThousandMsgsPerSec = 100000L
  val nil = 0L
}

trait SimulatorMapper extends SimConfValues with Logging {

  def map(c: SimulatorConfig): () => Unit = {
    () => {
      @tailrec def simulate(toGo: Int): Unit = {
        if ((c.notifications == ENDLESS || toGo > 0) && !c.end()) {
          val h = Some(Header("", System.nanoTime, 0L))
          val n = Notification(h, msg)
          debug(n)
          c.pub(n)
          Util.pause(System.nanoTime, c.pauseTime)
          simulate(toGo - 1)
        }
      }

      info("simulating " + c.notifications + " msgs, " + c.notificationLength + " len, " + c.pauseTime + " pause")
      lazy val msg = Random.nextString(c.notificationLength)
      simulate(c.notifications)
      info("done simulating")
    }
  }
}

trait SimulatorRunner extends Logging {

  def runParallel(s: List[() => Unit]): Unit = {
    info("running " + s.size + " simulators")
    s.par.foreach(_())
    info("simulations done")
  }
}