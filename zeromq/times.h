#ifndef TIMES_H_
#define TIMES_H_

#include <time.h>

struct timespec ts;

long pauseInNanos;

struct timespec currentTime(void);

long currentNanos(void);

const char * currentTimeStr(void);

void pause(long current);

void setPauseLenNanos(const char *p);

#endif /* TIMES_H_ */
