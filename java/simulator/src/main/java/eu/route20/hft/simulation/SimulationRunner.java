package eu.route20.hft.simulation;

import java.util.*;
import lombok.*;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.*;

@Component @Qualifier("simulationRunner") public class SimulationRunner {
	final static Logger logger = LoggerFactory.getLogger(SimulationRunner.class);
	@Setter private List<Simulator> simulators;
	@Autowired @Qualifier("report") private SimulationReport simulationReport;

	public void run() {
		logger.info("Starting simulation with {} simulators.", simulators.size());
		logger.info("Simulator container id {}.", simulators.toString());
		simulators.stream().parallel().forEach(s -> s.doSimulation());
		logger.info("Notifications simulated.");
	}

	public void report() {
		simulationReport.printReport();
	}
}
