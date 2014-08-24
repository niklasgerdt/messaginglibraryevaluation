package eu.route20.hft.simulation

import eu.route20.hft.publish._
import eu.route20.hft.simulation._
import eu.route20.hft.notification.Notification
import eu.route20.hft.config.Config
import grizzled.slf4j.Logging

object SimulatorApp extends App with Logging with Config {
  info("Run simulations with different configurations")

  def pub(n: Notification) = {}

  val c1 = SimulatorConfig(tenmillion / 10, hundred, pauseForMillionMsgsPerSec)
  val s1 = new ConfigurableSimulator(pub, c1).sim

  val c2 = SimulatorConfig(tenthousand, tenthousand, pauseForTenThousandMsgsPerSec)
  val s2 = new ConfigurableSimulator(pub, c2).sim

  val c3 = SimulatorConfig(tenthousand, hundred * 10, pauseForTenThousandMsgsPerSec)
  val s3 = new ConfigurableSimulator(pub, c3).sim

  val simulators = List(s1, s2, s3)
  //  val simulators = List(s1)
  // val simulators = List(s2)
  // val simulators = List(s3)

  val runner = new SimulationRunner(simulators)
  runner run
}
