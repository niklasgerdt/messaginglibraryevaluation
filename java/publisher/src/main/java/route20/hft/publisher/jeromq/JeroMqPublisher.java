package route20.hft.publisher.jeromq;

import javax.annotation.*;

import org.springframework.beans.factory.annotation.*;

import route20.hft.publisher.*;

public class JeroMqPublisher implements Publisher {
	@Autowired
	private JeroMqHelper jmq;

	@PostConstruct
	public void up(String address) {
		jmq.up();
	}

	@Override
	public void pub(String notification) {
		jmq.send(notification);
	}

	@PreDestroy
	public void down() {
		jmq.down();
	}
}
