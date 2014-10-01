#include "times.h"
#include <stdio.h>

long currentNanos() {
	clock_gettime(CLOCK_REALTIME, &ts);
	return (long) ts.tv_nsec;
}

struct timespec currentTime() {
	clock_gettime(CLOCK_REALTIME, &ts);
	return ts;
}

const char * currentTimeStr(void){

}

void pause(long current) {
	//todo include seconds
	if (currentNanos() < (pauseInNanos + current)) {
		pause(current);
	}
}

void setPauseLenNanos(const char *p) {
	char *omit;
	pauseInNanos = strtol(p, &omit, 10);
	printf("Setting pause to %ld\n", pauseInNanos);
}
