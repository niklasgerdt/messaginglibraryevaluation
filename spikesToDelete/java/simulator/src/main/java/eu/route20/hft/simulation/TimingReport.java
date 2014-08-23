package eu.route20.hft.simulation;

import org.slf4j.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.context.annotation.*;
import org.springframework.stereotype.*;
import eu.route20.hft.publisher.*;

@Component @Profile("timing") @Qualifier("report")
public class TimingReport implements SimulationReport{
	final static Logger logger = LoggerFactory.getLogger(SimulationReport.class);
	@Autowired @Qualifier("publisher") private Publisher publisher;

	@Override public void printReport() {
		TimingPublisher pub = (TimingPublisher)publisher;
		logger.info("Publisher captured {} notifications in {} milliseconds", pub.getMessages(), pub.getLastMsgTs() - pub.getFirstMsgTs());
	}
}
