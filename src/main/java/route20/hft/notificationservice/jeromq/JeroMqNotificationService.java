package route20.hft.notificationservice.jeromq;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.zeromq.ZMQ;

public class JeroMqNotificationService {
	ZMQ.Context ctx = ZMQ.context(1);
	ZMQ.Socket sub = ctx.socket(ZMQ.SUB);
	ZMQ.Socket pub = ctx.socket(ZMQ.PUB);

	public void run() {
		while (true) {
			String msg = sub.recvStr();
			pub.send(msg);
		}
	}

	@PostConstruct
	public void up(String address) {
		sub.connect("tcp://localhost:5501");
		sub.connect("tcp://localhost:5502");
		pub.bind("tcp://*:5500");
	}

	@PreDestroy
	public void tearDown() {
		sub.close();
		pub.close();
		ctx.term();
	}
}
