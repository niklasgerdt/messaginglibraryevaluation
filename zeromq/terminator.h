#ifndef TERMINATOR_H_
#define TERMINATOR_H_

#include <signal.h>

struct sigaction action;

int termsig;

void term(int signum);

int terminate();

void setTerm(void);

#endif /* TERMINATOR_H_ */
