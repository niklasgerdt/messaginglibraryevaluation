#include <assert.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "mod/util.h"
#include "pubsub.h"

int main(int argc, char *argv[]) {
	long pauseNanos = atol(argv[1]);
	char *address = argv[2];
	char channel = argv[3][0];
	int eventMessageLength = atoi(argv[4]);
	initTerminator();
	initEventStore("EVENTSTORE-PUB-" + channel);
	initPub(address, channel);
	printf("Running with params: %d, %s, %s, %d\n", pauseNanos, address, channel, eventMessageLength);

	long idCount = 0;
	size_t size = sizeof(struct event) + eventMessageLength * sizeof(char);
	char *eData = malloc(eventMessageLength * sizeof(char));
	memset(eData, 'A', eventMessageLength * sizeof(char));
	while (killSignal == 0) {
		struct eventHeader eh = { .source = channel, .id = idCount, .created = currentTime() };
		struct event e = { .header = eh, .dataLength = eventMessageLength, .data = eData };
		pub(&e, size);
		e.header.published = currentTime();
		storeEvent(&e);
		pause(pauseNanos);
		idCount++;
	}

	finalizeEventStore();
	destroyPub();
	return 0;
}
