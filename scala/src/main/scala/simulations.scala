package eu.route20.hft.app

import eu.route20.hft.simulation._
import eu.route20.hft.publish._
import grizzled.slf4j.Logging

object EmptyTest extends App with Logging with ConfValues {
  info("Run simulations with different configurations")

  val c1 = Config(tenmillion / 10, hundred, pauseForMillionMsgsPerSec)
  val c2 = Config(tenthousand, tenthousand, pauseForTenThousandMsgsPerSec)
  val c3 = Config(tenthousand, hundred * 10, pauseForTenThousandMsgsPerSec)
  val confs = List(c1, c2, c3)

  val sim = new Simulator with DummyPub
  sim run confs
}
