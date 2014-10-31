package eu.route20.hft.publisher;

import route20.hft.annotations.*;

@Threadsafe
public interface Publisher {

	void pub(byte[] notification);
}
