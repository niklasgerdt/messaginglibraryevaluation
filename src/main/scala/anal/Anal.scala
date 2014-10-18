package r20.anal

import java.io.File
import grizzled.slf4j.Logging
import scala.io.Source

object AnalyzeSimulatorSpeed extends App with Logging {
  val drop = 1000000
  val take = 1000000
  val filename = "/media/devel/repos/MOM4HFT/src/main/resources/EVENTSTORE-PUB-D"
  //  val filename = args(0)
  val file = new File(filename)

  assert(file.exists())
  info("Analyzing simulator speed. Using file : " + file.getAbsolutePath)

  val sample = Source.fromFile(file).
    getLines().
    drop(drop).
    take(take).
    map(Util.mapToRouteInfo(_)).
    toList

  val agg = sample.foldLeft((sample.head.postpub, 0L, 0L))(diff(_, _))
  val ave = agg._2 / agg._3
  val max = sample.foldLeft((sample.head.postpub, 0L))(maxf(_, _))._2
  val min = sample.foldLeft((sample.head.postpub, 0L))(minf(_, _))._2

  info("Simulator average delay is: " + ave)
  info("Simulator maximum delay is: " + max)
  info("Simulator minimum delay is: " + min)

  def diff(a: (Stamp, Long, Long), r: RouteInfo): (Stamp, Long, Long) = {
    val time = r.prepub.nano - a._1.nano
    if (time < 0)
      a
    else
      (r.postpub, a._2 + time, a._3 + 1)
  }

  def maxf(a: (Stamp, Long), r: RouteInfo): (Stamp, Long) = {
    val time = r.prepub.nano - a._1.nano
    if (time < 0)
      a
    if (time < a._2)
      (r.postpub, a._2)
    else
      (r.postpub, time)
  }

  def minf(a: (Stamp, Long), r: RouteInfo): (Stamp, Long) = {
    val time = r.prepub.nano - a._1.nano
    if (time < 0)
      a
    if (time > a._2)
      (r.postpub, a._2)
    else
      (r.postpub, time)
  }
}

case class RouteInfo(src: String, dst: String, id: Long, prepub: Stamp, postpub: Stamp, routed: Stamp)

case class Stamp(sec: Long, nano: Long) {
  val maxNanos = 999999999L
  val SECL = 1000000000L

  def -(t: Stamp): Stamp = {
    val n = nano - t.nano
    val s = sec - t.sec
    if (n < 0L)
      Stamp(s - 1, SECL + n)
    else
      Stamp(s, n)
  }
}

object Util {
  val reg = "\\d{1,9}".r
  val stampReg = "\\d{1,9}".r


  def isNum(str: String) = reg.findFirstIn(str).isDefined

  def mapToRouteInfo(str: String): RouteInfo = {
    val tokens = str.split(";")
    assert(tokens.length == 6)
    assert(Util.isNum(tokens(2)))
    val pre = mapToStamp(tokens(3))
    val post = mapToStamp(tokens(4))
    val routed = mapToStamp(tokens(5))
    RouteInfo(tokens(0), tokens(1), tokens(2).toLong, pre, post, routed)
  }


  def mapToStamp(str: String): Stamp = {
    val tokens = str.split("\\.")
    assert(tokens.length == 2)
    val sec = stampReg.findFirstIn(tokens(0))
    val nano = stampReg.findFirstIn(tokens(1))
    assert(sec.isDefined && nano.isDefined)
    val s = sec.get.toLong
    val n = nano.get.toLong
    Stamp(s, n)
  }

}