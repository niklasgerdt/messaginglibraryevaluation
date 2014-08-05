package eu.route20.hft.simulation.config;

import org.springframework.context.annotation.*;
import org.springframework.scheduling.concurrent.*;

@Configuration @ComponentScan(basePackages = "eu.route20") public class Config {

	@Bean public ThreadPoolTaskExecutor taskExecutor() {
		ThreadPoolTaskExecutor pool = new ThreadPoolTaskExecutor();
		pool.setCorePoolSize(3);
		pool.setMaxPoolSize(3);
		pool.setWaitForTasksToCompleteOnShutdown(true);
		return pool;
	}

}
