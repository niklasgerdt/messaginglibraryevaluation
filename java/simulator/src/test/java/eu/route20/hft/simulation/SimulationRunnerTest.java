package eu.route20.hft.simulation;

import org.junit.*;
import org.mockito.*;

public class SimulationRunnerTest {
	private SimulationRunner runner;
	@Mock private Simulator s1;
	@Mock private Simulator s2;

	@Before public void setup() {
		MockitoAnnotations.initMocks(this);
		runner = new SimulationRunner();
	}

	@Test public void takes1toXSimulators() {
		runner.addSimulators(s1);
		runner.addSimulators(s1, s2);
	}

	@Ignore @Test public void runsAllSimulators() {
		runner.addSimulators(s1, s2);
		runner.run();
		Mockito.verify(s1).doSimulation();
		Mockito.verify(s2).doSimulation();
	}

	@Ignore @Test public void runInParallel() {
		runner.addSimulators(s1, s2);
		runner.run();
		InOrder order = Mockito.inOrder(s1, s2);
		order.verify(s1).doSimulation();
		order.verify(s2).doSimulation();

	}
}
