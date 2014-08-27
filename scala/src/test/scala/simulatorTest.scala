package eu.route20.hft.simulator

import eu.route20.hft.test.BaseTest
import eu.route20.hft.common.Notification
import eu.route20.hft.pub.Pub
import org.scalatest._
import eu.route20.hft.simulator._

class SimulatorTest extends BaseTest {
  def mock = new Mock

  "Simulator" should "publish notifications" in {
    val m = mock
    val c = List(Config(1, 1, 1))
    m.start(Simulator.sims(c))
    m.events.size should be(1)
  }

  it should "not send notifications if no confs" in {
    val m = mock
    val c = List()
    m.start(Simulator.sims(c))
    m.events.size should be(0)
  }

  it should "send notifications as conffed value" in {
    val m = mock
    val c = List(Config(3, 1, 1), Config(2, 1, 1))
    m.start(Simulator.sims(c))
    m.events.size should be(3 + 2)
  }

  it should "send notification as conffed length" in {
    val m = mock
    val c = List(Config(3, 5, 1), Config(2, 2, 1))
    m.start(Simulator.sims(c))
    val f = m.events.filter(_.msg.length == 5)
    f.size should be(3)
  }

  class Mock extends Pub {
    var events: List[Notification] = List()

    override def pub(n: Notification) = { events = events :+ n }
  }
}

