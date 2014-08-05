package eu.route20.hft.simulation.config;

import java.util.*;
import lombok.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.context.*;
import org.springframework.context.annotation.*;
import eu.route20.hft.simulation.*;

@Configuration @Profile("timing") public class TimingConfig {
	@Autowired ApplicationContext ctx;

	@Bean public List<Simulator> simulators() {
		val simulatorList = new ArrayList<Simulator>();
		
		Simulator quoteSimulator = (Simulator) ctx.getBean("simulator");
		quoteSimulator.setNotificationLengthInBytes(100);
		quoteSimulator.setNotifications(1000);
		
		simulatorList.add(quoteSimulator);
		return simulatorList;
	}
}
