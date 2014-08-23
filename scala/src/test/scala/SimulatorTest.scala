
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
    val c = SimulatorConfig(1, 0, 0)
    val n = Notification("")
    (m.publish _).expects(n)
    val s = new ConfigurableSimulator(m, c)
    s simulate
  }

  it should "invoke publisher per configured notification" in {
    val m = mock[Publisher]
    val n = Notification("")
    val c = SimulatorConfig(100, 0, 0)
    (m.publish _).expects(n).repeat(100)
    val s = new ConfigurableSimulator(m, c)
    s simulate
  }

  it should "send notification of pre-configured size" in {
    val m = new Mock
    val c = SimulatorConfig(1, 5, 0)
    val s = new ConfigurableSimulator(m, c)
    s.simulate
    m.msg should have size 5
  }

  it should "pause between notifications, if pause enabled" in {
    val timeBetweenNotifications = 16000l
    val pauseTime = timeBetweenNotifications * 100
    val c = SimulatorConfig(2, 0, pauseTime)
    val m = new TimingMock
    val s = new ConfigurableSimulator(m, c)
    s.simulate
    m.timeBetweenMessages shouldBe (pauseTime + timeBetweenNotifications) +- (pauseTime / 10)
  }
}

class Mock extends Publisher {
  var msg = ""
  def publish(n: Notification): Unit = {
    msg = n.msg
  }
}

class TimingMock extends Publisher {
  var timeBetweenMessages = 0l
  var lastMessage = 0l

  def publish(n: Notification) {
    val msgTime = System.nanoTime
    timeBetweenMessages = msgTime - lastMessage
    lastMessage = msgTime
  }
}
