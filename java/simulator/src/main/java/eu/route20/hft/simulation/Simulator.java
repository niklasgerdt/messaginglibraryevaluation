package eu.route20.hft.simulation;

import org.springframework.beans.factory.annotation.Autowired;
import route20.hft.publisher.Publisher;

public class Simulator {
	@Autowired
	private Publisher publisher;
	@Autowired
	private int notificationChars;
	@Autowired
	private long pauseInNanos;
	private Long notifications;

	public void setNotifications(Long notifications) {
		this.notifications = notifications;
	}

	public void doSimulation() {
		for (int i = 0; i < notifications; i++)
			publisher.pub("notification " + i);
	}
}
