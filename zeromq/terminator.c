#include "terminator.h"
#include <stdio.h>

void term(int signum) {
    printf("Received SIGTERM, exiting...\n");
    termsig = 1;
}

void setTerm(void){
	action.sa_handler = term;
	sigaction(SIGTERM, &action, NULL);
}

int terminate(){
	return termsig;
}


