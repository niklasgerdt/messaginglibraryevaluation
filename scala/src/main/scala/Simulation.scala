package eu.route20.hft.simulation

import eu.route20.hft.publish._
import eu.route20.hft.notification._
import grizzled.slf4j.Logging
import scala.util.Random

trait Simulator {
  def simulate(): Unit
}

case class SimulatorConfig(notifications: Long, notificationLength: Long, pauseTime: Long)

class SimulationRunner(simulators: List[Simulator]) extends Logging {
  def run(): Unit = {
    info("starting simulations on " + simulators.size + " simulators")
    simulators.par.foreach((s: Simulator) => s.simulate())
    info("simulations done")
  }
}

class ConfigurableSimulator(publisher: Publisher, config: SimulatorConfig) extends Simulator with Logging {
  def simulate(): Unit = {
    info("simulating " + config.notifications + " msgs, " + config.notificationLength + " len, " + config.pauseTime + " pause")
    val random = new Random()
    val msg = random.nextString(config.notificationLength.toInt)
    val stream = Stream.range(0, config.notifications)
    stream.foreach(_ => send())
    info("done simulating")

    def send(): Unit = {
      debug("msg: " + msg)
      publisher.publish(Notification(msg))
      pause
    }

    def pause(): Unit = {
      val nanoTime = System.nanoTime
      while (nanoTime + config.pauseTime >= System.nanoTime) {}
    }
  }
}
