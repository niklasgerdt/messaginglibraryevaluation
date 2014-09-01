
def f(): () => Long = () => System.nanoTime

val a = f()
val b = f()

val c = 5 < 6



val ret = for (x <- f()() until Int.MaxValue) {}

val ret2 = for {z <- 1 to 10} yield z

def sleep(i: Int): Unit = {
  println(i)
  if (i < 5)
    sleep(i + 1)
}

sleep(0)
val o: Option[String] = Some("temp.scala.sc")
val s: Option[String] = None
def ev(a: Option[String]) =
s match {
  case Some(s) => println("-"+s)
  case None => println(o)
}
ev(o)
ev(s)


o.getOrElse("SS")
s.getOrElse("SS")
o.isDefined
s.isDefined

