package eu.route20.hft.simulation

import eu.route20.hft.publish._
import eu.route20.hft.notification._
import grizzled.slf4j.Logging
import scala.util.Random

trait Simulator {
  def simulate(): Unit
}

case class SimulatorConfig(notifications: Int, notificationLength: Int)

class SimulationRunner(simulators: List[Simulator]) {
  def run(): Unit = {
    simulators.par.foreach((s: Simulator) => s.simulate())
  }
}

class ConfigurableSimulator(publisher: Publisher, config: SimulatorConfig) extends Simulator with Logging {
  def simulate(): Unit = {
    info("simulating " + config.notifications + " notifications")
    val random = new Random()
    val stream = Stream.range(0, config.notifications)
    stream.foreach(_ => send())
    info("done simulating")

    def send() = {
      val msg = random.nextString(config.notificationLength)
      debug("msg: " + msg)
      publisher.publish(Notification(msg))
    }
  }
}