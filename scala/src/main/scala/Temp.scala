import org.zeromq.ZMQ

object Sub extends App {
  val context = ZMQ.context(1)
  val socket = context.socket(ZMQ.SUB)
  socket.connect("tcp://localhost:5000")
  socket.subscribe("".getBytes)
  run()
  socket.close()
  context.term()

  def run(): Unit = {
    val x = socket.recvStr()
    if (!x.equalsIgnoreCase("x")) {
      println(x)
      run()
    }
  }
}

object Pub extends App{
  val context = ZMQ.context(1)
  val socket = context.socket(ZMQ.PUB)
  socket.bind("tcp://*:5000")
  run()
  socket.close()
  context.term()


  def run(): Unit = {
    val x = Console.in.readLine()
    if (!x.equalsIgnoreCase("x")) {
      socket.send(x)
      run()
    }
  }
}