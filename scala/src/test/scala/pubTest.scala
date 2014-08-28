package eu.route20.hft.pub

import eu.route20.hft.common.Notification
import eu.route20.hft.test.BaseTest
import org.scalatest._

class DummyPubTest extends BaseTest {
  class Mock

  "DummyPub" should "do nothing" in {
    val m = new Mock with DummyPub
    m.pub(Notification(None, ""))
  }
}

