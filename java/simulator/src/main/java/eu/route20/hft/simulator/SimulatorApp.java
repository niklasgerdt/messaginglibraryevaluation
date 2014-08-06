package eu.route20.hft.simulator;

import java.util.*;
import lombok.*;
import org.slf4j.*;
import org.springframework.context.annotation.*;
import ch.qos.logback.classic.*;
import ch.qos.logback.core.util.*;
import eu.route20.hft.simulation.*;
import eu.route20.hft.simulation.config.*;

public class SimulatorApp {

	public static void main(String[] args) throws ClassNotFoundException {
		logbackStatus();
		run(args[0]);
		stopLogging();
	}

	private static void run(String profile) {
		System.setProperty("spring.profiles.active", profile);
		val ctx = new AnnotationConfigApplicationContext(Config.class);
		val runner = (SimulationRunner) ctx.getBean("simulationRunner");
		@SuppressWarnings("unchecked") val simulators = (List<Simulator>) ctx.getBean("simulators");
		runner.setSimulators(simulators);

		runner.run();
		runner.report();

		ctx.close();
	}

	private static void stopLogging() {
		LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
		loggerContext.stop();
	}

	private static void logbackStatus() {
		LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
		StatusPrinter.print(lc);
	}
}
