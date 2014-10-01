#include <sys/time.h>
#include <stdio.h>
#include "../event.h"

struct timespec t1, t2;

int main() {

	struct event *e;
	initEventStore("filename.txt");
	storeEvent(e);
	finalizeEventStore();

	FILE *ff = fopen("speed", "w");
	//clock_gettime(CLOCK_REALTIME, &t1);
	fprintf(ff, "%s %s;", "Niklas", "Gerdt");
	//clock_gettime(CLOCK_REALTIME, &t2);
	fclose(ff);

}

