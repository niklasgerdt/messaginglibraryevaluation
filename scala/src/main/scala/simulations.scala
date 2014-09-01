package eu.route20.hft.simulations

import eu.route20.hft.app.NeverKill
import eu.route20.hft.common.{ConfValues, Notification}
import eu.route20.hft.eventsource.{Runner, SimulatorConfig, SimulatorMapper}
import eu.route20.hft.pub._
import grizzled.slf4j.Logging

import scala.annotation.tailrec

object StreamSimulation extends App with Logging with ConfValues with DummyPub with NeverKill with SimulatorMapper {
  info("Run simulations with different configurations")
  val c1 = SimulatorConfig(tenmillion / 10, hundred, pauseForMillionMsgsPerSec)
  val c2 = SimulatorConfig(tenthousand, tenthousand, pauseForTenThousandMsgsPerSec)
  val c3 = SimulatorConfig(tenthousand, hundred * 10, pauseForTenThousandMsgsPerSec)
  val es = List(c1, c2, c3).map(map(_))

  info("running " + es.size + " simulators")
  es.par.foreach(run(_, pub))
  info("simulations done")

  @tailrec def run(es: () => Option[Notification], pub: Notification => Unit): Unit = {
    val n = es()
    if (n.isDefined) {
      pub(n.get)
      run(es, pub)
    }
  }
}