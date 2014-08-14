package eu.route20.hft.simulation

import eu.route20.hft.publish._
import eu.route20.hft.notification._
import eu.route20.test._
import org.scalatest._

class ConfigurableSimulatorTest extends BaseTest {

  "Simulator" should "invoke publisher" in {
    val m = mock[Publisher]
    val n = Notification()
    (m.publish _).expects(n)
    val s = new ConfigurableSimulator(m, SimulatorConfig(1, 0))
    s simulate
  }

  it should "invoke publisher per configured notification" in {
    val m = mock[Publisher]
    val n = Notification()
    val c = SimulatorConfig(100, 0)
    (m.publish _).expects(n).repeat(100)
    val s = new ConfigurableSimulator(m, c)
    s simulate
  }

}