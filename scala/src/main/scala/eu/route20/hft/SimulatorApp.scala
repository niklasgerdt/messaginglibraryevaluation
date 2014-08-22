package eu.route20.hft.simulation
import eu.route20.hft.publish._
import eu.route20.hft.simulation._

object SimulatorApp extends App {
  println("Run simulations with different configurations")
  val tenmillion = 10000000l
  val tenthousand = 10000l
  val hundred = 100l
  val pauseForMillionMsgsPerSec = 1000l
  val pauseForTenThousandMsgsPerSec = 100000l
  val nil = 0l

  val pub = new DummyPublisher
  val c1 = SimulatorConfig(tenmillion / 10, hundred, pauseForMillionMsgsPerSec)
  val s1 = new ConfigurableSimulator(pub, c1)

  val c2 = SimulatorConfig(tenthousand, tenthousand, pauseForTenThousandMsgsPerSec)
  val s2 = new ConfigurableSimulator(pub, c2)

  val c3 = SimulatorConfig(tenthousand, hundred * 10, pauseForTenThousandMsgsPerSec)
  val s3 = new ConfigurableSimulator(pub, c3)

  val simulators = List(s1, s2, s3)
  //  val simulators = List(s1)
  // val simulators = List(s2)
  // val simulators = List(s3)

  val runner = new SimulationRunner(simulators)
  runner.run
}

