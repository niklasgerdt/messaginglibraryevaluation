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

void initPub(const char *addr, const char *channel) {
	printf("Setting up ZeroMQ pub-sub-system\n");
	pubchannel = channel[0];
	context = zmq_ctx_new();
	publisher = zmq_socket(context, ZMQ_PUB);
	assert(zmq_bind(publisher, addr) == 0);
	printf("Pub bind to %s\n", addr);
}

void pub(struct event *e, size_t size) {

//TODO ADD CHANNEL AS PART
//	printf("%c;%c;%d;%lld.%09ld;%lld.%09ld;%lld.%09ld\n", e->header.source, e->header.destination, e->header.id,
//			e->header.created.tv_sec, e->header.created.tv_nsec, e->header.published.tv_sec,
//			e->header.published.tv_nsec, e->header.routed.tv_sec, e->header.routed.tv_nsec);
	zmq_send(publisher, e, size, 0);
}

void pubRaw(const void *b) {
	zmq_send(publisher, b, sizeof(b), 0);
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

void addSub(const char *addr){
	assert(zmq_connect(subscriber, addr) == 0);
}

void sub(struct event *e, size_t size) {
	zmq_recv(subscriber, e, size, 0);
}

void subRaw(void *b) {
	zmq_recv(publisher, b, sizeof(b), 0);
}

void destroySub() {
	zmq_close(subscriber);
	zmq_ctx_destroy(context);
	printf("ZeroMQ down\n");
}

