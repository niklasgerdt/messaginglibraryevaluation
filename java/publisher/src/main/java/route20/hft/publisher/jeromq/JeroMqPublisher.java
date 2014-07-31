package route20.hft.publisher.jeromq;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.zeromq.ZMQ;

import route20.hft.publisher.Publisher;

public class JeroMqPublisher implements Publisher {
	private ZMQ.Context ctx;
	private ZMQ.Socket socket;

	@PostConstruct
	public void up(String address) {
		ctx = ZMQ.context(1);
		socket = ctx.socket(ZMQ.PUB);
		socket.connect("");
	}

	@Override
	public void pub(String notification) {
		socket.send(notification);
	}

	@PreDestroy
	public void down() {
		socket.close();
		ctx.term();
	}
}
