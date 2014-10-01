#include "event.h"
#include "stdio.h"

//#define EVENTSTORESIZE 10000000

#define EVENTSTORESIZE 0

static int eventCount = 0;

static struct eventHeader events[EVENTSTORESIZE];

static FILE *f;

void initEventStore(char *fileName) {
	f = fopen(fileName, "w");
	if (f == NULL) {
		printf("Error opening file!\n");
	}
}

void storeEvent(struct event *e) {
	if (eventCount == EVENTSTORESIZE) {
		for (int i = 0; i < eventCount; i++) {
			//todo ASYNCs
			fprintf(f, "%s;%s;", events[i].source, events[i].destination);
		}
		eventCount = 0;
	}
	events[eventCount] = e->header;
	eventCount++;
}

void finalizeEventStore() {
	fclose(f);
}
