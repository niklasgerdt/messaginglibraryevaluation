#include <assert.h>
#include <stdio.h>
#include <stdlib.h>
#include "mod/util.h"
#include "pubsub.h"

char channel = '\0';

int main(int argc, char *argv[]) {
	char *pub = argv[1];
	int msgLen = atoi(argv[2]);
	char *sub = argv[3];
	initTerminator();
	initPub(pub, channel);
	initSub(sub, channel);
	printf("Running with params: %s, %s, %d\n", pub, sub, msgLen);

	char buffer[100];
	void *buf;
	while (killSignal == 0) {
		subRaw(buf);
		printf("mediating %s", buf);
		pubRaw(buf);
	}

	destroyPub();
	destroySub();
	return 0;

}

