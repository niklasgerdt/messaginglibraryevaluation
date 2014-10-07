#include <assert.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "mod/util.h"
#include "pubsub.h"

int main(int argc, char *argv[]) {
	long pauseNanos = atol(argv[1]);
	char *address = argv[2];
	const char *channel = argv[3];
	int eventMessageLength = atoi(argv[4]);
	char eventFile[20] = "logs/EVENTSTOREPUB";
	strcat(eventFile, channel);

	initTerminator();
	initEventStore(eventFile);
	initPub(address, channel);
	printf("Running with params: \npause %d, \naddress %s, \nchannel %s, \nmsglen %d\n", pauseNanos, address, channel,
			eventMessageLength);

	size_t size = sizeof(struct event) + eventMessageLength * sizeof(char);
	char *eData = malloc(eventMessageLength * sizeof(char));
	memset(eData, 'A', eventMessageLength * sizeof(char));

	long idCount = 0;
	while (killSignal == 0) {
		struct eventHeader eh = { .source = channel[0], .id = idCount, .created = currentTime() };
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
