#include <stdio.h>
#include <stdlib.h>
#include <signal.h>
#include <string.h>
#include <fcntl.h>
#include "event.h"

#define MILLION 1000000
#define EVENTSTORESIZE 100*MILLION
static int eventCount = 0;
static struct eventHeader *events;

static FILE *f;

void initEventStore(char *fileName) {
	f = fopen(fileName, "w");
	if (f == NULL) {
		printf("Error opening file!\n");
	}
	events = malloc(EVENTSTORESIZE * sizeof(struct eventHeader));
}

static void write() {
	for (int i = 0; i < EVENTSTORESIZE; i++) {
		fprintf(f, "%s;%s;%d;%lld.%09ld;%lld.%09ld;%lld.%09ld\n", events[i].source, events[i].destination, events[i].id,
				events[i].created.tv_sec, events[i].created.tv_nsec, events[i].published.tv_sec,
				events[i].published.tv_nsec, events[i].routed.tv_sec, events[i].routed.tv_nsec);
	}
	eventCount = 0;
	events = malloc(EVENTSTORESIZE * sizeof(struct eventHeader));
}

void storeEvent(struct event *e) {
	if (eventCount == EVENTSTORESIZE) {
		write();
	}
	events[eventCount] = e->header;
	eventCount++;
}

void finalizeEventStore() {
	write();
	fclose(f);
	free(events);
}
