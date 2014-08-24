package eu.route20.hft.nos

object JeroPubSub {
  import org.zeromq.ZMQ;
  val ctx = ZMQ.context(1)
  val pubSocket = ctx.socket(ZMQ.PUB)
  val subSocket = ctx.socket(ZMQ.SUB)

}

