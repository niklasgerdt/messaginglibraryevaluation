#include <zmq.h>
#include <assert.h>
#include <stdio.h>
#include "../mod/event.h"
#include "../pubsub.h"

static void *context;
static void *publisher;
static void *subscriber;
static char pubchannel;
//static char *subchannel;

void initPub(const char *addr, char channel) {
	printf("Setting up ZeroMQ pub-sub-system\n");
	pubchannel = channel;
	context = zmq_ctx_new();
	publisher = zmq_socket(context, ZMQ_PUB);
	assert(zmq_bind(publisher, addr) == 0);
	printf("Pub bind to %s\n", addr);
}

void pub(struct event *e) {
//TODO ADD CHANNEL AS PART
	zmq_send(publisher, &e, sizeof(e), 0);
}

void pubRaw(const void *b) {
	zmq_send(publisher, b, sizeof(b), 0);
}

void destroyPub() {
	zmq_close(publisher);
	zmq_ctx_destroy(context);
	printf("ZeroMQ down\n");
}

void initSub(const char *addr, char channel) {
	printf("Setting up ZeroMQ pubsub-system\n");
	context = zmq_ctx_new();
	subscriber = zmq_socket(context, ZMQ_SUB);
	assert(zmq_connect(subscriber, addr) == 0);
//	assert(zmq_setsockopt(subscriber, ZMQ_SUBSCRIBE, channel, sizeof(channel)) == 0);
	assert(zmq_setsockopt(subscriber, ZMQ_SUBSCRIBE, "", 0) == 0);
	printf("Sub connected to %s\n", addr);
}

void sub(struct event *e) {
	char buffer[100];
	zmq_recv(subscriber, buffer, 100, 0);
	printf("received event %s\n", buffer);
//TODO convert to event
}

void subRaw(void *b) {
	zmq_recv(publisher, b, sizeof(b), 0);
}

void destroySub() {
	zmq_close(subscriber);
	zmq_ctx_destroy(context);
	printf("ZeroMQ down\n");
}

