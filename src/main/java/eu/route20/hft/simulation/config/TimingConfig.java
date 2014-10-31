package eu.route20.hft.simulation.config;

import java.util.*;
import org.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.context.*;
import org.springframework.context.annotation.*;
import eu.route20.hft.simulation.*;

@Configuration @Profile("timing") public class TimingConfig {
	private static final int THOUSAND = 1000;
	private static final int HUNDREDTHOUSAND = 100000;
	private static final int MILLION = 1000 * 1000;
	private static final int HUNDRED = 100;
	final static Logger logger = LoggerFactory.getLogger(TimingConfig.class);
	@Autowired ApplicationContext ctx;

	@Bean public List<Simulator> simulators() {
		List<Simulator> simulatorList = new ArrayList<>();
		
		Simulator quoteSimulator = (Simulator) ctx.getBean("simulator");
		quoteSimulator.setNotificationLengthInBytes(HUNDRED);
		quoteSimulator.setNotifications(MILLION);
		quoteSimulator.setPauseInNanosBetweenNotifications(1);
		logger.info("created quotesimulator");
		
		Simulator newsSimulator = (Simulator) ctx.getBean("simulator");
		newsSimulator.setNotificationLengthInBytes(HUNDREDTHOUSAND);
		newsSimulator.setNotifications(THOUSAND);
		logger.info("created newssimulator");
		
		Simulator someSimulator = (Simulator) ctx.getBean("simulator");
		someSimulator.setNotificationLengthInBytes(THOUSAND);
		someSimulator.setNotifications(THOUSAND);
		logger.info("created somesimulator");
		
		simulatorList.add(quoteSimulator);
//		simulatorList.add(newsSimulator);
//		simulatorList.add(someSimulator);
		logger.info("Simulator container id {}.", simulatorList.toString());
		
		return simulatorList;
	}
	
}
