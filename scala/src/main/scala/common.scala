package eu.route20.hft.common

import scala.annotation.tailrec

case class Notification(header: Option[Header], body: String)

case class Header(id: String, createdNano: Long, routedNano: Long)

object Common {

  @tailrec def pause(nanoTime: Long, pauseTime: Long): Unit = {
    if (System.nanoTime < nanoTime + pauseTime)
      pause(nanoTime, pauseTime)
  }
}

trait ConfValues {
  val tenmillion = 10000000
  val tenthousand = 10000
  val hundred = 100
  val pauseForMillionMsgsPerSec = 1000L
  val pauseForTenThousandMsgsPerSec = 100000L
  val nil = 0L
}
