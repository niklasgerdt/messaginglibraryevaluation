#include <assert.h>
#include <stdio.h>
#include <stdlib.h>
#include "mod/util.h"
#include "mod/event.h"
#include "pubsub.h"

static char *channel = "\0";
static int stockEventLen = 100;

int main(int argc, char *argv[]) {
	char *pubAddr = argv[1];
	char *subAddr[4];
	for (int i = 1; i < argc; i++) {
		subAddr[i-1]= argv[i + 1];
	}

	initPub(pubAddr, channel);
	initSub(subAddr[0], channel);
//	addSub(sub[1]);
//	addSub(sub[2]);
//	addSub(sub[3]);
	initTerminator();

	size_t size = sizeof(struct event) + stockEventLen * sizeof(char);

	while (killSignal == 0) {
		struct event e;

		sub(&e, size);
//		printf("%c;%c;%d;%lld.%09ld;%lld.%09ld;%lld.%09ld\n", e.header.source, e.header.destination, e.header.id,
//				e.header.created.tv_sec, e.header.created.tv_nsec, e.header.published.tv_sec,
//				e.header.published.tv_nsec, e.header.routed.tv_sec, e.header.routed.tv_nsec);
		pub(&e, size);
	}

	destroyPub();
	destroySub();
	return 0;

}

