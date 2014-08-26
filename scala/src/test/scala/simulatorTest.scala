package eu.route20.hft.simulator

import eu.route20.hft.test.BaseTest
import eu.route20.hft.common.Notification
import eu.route20.hft.pub.Pub
import org.scalatest._
import eu.route20.hft.simulator._

class SimulatorTest extends BaseTest {
  var invocations = 0

  "Simulator" should "publish notifications" in {
    val c = Config(1, 1, 1)
    val s = new Simulator with Mock
    s run List(c)
    invocations should be(1)
  }

  trait Mock extends Pub {
    override def pub(n: Notification) = { invocations = invocations + 1 }
  }
}

