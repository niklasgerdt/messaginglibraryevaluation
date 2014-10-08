#ifndef PUBSUB_H_
#define PUBSUB_H_

#include "mod/event.h"

void initPub(const char *address, const char *channel);

void pub(struct event *e, size_t size);

void pubRaw(const void *b);

void destroyPub(void);

void sub(struct event *e, size_t size);

void subRaw(void *b);

void initSub(const char *address, const char *channel);

void addSub(const char *address);

void destroySub(void);

#endif /* PUBSUB_H_ */
