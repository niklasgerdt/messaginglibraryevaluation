
package eu.route20.hft

import eu.route20.hft.test._
import org.scalatest._
import eu.route20.hft.simulation._

class SimulationRunnerTest extends BaseTest {

  "SimulationRunner" should "invoke simulator" in {
    val simulatorMock = mockFunction[Unit, Unit]
    def f = { simulatorMock() }
    val simulators = List(f _)
    simulatorMock expects ()
    val simulationRunner = new SimulationRunner(simulators)
    simulationRunner run
  }

  it should "invoke all configured simulators" in {
    val m1 = mockFunction[Unit, Unit]
    val m2 = mockFunction[Unit, Unit]
    def f1 = { m1() }
    def f2 = { m2() }
    val simulators = List(f1 _, f2 _)
    m1 expects ()
    m2 expects ()
    val runner = SimulationRunner(simulators)
    runner run
  }
}
