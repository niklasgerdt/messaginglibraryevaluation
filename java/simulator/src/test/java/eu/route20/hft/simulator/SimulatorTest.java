package eu.route20.hft.simulator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import route20.hft.publisher.Publisher;
import eu.route20.hft.simulation.Simulator;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/ApplicationContext.xml")
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
	public void sendsNotificationViaPublisher() {
		simulator.setNotifications(10L);
		simulator.doSimulation();
		Mockito.verify(pub, Mockito.atLeast(1)).pub(Mockito.startsWith("notification"));
	}

	@Test
	public void test() {
		// fail("Not yet implemented");
	}

}
