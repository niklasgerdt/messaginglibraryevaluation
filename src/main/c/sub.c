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
	char eventFile[21] = "logs/EVENTSTORE-SUB-";
	strcat(eventFile, channel);

	R20_initTerminator();
	R20_initEventStore(eventFile);
	initSub(address, channel);

	while (R20_killSignal == 0) {
		struct R20_event e = sub();
		e.header.destination = chn;
		e.header.routed = R20_currentTime();
//		printEvent(e);
		R20_storeEvent(e);
	}

	R20_finalizeEventStore();
	destroySub();
	return 0;
}
