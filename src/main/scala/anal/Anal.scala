package r20.anal

import java.io.File
import grizzled.slf4j.Logging
import scala.io.Source

object AnalyzeRoutingSpeed extends App with StatFunctions with Logging {
  val srcFilename = Const.filepath + args(0)
  val dstFilename = Const.filepath + args(1)
  val srcfile = new File(srcFilename)
  val dstfile = new File(dstFilename)
  assert(srcfile.exists())
  assert(dstfile.exists())
  info("Analyzing simulator speed. Using files : " + srcfile.getAbsolutePath + " and " + dstfile.getAbsolutePath)

  val srcsample = Util.mapFileToRouteInfoList(srcfile)
  val dstsample = Util.mapFileToRouteInfoList(dstfile)

  val none: Option[RouteInfo] = None
  val agg = dstsample.foldLeft((0L, 0L))(aveRoutingf(_, _))
  val ave = agg._1 / (agg._2 - 1)
  val max = dstsample.foldLeft(0L)(maxRoutingf(_, _))
  val min = dstsample.foldLeft(Const.maxNanos)(minRoutingf(_, _))
  val aggStd = dstsample.foldLeft((0L, ave))(stdRoutingf(_, _))
  val std = Math.sqrt(aggStd._2) / (agg._2 - 1)

  info("Average routing delay is: " + ave)
  info("Routing maximum delay is: " + max)
  info("Routing minimum delay is: " + min)
  info("Routing variance is: " + std)

  //  val srcEvents = Util.mapFileToRouteInfoList(srcfile).map(_.id)
  //  val dstEvents = Util.mapFileToLines(dstfile).map(Util.mapToRouteInfo(_)).map(_.id)
  //  info("SRC EVENTS: " + srcEvents.size)
  //  info("DST EVENTS: " + dstEvents.size)
  //  val eventAggregate = srcEvents ++ dstEvents
  //  val distinctedEvents = eventAggregate.distinct.size
  //
  //  val droppedEvents = srcEvents.foldLeft(0)((a, b) => {
  //    val e = srcEvents.find(_ == b)
  //    if (e.isDefined)
  //      a
  //    else
  //      a + 1
  //  }
  //  )
  //  info("Events dropped: " + droppedEvents)

}

object AnalyzeSimulatorSpeed extends App with StatFunctions with Logging {
  val filename = Const.filepath + args(0)
  val file = new File(filename)
  assert(file.exists())
  info("Analyzing simulator speed. Using file : " + file.getAbsolutePath)
  val sample = Util.mapFileToRouteInfoList(file)

  val none: Option[RouteInfo] = None
  val agg = sample.foldLeft((none, 0L, 0L))(avef(_, _))
  val ave = agg._2 / (agg._3 - 1)
  val max = sample.foldLeft((none, 0L))(maxf(_, _))._2
  val min = sample.foldLeft((none, Const.maxNanos))(minf(_, _))._2
  val aggStd = sample.foldLeft((none, 0L, ave))(stdf(_, _))
  val std = Math.sqrt(aggStd._2 / (agg._3 - 1))

  info("Simulator average delay is: " + ave)
  info("Simulator maximum delay is: " + max)
  info("Simulator minimum delay is: " + min)
  info("Simulator variance is: " + std)
}

case class RouteInfo(src: String, dst: String, id: Long, prepub: Stamp, postpub: Stamp, routed: Stamp)

case class Stamp(sec: Long, nano: Long) {
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

  def mapFileToRouteInfoList(file: File) =
    mapFileToLines(file).
      drop(Const.drop).
      take(Const.take).
      map(Util.mapToRouteInfo(_)).
      toList

  def mapFileToLines(file: File) =
    Source.fromFile(file).
      getLines()
}

trait StatFunctions {

  def avef(a: (Option[RouteInfo], Long, Long), cur: RouteInfo): (Option[RouteInfo], Long, Long) = {
    if (a._1.isEmpty)
      (Some(cur), 0, 0)
    else {
      val prev = a._1.get
      val elapsed = cur.prepub - prev.postpub
      (Some(cur), a._2 + elapsed.nano, a._3 + 1)
    }
  }

  def maxf(a: (Option[RouteInfo], Long), cur: RouteInfo): (Option[RouteInfo], Long) = {
    if (a._1.isEmpty)
      (Some(cur), 0)
    else {
      val prev = a._1.get
      val elapsed = cur.prepub - prev.postpub
      if (elapsed.nano > a._2)
        (Some(cur), elapsed.nano)
      else
        (Some(cur), a._2)
    }
  }

  def minf(a: (Option[RouteInfo], Long), cur: RouteInfo): (Option[RouteInfo], Long) = {
    if (a._1.isEmpty)
      (Some(cur), Const.maxNanos)
    else {
      val prev = a._1.get
      val elapsed = cur.prepub - prev.postpub
      if (elapsed.nano < a._2)
        (Some(cur), elapsed.nano)
      else
        (Some(cur), a._2)
    }
  }

  def stdf(a: (Option[RouteInfo], Long, Long), cur: RouteInfo): (Option[RouteInfo], Long, Long) = {
    if (a._1.isEmpty)
      (Some(cur), 0, a._3)
    else {
      val prev = a._1.get
      val elapsed = cur.prepub - prev.postpub
      val s = elapsed.nano - a._3
      val ss = s * s
      if (a._2 + ss < a._2) {
        println("overflow " + a._2 + " : " + ss)
      }
      (Some(cur), a._2 + ss, a._3)
    }
  }

  def aveRoutingf(a: (Long, Long), cur: RouteInfo): (Long, Long) = {
    val elapsed = cur.routed - cur.prepub
    (a._1 + elapsed.nano, a._2 + 1)
  }

  def maxRoutingf(a: Long, cur: RouteInfo): Long = {
    val elapsed = cur.routed - cur.prepub
    if (elapsed.nano > a)
      elapsed.nano
    else
      a
  }

  def minRoutingf(a: Long, cur: RouteInfo): Long = {
    val elapsed = cur.routed - cur.prepub
    if (elapsed.nano < a)
      elapsed.nano
    else
      a
  }

  def stdRoutingf(a: (Long, Long), cur: RouteInfo): (Long, Long) = {
    val elapsed = cur.routed - cur.prepub
    val s = elapsed.nano - a._2
    val ss = s * s
    (a._1 + ss, a._2)
  }
}


object Const {
  val filepath = "/media/devel/repos/MOM4HFT/logs/"
  val maxNanos = 999999999L
  val drop = 1000000
  val take = 1000000
}