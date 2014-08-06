package eu.route20.hft.simulation;

import java.util.*;
import lombok.*;
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
		val simulatorsWithOneSimulator = Arrays.asList(s1);
		runner.setSimulators(simulatorsWithOneSimulator);
		val simulatorsWithTwoSimulators = Arrays.asList(s1, s2);
		runner.setSimulators(simulatorsWithTwoSimulators);
	}

	@Ignore @Test public void runsAllSimulators() {
		val simulatorsWithTwoSimulators = Arrays.asList(s1, s2);
		runner.setSimulators(simulatorsWithTwoSimulators);
		runner.run();
		Mockito.verify(s1).doSimulation();
		Mockito.verify(s2).doSimulation();
	}

	@Ignore @Test public void runInParallel() {
		val simulatorsWithTwoSimulators = Arrays.asList(s1, s2);
		runner.setSimulators(simulatorsWithTwoSimulators);
		runner.run();
		InOrder order = Mockito.inOrder(s1, s2);
		order.verify(s1).doSimulation();
		order.verify(s2).doSimulation();

	}
}
