package eu.route20.hft.simulation;

import java.util.*;
import lombok.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.context.annotation.*;
import org.springframework.stereotype.*;
import eu.route20.hft.publisher.*;

@Component @Scope("prototype") @Qualifier("simulator") public class Simulator implements Runnable {
	@Autowired private Publisher publisher;
	@Setter private int notificationLengthInBytes;
	@Setter private int pauseInMillisBetweenNotifications;
	@Setter private int pauseInNanosBetweenNotifications;
	@Setter private int notifications;

	public void doSimulation() {
		System.out.println("do sim");
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
