package eu.route20.hft.simulator;

import lombok.*;
import org.springframework.context.annotation.*;
import eu.route20.hft.simulation.*;
import eu.route20.hft.simulation.config.*;

public class SimulatorApp {

	public static void main(String[] args) throws ClassNotFoundException {
		System.setProperty("spring.profiles.active", args[0]);
		val ctx = new AnnotationConfigApplicationContext(Config.class);
		val runner = (SimulationRunner) ctx.getBean("simulationRunner");
		runner.run();
		runner.report();
		ctx.close();
	}

}
