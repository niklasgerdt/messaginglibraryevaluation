package eu.route20.hft.simulation;

import java.util.Random;
import lombok.Setter;
import route20.hft.publisher.Publisher;

public class Simulator {
	@Setter
	private Publisher publisher;
	@Setter
	private int notificationChars;
	@Setter
	private int pauseInNanos;
	@Setter
	private long notifications;

	public void doSimulation() throws InterruptedException {
		Random r = new Random();
		byte[] bytes = new byte[notificationChars];
		for (int i = 0; i < notifications; i++) {
			r.nextBytes(bytes);
			publisher.pub(bytes.toString());
			Thread.sleep(0, pauseInNanos);
		}
	}
}
