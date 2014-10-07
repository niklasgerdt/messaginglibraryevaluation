#include <assert.h>
#include <stdio.h>
#include <stdlib.h>
#include "mod/event.h"
#include "pubsub.h"
#include "mod/util.h"

int main(int argc, char *argv[]) {
	char *address = argv[1];
	char *channel = argv[2];
	int msgLen = atoi(argv[3]);
	initTerminator();
	initEventStore("EVENTSTORE-SUB");
	initSub(address, channel);
	printf("Running with params: %s, %s, %d\n", address, channel, msgLen);

	struct event e;
	while (killSignal == 0) {
		sub(&e);
		e.header.routed = currentTime();
		storeEvent(&e);
	}

	finalizeEventStore();
	destroySub();
	return 0;
}
