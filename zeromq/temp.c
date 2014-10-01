#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#define BILLION 1000000000
#define SIZE 100000000
#define MILLION 1000000

typedef struct eventHeader {
	char source[1];
	char id[9];
	char created[14];
	char published[14];
	char routed[14];
} eventHeader;

//static eventHeader events[MILLION];


int main(int argc, char const *argv[]) {
	eventHeader* events = malloc(BILLION * sizeof(eventHeader)); // 5.2G

	for (int i = 0; i < BILLION; i++) {
		strcpy(events[i].source, "a");
		strcpy(events[i].id, "1234567890");
		strcpy(events[i].created, "12345678901234");
		strcpy(events[i].published, "12345678901234");
		strcpy(events[i].routed, "12345678901234");
	}
	printf("ds created");
	int value;
	scanf("%d", &value);
	free(events);
	return 0;
}
