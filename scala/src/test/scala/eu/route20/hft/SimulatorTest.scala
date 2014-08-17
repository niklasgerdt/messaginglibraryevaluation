package eu.route20.hft

import eu.route20.hft.publish._
import eu.route20.hft.notification._
import eu.route20.hft.test._
import eu.route20.hft.simulation._
import org.scalatest._
import org.scalamock.FunctionAdapter1

class ConfigurableSimulatorTest extends BaseTest {

  "Simulator" should "invoke publisher" in {
    val m = mock[Publisher]
    val n = Notification("")
    (m.publish _).expects(n)
    val s = new ConfigurableSimulator(m, SimulatorConfig(1, 0))
    s simulate
  }

  it should "invoke publisher per configured notification" in {
    val m = mock[Publisher]
    val n = Notification("")
    val c = SimulatorConfig(100, 0)
    (m.publish _).expects(n).repeat(100)
    val s = new ConfigurableSimulator(m, c)
    s simulate
  }
  
  it should "send notification of pre-configured size" in {
    var msg = ""
    class Mock extends Publisher {
      def publish(n: Notification): Unit = {
        msg = n.msg
      }
    }
    val m = new Mock
    val c = SimulatorConfig(1, 5)
    val s = new ConfigurableSimulator(m, c)
    s.simulate
    msg should have size 5
  }

}