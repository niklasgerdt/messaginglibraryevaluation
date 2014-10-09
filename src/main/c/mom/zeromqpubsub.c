#include <zmq.h>
#include <assert.h>
#include <stdio.h>
#include <string.h>
#include <time.h>
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
	zmq_msg_t msg;
	char msgStr[size];
	assert(zmq_msg_init_size(&msg, size) == 0);
	sprintf(msgStr, "%c;%ld;%lld.%09ld;%d;%s", e.header.source, e.header.id, e.header.created.tv_sec, e.header.created.tv_nsec,
			e.dataLength, e.data);
	memcpy(zmq_msg_data(&msg), msgStr, size);
	assert(zmq_msg_send(&msg, publisher, 0) == size);
	zmq_msg_close(&msg);
}

struct event sub(size_t size) {
	zmq_msg_t msg;
	assert(zmq_msg_init(&msg) == 0);
	assert(zmq_msg_recv(&msg, subscriber, 0) >= 0);
	char msgStr[size];
	memcpy(msgStr, zmq_msg_data(&msg), size);
	char src;
	long id;
	long int cSec;
	long int cNsec;
	sscanf(msgStr, "%c;%ld;%lld.%09ld", &src, &id, &cSec, &cNsec);
	struct timespec t = { .tv_sec = cSec, .tv_nsec = cNsec };
	struct eventHeader eh = { .source = src, .id = id, .created = t };
	struct event e = { e.header = eh };
	zmq_msg_close(&msg);
	return e;
}

void med(size_t size) {
	zmq_msg_t msg_in;
	assert(zmq_msg_init(&msg_in) == 0);
	assert(zmq_msg_recv(&msg_in, subscriber, 0) >= 0);
	zmq_msg_t msg_out;
	assert(zmq_msg_init_size(&msg_out, zmq_msg_size(&msg_in)) == 0);
	memcpy(zmq_msg_data(&msg_out), zmq_msg_data(&msg_in), zmq_msg_size(&msg_in));
	assert(zmq_msg_send(&msg_out, publisher, 0) == zmq_msg_size(&msg_in));
	zmq_msg_close(&msg_in);
	zmq_msg_close(&msg_out);
}
