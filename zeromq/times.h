#ifndef TIMES_H_
#define TIMES_H_

#include <time.h>

struct timespec ts;
long pauseInNanos;

long currentNanos(void);

void pause(long current);

void setPauseLenNanos(const char *p);

#endif /* TIMES_H_ */
