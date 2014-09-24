
val a = "A"

def b() = "B"

val s = Stream.continually(b)

val ss = s.take(3)

ss.foreach(println(_))


val r = 1 to 3


for (a <- r) println(a)



