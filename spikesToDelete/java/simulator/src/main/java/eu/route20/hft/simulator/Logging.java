package eu.route20.hft.simulator;

import org.slf4j.*;
import ch.qos.logback.classic.*;
import ch.qos.logback.core.util.*;

public class Logging {

	void stopLogging() {
		LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
		loggerContext.stop();
	}

	void logbackStatus() {
		LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
		StatusPrinter.print(lc);
	}
}
