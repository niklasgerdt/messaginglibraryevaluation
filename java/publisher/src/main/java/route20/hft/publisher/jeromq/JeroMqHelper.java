package route20.hft.publisher.jeromq;

import javax.annotation.*;

import org.springframework.beans.factory.annotation.*;
import org.zeromq.*;

class JeroMqHelper {
	private ZMQ.Context ctx;
	private ZMQ.Socket socket;
	@Autowired(required = true)
	private String address;

	@PostConstruct
	void up() {
		ctx = ZMQ.context(1);
		socket = ctx.socket(ZMQ.PUB);
		socket.connect(address);
	}

	@PreDestroy
	void down() {
		socket.close();
		ctx.term();
	}

	void send(String notification) {
		socket.send(notification);
	}
}
