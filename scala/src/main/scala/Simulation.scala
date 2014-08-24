package eu.route20.hft.simulation

import eu.route20.hft.notification.Notification
import grizzled.slf4j.Logging

class SimulationRunner(simulators: List[() => Unit]) extends Logging {

  def run(): Unit = {
    info("starting simulations on " + simulators.size + " simulators")
    simulators.par.foreach(s => s())
    info("simulations done")
  }
}

object SimulationRunner {
  def apply(simulators: List[() => Unit]) = new SimulationRunner(simulators)
}

case class SimulatorConfig(notifications: Long, notificationLength: Long, pauseTime: Long)

class ConfigurableSimulator(publisher: Notification => Unit, config: SimulatorConfig) extends Logging {
  import scala.util.Random
  private val random = new Random
  private val msg = random.nextString(config.notificationLength.toInt)
  private val stream = Stream.range(0, config.notifications)
  val sim = simulate _

  private def simulate: Unit = {

    def sendAndPause(): Unit = {
      debug("msg: " + msg)
      publisher(Notification(msg))
      pause
    }

    def pause(): Unit = {
      val nanoTime = System.nanoTime
      while (nanoTime + config.pauseTime >= System.nanoTime) {}
    }

    info("simulating " + config.notifications + " msgs, " + config.notificationLength + " len, " + config.pauseTime + " pause")
    stream.foreach(_ => sendAndPause)
    info("done simulating")
  }
}

object ConfigurableSimulator {
  def apply(publisher: Notification => Unit, config: SimulatorConfig) = new ConfigurableSimulator(publisher, config)
}
