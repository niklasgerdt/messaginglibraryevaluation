package eu.route20.hft.common

case class Notification(header: Option[Header], body: String)
case class Header(id: String, createdNano: Long, routedNano: Long)
