#include <aio.h>
#include <stdio.h>
#include <stdlib.h>
#include <signal.h>
#include <string.h>
#include <fcntl.h>
#include "event.h"

//#define EVENTSTORESIZE 10000000

#define EVENTSTORESIZE 1000
static int eventCount = 0;
static struct eventHeader *events;

static FILE *f;
struct aiocb cb;
struct sigevent sig;

static char *asyncEvents;

static struct sigaction action;

static void asyncWriteSig(int signum) {
	free(asyncEvents);
	printf("Async write done\n");
}

static void initWriteSig(void) {
	action.sa_handler = asyncWriteSig;
	sigaction(SIGUSR1, &action, NULL);
	printf("Set up async io signal handler\n");
}

void initEventStore(char *fileName) {
	f = fopen(fileName, "w");
	if (f == NULL) {
		printf("Error opening file!\n");
	}
	initWriteSig();
	events = malloc(EVENTSTORESIZE * sizeof(struct eventHeader));
}

static void asyncWrite(){
	asyncEvents = malloc(EVENTSTORESIZE * sizeof(struct eventHeader));
	for (int i=0; i<EVENTSTORESIZE; i++){
		int e = sprintf(&asyncEvents[i], "%s;%s;%d;", events[i].source, events[i].destination, events[i].id);
	}
	printf("s%", asyncEvents);
	memset(&cb, 0, sizeof(struct aiocb));
	cb.aio_fildes = f->_fileno;
	cb.aio_buf = asyncEvents;
	cb.aio_nbytes = sizeof(asyncEvents);
	cb.aio_sigevent.sigev_notify = SIGEV_SIGNAL;
	cb.aio_sigevent.sigev_signo = SIGUSR1;
	aio_write(&cb);
	eventCount = 0;
	events = malloc(EVENTSTORESIZE * sizeof(struct eventHeader));
}

void storeEvent(struct event *e) {
	if (eventCount == EVENTSTORESIZE) {
		printf("Start async!\n");
		asyncWrite();
	}
	events[eventCount] = e->header;
	eventCount++;
}

void finalizeEventStore() {
	asyncWrite();
	fclose(f);
	free(asyncEvents);
	free(events);
}
