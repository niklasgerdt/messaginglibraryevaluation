#ifndef EVENT_H_
#define EVENT_H_

#include <string.h>

char dummyData[] = "agsggjbvoefmlä,äoeaaaagj,vaäsnkldfanhkäladjpoherlj'ahldj'ghaskäagmj'adfljäedäkäaskfä aä  ag äida";

struct eventHeader {
	char pub[4];
	unsigned long id;
	long created;
	long published;
	long routed;
};

struct event {
	struct eventHeader header;
	char data[100];
};

#endif /* EVENT_H_ */
