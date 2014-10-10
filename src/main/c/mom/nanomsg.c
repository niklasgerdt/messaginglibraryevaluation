#include <assert.h>
#include <stdio.h>
#include <nanomsg/nn.h>
#include <nanomsg/pubsub.h>
#include <string.h>
#include <time.h>
#include "../mod/event.h"
#include "../mod/util.h"
#include "../pubsub.h"

static int sock;
static char channel[10];

void initPubSub() {
	sock = nn_socket(AF_SP, NN_PUB);
	assert(sock >= 0);
}

void initPub(const char *addr, const char *_channel) {
	printf("Binding pub %s:%s\n", addr, channel);
	strcpy(channel, _channel);
	sock = nn_socket(AF_SP, NN_PUB);
	assert(sock >= 0);
	assert(nn_bind(sock, addr) >= 0);
	printf("Pub bind to %s:%s\n", addr, channel);
}

void destroyPub() {
	nn_shutdown(sock, 0);
	printf("pub down\n");
}

void initSub(const char *addr, const char *_channel) {
	strcpy(channel, _channel);
	printf("Connection sub %s:%s\n", addr, channel);
	sock = nn_socket(AF_SP, NN_SUB);
	assert(sock >= 0);
	for (int i = 0; i < strlen(channel); i++) {
		if (channel[i] == 'N') {
			printf("no subscription (%s)\n", channel);
			assert(nn_setsockopt (sock, NN_SUB, NN_SUB_SUBSCRIBE, "", 0) >= 0);
			break;
		} else {
			printf("subscribed to channel %c\n", channel[i]);
			char c[1] = { channel[i] };
			assert(nn_setsockopt (sock, NN_SUB, NN_SUB_SUBSCRIBE, "", 0) >= 0);
		}
	}
	assert(nn_connect(sock, addr) >= 0);
	printf("Sub connected to %s:%s\n", addr, channel);
}

void addSub(const char *addr) {
	assert(nn_connect(sock, addr) >= 0);
	printf("Sub connected to %s:%s\n", addr, channel);
}

void destroySub() {
	nn_shutdown(sock, 0);
	printf("Sub down\n");
}

void pub(struct event e, size_t size) {
	char msgStr[size];
	sprintf(msgStr, "%c;%ld;%lld.%09ld;%d;%s", e.header.source, e.header.id, e.header.created.tv_sec, e.header.created.tv_nsec,
			e.dataLength, e.data);
	void *msg = nn_allocmsg(strlen(msgStr), 0);
	strncpy(msg, msgStr, strlen(msgStr));
	int nbytes = nn_send(sock, &msg, NN_MSG, 0);
	assert(nbytes == strlen(msgStr));
//	printf("PUB:%s\n", msgStr);
}

struct event sub() {
	struct event e;
	void *buf = NULL;
	int nbytes = nn_recv(sock, &buf, NN_MSG, 0);
	if (nbytes < 0) {
		killSignal = -1;
		return e;
	} else {
		char src;
		long id;
		long int cSec;
		long int cNsec;
		sscanf(buf, "%c;%ld;%lld.%09ld", &src, &id, &cSec, &cNsec);
		struct timespec t = { .tv_sec = cSec, .tv_nsec = cNsec };
		struct eventHeader eh = { .source = src, .id = id, .created = t };
		struct event e = { e.header = eh };
		nn_freemsg(buf);
		return e;
	}
}

void med() {
}

