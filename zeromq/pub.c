#include <zmq.h>
#include <assert.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
//#include "times.h"
#include "../modules/terminator.h"
#include "event.h"

int MILLION = 1000000;

int main(int argc, char *argv[]) {
	//setPauseLenNanos(argv[1]);
	initTerminator();
	initEventStore("EVENTSTORE");

	printf("Setting up ZeroMQ pub-sub-system\n");
	int rc;
	void *context = zmq_ctx_new();
	void *publisher = zmq_socket(context, ZMQ_PUB);
	rc = zmq_bind(publisher, argv[2]);
	assert(rc == 0);
	printf("ZeroMQ up\n");
	printf("Pub bind to %s\n", argv[2]);

	long idCount = 0;
	while (killSignal == 0) {
		struct eventHeader eh = { .source = "A", .id = idCount };
		struct event e = { .header = eh };
		memset(&e.data, 'A', sizeof(e.data));
		zmq_send(publisher, &e, sizeof(e), 0);
	//	pause(currentNanos());
		storeEvent(&e);
		idCount++;
//		printf("sent %d\n", idCount);
	}
	printf("ZeroMQ down\n");

	finalizeEventStore();
	zmq_close(publisher);
	zmq_ctx_destroy(context);
	return 0;
}
