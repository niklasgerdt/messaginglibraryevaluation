package eu.route20.hft.simulator

import eu.route20.hft.test.BaseTest
import eu.route20.hft.common.Notification
import eu.route20.hft.pub.Pub
import org.scalatest._
import eu.route20.hft.simulator._

class SimulatorTest extends BaseTest {

  "Simulator" should "publish notifications" in {
    val c = Config(1, 1, 1)
    val s = new Simulator with Mock
    s run List(c)
    s.invocations should be(1)
  }

  it should "send notifications if no confs" in {
    val s = new Simulator with Mock
    s run List()
    s.invocations should be(0)
  }

  it should "send notifications as conffed value" in {
    val s = new Simulator with Mock
    val c1 = Config(3, 1, 1)
    val c2 = Config(2, 1, 1)
    s run List(c1, c2)
    s.invocations should be(3 + 2)
  }

  trait Mock extends Pub {
    var invocations = 0

    override def pub(n: Notification) = { invocations = invocations + 1 }
  }
}

