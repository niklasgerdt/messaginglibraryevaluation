
package eu.route20.hft

import eu.route20.hft.test._
import org.scalatest._
import eu.route20.hft.simulation._

class SimulationRunnerTest extends BaseTest {

  "SimulationRunner" should "invoke simulator" in {
    val simulatorMock = mock[() => {}]
    val simulators = List(simulatorMock)
    (simulatorMock() _).expects
    val simulationRunner = new SimulationRunner(simulators)
    simulationRunner run
  }

  it should "invoke all configured simulators" in {
    val m1 = mock[() => {}]
    val m2 = mock[() => {}]
    val simulators = List(m1, m2)
    (m1() _).expects
    (m2() _).expects
    val runner = SimulationRunner(simulators)
    runner run
  }
}
