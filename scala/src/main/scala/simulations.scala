package eu.route20.hft.simulations

import eu.route20.hft.app.NeverKill
import eu.route20.hft.simulator.{SimConfValues, SimulatorRunner, SimulatorConfig, SimulatorMapper}
import eu.route20.hft.pub._
import grizzled.slf4j.Logging

object DummySimulation extends App with Logging with SimConfValues with DummyPub with NeverKill with SimulatorMapper with SimulatorRunner {
  info("Run 3 simulations with different configurations notification sizes and counts. Duration of simulation ~ 1s.")
  runParallel(
    List(SimulatorConfig(tenmillion / 10, hundred, pauseForMillionMsgsPerSec, pub, kill),
      SimulatorConfig(tenthousand, tenthousand, pauseForTenThousandMsgsPerSec, pub, kill),
      SimulatorConfig(tenthousand, hundred * 10, pauseForTenThousandMsgsPerSec, pub, kill)
    ).map(map(_))
  )
}

object EndlessDummySimulation extends App with Logging with SimConfValues with DummyPub with NeverKill with SimulatorMapper with SimulatorRunner {
  info("Run 3 simulations with different configurations notification sizes and counts. Infinite simulation.")
  runParallel(
    List(SimulatorConfig(ENDLESS, hundred, pauseForMillionMsgsPerSec, pub, kill),
      SimulatorConfig(ENDLESS, tenthousand, pauseForTenThousandMsgsPerSec, pub, kill),
      SimulatorConfig(ENDLESS, hundred * 10, pauseForTenThousandMsgsPerSec, pub, kill)
    ).map(map(_))
  )
}

object JeroMqSimulation extends App with Logging with SimConfValues with NeverKill with SimulatorMapper with SimulatorRunner {
  info("Run simulations with different configurations")
  val pub1 = JeroMqPub("tcp://*:5500")
  val pub2 = JeroMqPub("tcp://*:5501")
  val pub3 = JeroMqPub("tcp://*:5502")
  runParallel(
    List(SimulatorConfig(tenmillion / 10, hundred, pauseForMillionMsgsPerSec, pub1.pub, kill),
      SimulatorConfig(tenthousand, tenthousand, pauseForTenThousandMsgsPerSec, pub2.pub, kill),
      SimulatorConfig(tenthousand, hundred * 10, pauseForTenThousandMsgsPerSec, pub3.pub, kill)
    ).map(map(_))
  )
  pub1.end()
  pub2.end()
  pub3.end()
}

object EndlessJeroMqSimulation extends App with Logging with SimConfValues with NeverKill with SimulatorMapper with SimulatorRunner {
  info("Run simulations with different configurations")
  val pub = JeroMqPub("tcp://*:5500")
  runParallel(
    List(SimulatorConfig(ENDLESS, hundred, pauseForMillionMsgsPerSec, pub.pub, kill))
      .map(map(_))
  )
  pub.end()
}
