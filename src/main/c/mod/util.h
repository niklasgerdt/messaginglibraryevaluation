#ifndef UTIL_H_
#define UTIL_H_

#include <time.h>

void pause(long pause);

struct timespec currentTime();

int killSignal;

void initTerminator(void);

#endif /* UTIL_H_ */
