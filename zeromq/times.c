#include <time.h>
#include <assert.h>
#include "times.h"

#define SECONDINNANOS 1000000000

struct timespec currentTime() {
	struct timespec ts;
	clock_gettime(CLOCK_REALTIME, &ts);
	return ts;
}

static void pauseNanos(struct timespec t) {
	struct timespec now = currentTime();
	if (t.tv_sec > now.tv_sec) {
		pauseNanos(t);
	} else if (t.tv_sec == now.tv_sec && t.tv_nsec > now.tv_nsec) {
		pauseNanos(t);
	}
}

void pause(long pause) {
	assert(pause < SECONDINNANOS);
	struct timespec t = currentTime();
	if (t.tv_nsec + pause >= 1000000000) {
		t.tv_sec++;
		t.tv_nsec = t.tv_nsec + pause - SECONDINNANOS;
	} else {
		t.tv_nsec += pause;
	}
	pauseNanos(t);
}

