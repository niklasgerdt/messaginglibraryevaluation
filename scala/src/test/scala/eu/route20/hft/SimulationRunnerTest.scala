
package eu.route20.hft

import eu.route20.hft.test._
import org.scalatest._
import eu.route20.hft.simulation._

class SimulationRunnerTest extends BaseTest {

  "SimulationRunner" should "invoke simulator" in {
    val simulatorMock = mock[Simulator]
    val simulators = List(simulatorMock)
    (simulatorMock.simulate _).expects
    val simulationRunner = new SimulationRunner(simulators)
    simulationRunner run
  }

  it should "invoke all configured simulators" in{
  	val m1 = mock[Simulator]
  	val m2 = mock[Simulator]
  	val simulators = List(m1, m2)
  	(m1.simulate _).expects
  	(m2.simulate _).expects
  	val runner = new SimulationRunner(simulators)
  	runner run
  }
}