#ifndef PUBSUB_H_
#define PUBSUB_H_

#include "mod/event.h"

void initPub(const char *address, const char *channel);

void pub(struct event e, size_t size);

void destroyPub(void);

struct event sub(size_t size);

void initSub(const char *address, const char *channel);

void addSub(const char *address);

void destroySub(void);

#endif /* PUBSUB_H_ */
