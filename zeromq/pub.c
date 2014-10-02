#include <zmq.h>
#include <assert.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "times.h"
#include "../modules/terminator.h"
#include "event.h"

int MILLION = 1000000;
char *ptr;

int main(int argc, char *argv[]) {
	long idCount = 0;
	long pauseNanos = atol(argv[1]);
	char *address = argv[2];
	char *exchange = argv[3];
	int msgLen = atoi(argv[4]);
	printf("Setting up ZeroMQ pub-sub-system\n");
	printf("Running with params: %d, %s, %s, %d\n", pauseNanos, address, exchange, msgLen);
	initTerminator();
	initEventStore("EVENTSTORE");

	printf("Setting up ZeroMQ pub-sub-system\n");
	void *context = zmq_ctx_new();
	void *publisher = zmq_socket(context, ZMQ_PUB);
	int rc = 0;
	rc = zmq_bind(publisher, address);
	assert(rc == 0);
	printf("Pub bind to %s\n", address);

	char *eData;
	memset(eData, 'A', msgLen);
	while (killSignal == 0) {
		struct eventHeader eh = { .source = exchange, .id = idCount, .created = currentTime() };
		struct event e = { .header = eh, .data = eData };
		memset(&e.data, 'A', msgLen);
		zmq_send(publisher, &e, sizeof(e), 0);
		e.header.published = currentTime();
		pause(pauseNanos);
		storeEvent(&e);
		idCount++;
	}
	printf("ZeroMQ down\n");

	finalizeEventStore();
	zmq_close(publisher);
	zmq_ctx_destroy(context);
	return 0;
}
