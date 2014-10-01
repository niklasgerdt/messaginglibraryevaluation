#ifndef EVENT_H_
#define EVENT_H_

#include <time.h>

struct eventHeader {
	char *source;
	char *destination;
	char id[1000000000];
//	struct timespec created;
//	struct timespec published;
//	struct timespec routed;
};

struct event {
	struct eventHeader header;
	char data[100];
};

void initEventStore(char *fileName);

void storeEvent(struct event *e);

void finalizeEventStore();

#endif /* EVENT_H_ */
