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
static char channel[10];

void initPub(const char *addr, const char *_channel) {
	strcpy(channel, _channel);
	printf("Binding pub %s:%s\n", addr, channel);
	context = zmq_ctx_new();
	publisher = zmq_socket(context, ZMQ_PUB);
	assert(zmq_bind(publisher, addr) == 0);
	printf("Pub bind to %s:%s\n", addr, channel);
}

void destroyPub() {
	zmq_close(publisher);
	zmq_ctx_destroy(context);
	printf("pub down\n");
}

void initSub(const char *addr, const char *_channel) {
	strcpy(channel, _channel);
	printf("Connection sub %s:%s\n", addr, channel);
	context = zmq_ctx_new();
	subscriber = zmq_socket(context, ZMQ_SUB);
	assert(zmq_connect(subscriber, addr) == 0);
	assert(zmq_setsockopt(subscriber, ZMQ_SUBSCRIBE, channel, strlen(channel)) == 0);
//	assert(zmq_setsockopt(subscriber, ZMQ_SUBSCRIBE, "", 0) == 0);
	printf("Sub connected to %s:%s\n", addr, channel);
}

void addSub(const char *addr) {
	assert(zmq_connect(subscriber, addr) == 0);
	printf("Sub connected to %s:%s\n", addr, channel);
}

void destroySub() {
	zmq_close(subscriber);
	if (context != NULL) {
		zmq_ctx_destroy(context);
	}
	printf("Sub down\n");
}

static void addChannel() {
	zmq_msg_t msg;
	assert(zmq_msg_init_size(&msg, strlen(channel)) == 0);
	memcpy(zmq_msg_data(&msg), channel, strlen(channel));
	assert(zmq_msg_send(&msg, publisher, ZMQ_SNDMORE) == strlen(channel));
	zmq_msg_close(&msg);
}

void pub(struct event e, size_t size) {
	if (strlen(channel) > 0) {
		addChannel();
	}
	zmq_msg_t msg;
	char msgStr[size];
	assert(zmq_msg_init_size(&msg, size) == 0);
	sprintf(msgStr, "%c;%ld;%lld.%09ld;%d;%s", e.header.source, e.header.id, e.header.created.tv_sec, e.header.created.tv_nsec,
			e.dataLength, e.data);
	memcpy(zmq_msg_data(&msg), msgStr, size);
	assert(zmq_msg_send(&msg, publisher, 0) == size);
	zmq_msg_close(&msg);
}

struct event sub() {
	zmq_msg_t msg;
	assert(zmq_msg_init(&msg) == 0);
	assert(zmq_msg_recv(&msg, subscriber, 0) >= 0);
	char msgStr[zmq_msg_size(&msg)];
	memcpy(msgStr, zmq_msg_data(&msg), zmq_msg_size(&msg));
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

void med() {
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
