package eu.route20.hft.simulator

import eu.route20.hft.eventsource.SimulatorConfig
import eu.route20.hft.pub.Pub
import eu.route20.hft.common._
import grizzled.slf4j.Logging
import scala.util.Random

object Simulator extends Logging {

  def sims(confs: List[SimulatorConfig]): List[(Notification => Unit) => Unit] = {

    def toSim(c: SimulatorConfig): (Notification => Unit) => Unit = {
      (pub: Notification => Unit) => {
        def sendAndPause(): Unit = {
          def pause(): Unit = {
            val nanoTime = System.nanoTime
            Stream.continually(System.nanoTime())
              .takeWhile(_ < (nanoTime + c.pauseTime))
              .force
          }
          debug("msg: " + msg)
          val h = Some(Header("", System.nanoTime, 0L))
          pub(Notification(h, msg))
          pause
        }
        lazy val msg = Random.nextString(c.notificationLength.toInt)
        info("simulating " + c.notifications + " msgs, " + c.notificationLength + " len, " + c.pauseTime + " pause")
        Stream.range(0, c.notifications)
          .foreach(_ => sendAndPause)
        info("done simulating")
      }
    }

    confs.map(toSim(_))
  }
}

