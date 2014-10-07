#ifndef PUBSUB_H_
#define PUBSUB_H_

#include "mod/event.h"

void initPub(const char *address, char channel);

void pub(struct event *e);

void pubRaw(const void *b);

void destroyPub(void);

void sub(struct event *e);

void subRaw(void *b);

void initSub(const char *address, char channel);

void destroySub(void);

#endif /* PUBSUB_H_ */
