package eu.route20.hft.simulation;

import java.util.*;
import lombok.*;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.context.annotation.*;
import org.springframework.stereotype.*;
import eu.route20.hft.publisher.*;

@Component @Scope("prototype") @Qualifier("simulator") public class Simulator implements Runnable {
	final static Logger logger = LoggerFactory.getLogger(Simulator.class);
	@Autowired private Publisher publisher;
	@Setter private int notificationLengthInBytes = 100;
	@Setter private long pauseInMillisBetweenNotifications;
	@Setter private int pauseInNanosBetweenNotifications;
	@Setter private int notifications = 10;

	public void doSimulation() {
		logger.debug("Simulating {} notifications of {} bytes.", notifications, notificationLengthInBytes);
		Random r = new Random();
		for (int i = 0; i < notifications; i++) {
			val bytes = new byte[notificationLengthInBytes];
			r.nextBytes(bytes);
			publisher.pub(bytes);
			pause();
		}
	}

	private void pause() {
		try {
			Thread.sleep(pauseInMillisBetweenNotifications, pauseInNanosBetweenNotifications);
		} catch (InterruptedException e) {
			throw new SimulationException();
		}
	}

	@Override public void run() {
		doSimulation();
	}
}
