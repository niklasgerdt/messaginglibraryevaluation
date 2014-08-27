package eu.route20.hft.simulations

import eu.route20.hft.simulator._
import eu.route20.hft.pub._
import grizzled.slf4j.Logging

object EmptySimulation extends App with Logging with ConfValues with Pub with DummyPub {
  info("Run simulations with different configurations")

  val c1 = Config(tenmillion / 10, hundred, pauseForMillionMsgsPerSec)
  val c2 = Config(tenthousand, tenthousand, pauseForTenThousandMsgsPerSec)
  val c3 = Config(tenthousand, hundred * 10, pauseForTenThousandMsgsPerSec)
  val confs = List(c1, c2, c3)
  val sims = Simulator.sims(confs)
  start(sims)
}
