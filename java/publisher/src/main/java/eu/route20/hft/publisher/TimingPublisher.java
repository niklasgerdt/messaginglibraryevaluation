package eu.route20.hft.publisher;

import lombok.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.context.annotation.*;
import org.springframework.stereotype.*;
import route20.hft.annotations.*;

@Component @Profile("timing") @Qualifier("publisher") @Threadsafe public class TimingPublisher implements Publisher {
	@Getter private long firstMsgTs;
	@Getter private long lastMsgTs;
	@Getter private long messages;

	// @Getter private long shortestInterval;
	// @Getter private long longestInterval;

	@Override public void pub(byte[] notification) {
		System.out.println("pubbed");
		val ts = System.currentTimeMillis();
		if (firstMsgTs == 0)
			firstMsgTs = ts;
		messages++;
		lastMsgTs = ts;
	}
}
