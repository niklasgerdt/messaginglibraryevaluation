package spike {

  import org.scalatest.FlatSpec
  import org.scalamock.scalatest.MockFactory
  import tr._

  //Spiking for Scalatest & Scalamock integration
  class SpikeTest extends FlatSpec with MockFactory {

    "spike" should "do" in {
      val m = mockFunction[Int, Int]
      m expects (199)
      m(199)
    }

    "t" should "f" in {
      val m = mock[TR]
      (m.f _)expects
      val o = new O(m)
      o.f
    }
  }
}

package tr {
  trait TR {
    def f(): Unit
  }

  class O(tr: TR) {
    def f() = tr.f
  }
}