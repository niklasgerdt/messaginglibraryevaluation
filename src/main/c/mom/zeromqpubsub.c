#include <zmq.h>
#include <assert.h>
#include <stdio.h>
#include <string.h>
#include "../mod/event.h"
#include "../pubsub.h"

static void *context;
static void *publisher;
static void *subscriber;
static char pubchannel;
//static char *subchannel;

void initPub(const char *addr, const char *channel) {
	printf("Setting up ZeroMQ pub-sub-system\n");
	pubchannel = channel[0];
	context = zmq_ctx_new();
	publisher = zmq_socket(context, ZMQ_PUB);
	assert(zmq_bind(publisher, addr) == 0);
	printf("Pub bind to %s\n", addr);
}

void destroyPub() {
	zmq_close(publisher);
	zmq_ctx_destroy(context);
	printf("ZeroMQ down\n");
}

void initSub(const char *addr, const char *channel) {
	printf("Setting up ZeroMQ pubsub-system\n");
	context = zmq_ctx_new();
	subscriber = zmq_socket(context, ZMQ_SUB);
	assert(zmq_connect(subscriber, addr) == 0);
//	assert(zmq_setsockopt(subscriber, ZMQ_SUBSCRIBE, channel, sizeof(channel)) == 0);
	assert(zmq_setsockopt(subscriber, ZMQ_SUBSCRIBE, "", 0) == 0);
	printf("Sub connected to %s\n", addr);
}

void addSub(const char *addr) {
	assert(zmq_connect(subscriber, addr) == 0);
	printf("Sub connected to %s\n", addr);
}

void destroySub() {
	zmq_close(subscriber);
	if (context != NULL) {
		zmq_ctx_destroy(context);
	}
	printf("ZeroMQ down\n");
}

void pub(struct event e, size_t size) {
//TODO ADD CHANNEL AS PART
	printf("%d\n", &e);
	printf("%c;%c;%d;%lld.%09ld;%lld.%09ld;%lld.%09ld\n", e.header.source, e.header.destination, e.header.id,
			e.header.created.tv_sec, e.header.created.tv_nsec, e.header.published.tv_sec, e.header.published.tv_nsec,
			e.header.routed.tv_sec, e.header.routed.tv_nsec);
	zmq_send(publisher, &e, size, 0);
}

struct event sub(size_t size) {
	char buf[size];
	zmq_recv(subscriber, &buf, size, 0);
	struct event *e = (struct event *) &buf;

//	printf("%c;%c;%d;%lld.%09ld;%lld.%09ld;%lld.%09ld\n", e.header.source, e.header.destination, e.header.id,
//			e.header.created.tv_sec, e.header.created.tv_nsec, e.header.published.tv_sec, e.header.published.tv_nsec,
//			e.header.routed.tv_sec, e.header.routed.tv_nsec);
	printf("%d\n", e);
	return (*e);
}

void med(size_t size) {
	zmq_msg_t msg_in;
	assert(zmq_msg_init(&msg_in) == 0);
	assert(zmq_msg_recv(&msg_in, subscriber, 0) == 0);
	zmq_msg_t msg_out;
	assert(zmq_msg_init_size(&msg_out, zmq_msg_size(&msg_in)) == 0);
	memcpy(&msg_out, zmq_msg_data(&msg_in), zmq_msg_size(&msg_in));
	assert(zmq_msg_send(&msg_out, publisher, 0) == 0);
}
