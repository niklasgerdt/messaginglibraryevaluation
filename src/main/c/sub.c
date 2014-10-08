#include <assert.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "mod/event.h"
#include "pubsub.h"
#include "mod/util.h"

static char chn;

int main(int argc, char *argv[]) {
	char *address = argv[1];
	char *channel = argv[2];
	chn = argv[2][0];
	int msgLen = atoi(argv[3]);
	char eventFile[21] = "logs/EVENTSTORE-SUB-";
	strcat(eventFile, channel);
	printf("Running with params: %s, %s, %d\n", address, channel, msgLen);

	initTerminator();
	initEventStore(eventFile);
	initSub(address, channel);

	size_t size = sizeof(struct event) + msgLen * sizeof(char);

	while (killSignal == 0) {
		struct event e;
		sub(&e, size);
		e.header.destination = chn;
		e.header.routed = currentTime();
//		printf("%c;%c;%d;%lld.%09ld;%lld.%09ld;%lld.%09ld\n", e.header.source, e.header.destination, e.header.id,
//				e.header.created.tv_sec, e.header.created.tv_nsec, e.header.published.tv_sec,
//				e.header.published.tv_nsec, e.header.routed.tv_sec, e.header.routed.tv_nsec);
		storeEvent(&e);
	}

	finalizeEventStore();
	destroySub();
	return 0;
}
