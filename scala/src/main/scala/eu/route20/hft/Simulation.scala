package eu.route20.hft.simulation

import eu.route20.hft.publish._
import eu.route20.hft.notification._
import grizzled.slf4j.Logging
import scala.util.Random

trait Simulator {
  def simulate(): Unit
}

case class SimulatorConfig(notifications: Int, notificationLength: Int, pauseTime: Long)

class SimulationRunner(simulators: List[Simulator]) {
  def run(): Unit = {
    simulators.par.foreach((s: Simulator) => s.simulate())
  }
}

class ConfigurableSimulator(publisher: Publisher, config: SimulatorConfig) extends Simulator with Logging {
  def simulate(): Unit = {
    info("simulating " + config.notifications + " notifications")
    val random = new Random()
    val msg = random.nextString(config.notificationLength)
    val stream = Stream.range(0, config.notifications)
    stream.foreach(_ => send())
    info("done simulating")

    def send() = {
      debug("msg: " + msg)
      publisher.publish(Notification(msg))
      pause
    }

    def pause() = {
      val nanoTime = System.nanoTime
      while (nanoTime + config.pauseTime >= System.nanoTime) {}
    }
  }
}
