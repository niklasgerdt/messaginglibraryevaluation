package route20.hft.publisher.jeromq;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.zeromq.ZMQ;

import route20.hft.publisher.Publisher;

public class JeroMqPublisher implements Publisher {
	private static final int ZMQ_THREADS = 1;
	private ZMQ.Context ctx;
	private ZMQ.Socket socket;
	private String address;

	public void setAddress(String address) {
		this.address = address;
	}

	@PostConstruct
	public void up() {
		ctx = ZMQ.context(ZMQ_THREADS);
		socket = ctx.socket(ZMQ.PUB);
		socket.connect(address);
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
