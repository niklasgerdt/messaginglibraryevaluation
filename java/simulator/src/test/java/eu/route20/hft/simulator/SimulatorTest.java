package eu.route20.hft.simulator;

import static org.junit.Assert.assertEquals;
import lombok.val;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import route20.hft.publisher.Publisher;
import eu.route20.hft.simulation.Simulator;

public class SimulatorTest {
	@InjectMocks
	private Simulator simulator;
	@Mock
	private Publisher pub;

	@Before
	public void setup() {
		simulator = new Simulator();
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void sendsNotificationViaPublisher() throws InterruptedException {
		simulator.setNotifications(10L);
		simulator.doSimulation();
		Mockito.verify(pub, Mockito.times(10)).pub(Mockito.anyString());
	}

	@Test
	public void pausesBetweenNotification() throws InterruptedException {
		simulator.setPauseInNanos(100);
		simulator.setNotifications(1000L);
		val expected = 1280000000;
		val variance = 40000000;
		val t1 = System.nanoTime();
		simulator.doSimulation();
		val t2 = System.nanoTime();
		assertEquals(expected, t2 - t1, variance);
	}

	@Test
	public void randomMsgs() throws InterruptedException {
		simulator.setNotificationChars(100);
		simulator.setNotifications(2);
		simulator.doSimulation();
		Mockito.verify(pub, Mockito.times(2)).pub(Mockito.anyString()); // MATCHER?
	}
}
