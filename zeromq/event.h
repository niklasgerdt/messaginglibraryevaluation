#ifndef EVENT_H_
#define EVENT_H_

#include <time.h>

extern int eventMesasgeLength;

struct eventHeader {
	char source;
	char destination;
	long id;
	struct timespec created;
	struct timespec published;
	struct timespec routed;
};

struct event {
	struct eventHeader header;
	char data[eventMesasgeLength];
};

void initEventStore(char *fileName);

void storeEvent(struct event *e);

void finalizeEventStore();

#endif /* EVENT_H_ */
