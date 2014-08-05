package eu.route20.hft.simulation;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.*;
import org.springframework.scheduling.concurrent.*;
import org.springframework.stereotype.*;
import lombok.val;

@Component @Qualifier("simulationRunner") public class SimulationRunner {
	@Autowired ThreadPoolTaskExecutor taskExecutor;
	@Autowired private List<Simulator> simulators;
	@Autowired @Qualifier("report") private SimulationReport simulationReport;

	// TODO remove
	public void addSimulators(Simulator... simulators) {
		val sims = Arrays.asList(simulators);
		this.simulators = Collections.unmodifiableList(sims);
	}

	public void run() {
		// simulators.parallelStream().forEach(s -> taskExecutor.execute(s));
		System.out.println("-------**********************\n"+simulators.size());
		for (Simulator s : simulators){
			System.out.println("simulating");
			s.doSimulation();
		}
	}

	public void report() {
		simulationReport.printReport();
	}
}
