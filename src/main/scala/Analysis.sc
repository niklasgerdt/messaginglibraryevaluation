import java.io.File

import r20.anal.{RouteInfo, Stamp, Util}

import scala.io.Source

val drop = 1000000
val take = 10
val filename = "/media/devel/repos/MOM4HFT/src/main/resources/EVENTSTORE-PUB-D"
val f = new File(filename)

println(f.getAbsolutePath)
println(f.exists())

val sample = Source.fromFile(f).
  getLines().
  drop(drop).
  take(take).
  map(Util.mapToRouteInfo(_))



def d(a: (Stamp, Long, Int), r: RouteInfo): (Stamp, Long, Int) = {
  val time = r.prepub.nano - a._1.nano
  if (time < 0)
    a
  else
    (r.postpub, time, a._3 + 1)
}

def diff(t1: Stamp, t2: Stamp): Long = (t1 - t2).nano

def dif(t1: RouteInfo, t2: RouteInfo) = t1

val diffs = sample.foldRight(0L)((a, b) => diff(a.prepub, a.postpub))


def difff(l: Iterator[RouteInfo], sum: Long, initialSize: Int): Long = {
  val s = (l.next().prepub - l.().postpub).nano + sum
  if (l.hasNext)
    difff(l, s, initialSize)
  else
    s / initialSize
}

val ka = difff(sample, 0, sample.size)


//val rs = sample.next()
//val r = Util.mapToRouteInfo(rs)
//val rs2 = sample.next()
//val r2 = Util.mapToRouteInfo(rs2)

val s1 = sample.next()
val s2 = sample.next()
val t = s2.prepub - s1.postpub


//  map(str => RouteInfo(str)).
//  foreach(r => println(r))


//  .
//  map(RouteInfo(_))


def to(s: String) = {

}

val str = "AA;BB"
val ss = str.split(";")



val sl = sample.toList

val kaa  = sl.foldLeft((sl.head.postpub, 0L, 0))(d(_, _))
