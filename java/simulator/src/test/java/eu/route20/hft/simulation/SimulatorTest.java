package eu.route20.hft.simulation;

import static org.junit.Assert.assertEquals;
import java.util.ArrayList;
import java.util.List;
import lombok.val;
import org.junit.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import eu.route20.hft.publisher.*;
import eu.route20.hft.simulation.Simulator;

public class SimulatorTest {
	@InjectMocks private Simulator simulator;
	@Mock private Publisher pub;
	private List<byte[]> notifications;

	@Before public void setup() {
		simulator = new Simulator();
		MockitoAnnotations.initMocks(this);
	}

	@Test public void sendsNotificationToPublisher() throws InterruptedException {
		simulator.setNotifications(10);
		simulator.doSimulation();
		Mockito.verify(pub, Mockito.times(10)).pub(Mockito.any());
	}

	@Test public void sendsRightAmountOfNotifications() throws InterruptedException {
		simulator.setNotifications(1000);
		simulator.doSimulation();
		Mockito.verify(pub, Mockito.times(1000)).pub(Mockito.any());
	}

	@Ignore @Test public void pausesBetweenNotification() throws InterruptedException {
		int pauseInMillis = 100;
		simulator.setPauseInMillisBetweenNotifications(pauseInMillis);
		simulator.setPauseInNanosBetweenNotifications(1);
		simulator.setNotifications(2);
		val t1 = System.currentTimeMillis();
		simulator.doSimulation();
		val t2 = System.currentTimeMillis();
		assertEquals(2 * pauseInMillis, t2 - t1, 2 * (pauseInMillis / 10));
	}

	// @Test(expected = AssertionError.class)
	// public void randomMsgs() throws InterruptedException {
	// int amountOfnotifications = 1000;
	// notifications = new ArrayList<byte[]>();
	// //TODO Use Java 8 and lambdas
	// simulator.setPublisher(new Publisher() {
	// @Override
	// public void pub(byte[] notification) {
	// notifications.add(notification);
	// }
	// });
	// simulator.setNotificationLengthInBytes(amountOfnotifications);
	// simulator.setNotifications(2);
	// simulator.doSimulation();
	// int i = 0;
	// for (i = 0; i < amountOfnotifications; i++)
	// assertEquals(notifications.get(0)[i], notifications.get(1)[i]);
	// }
}
