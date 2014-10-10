#include <assert.h>
#include <stdio.h>
#include <stdlib.h>
#include "mod/util.h"
#include "mod/event.h"
#include "pubsub.h"

int main(int argc, char *argv[]) {
	char *pubAddr = argv[1];
	char *subAddr[4];
	for (int i = 1; i < argc; i++) {
		subAddr[i - 1] = argv[i + 1];
	}

	char *channel = "";
	initPub(pubAddr, channel);
	initSub(subAddr[0], channel);
	int i = 1;
	while (subAddr[i] != NULL && i < 5) {
		addSub(subAddr[i]);
		i++;
	}
	initTerminator();

	while (killSignal == 0) {
		med();
	}

	printf("destroying connections");
	destroyPub();
	destroySub();
	return 0;

}

