package eu.route20.hft.common

import scala.annotation.tailrec

case class Notification(header: Option[Header], body: String)

case class Header(id: String, createdNano: Long, routedNano: Long)

object Util {

  @tailrec def pause(nanoTime: Long, pauseTime: Long): Unit = {
    if (System.nanoTime < nanoTime + pauseTime)
      pause(nanoTime, pauseTime)
  }
}
