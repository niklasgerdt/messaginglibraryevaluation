package eu.route20.hft.publish

import eu.route20.hft.notification.Notification

trait Publisher {
	def publish(notification: Notification): Unit
}

trait LoggingPublisher extends Publisher{
	
	def publish(notification: Notification): Unit = {
		println(notification)
	}
}