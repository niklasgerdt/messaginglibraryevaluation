#include "terminator.h"
#include <stdio.h>
#include <signal.h>

static struct sigaction action;

void term(int signum) {
    printf("Received SIGTERM, exiting...\n");
    killSignal = 1;
}

void initTerminator(void){
	action.sa_handler = term;
	sigaction(SIGTERM, &action, NULL);
    printf("Set up term signal handler\n");
}


