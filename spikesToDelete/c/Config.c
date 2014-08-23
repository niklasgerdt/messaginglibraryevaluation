#include <string.h>
#include "NotificationService.h"

#if ZMQ
#include "zmq/ZmqNotificationService.c"
#endif


int maxaa = 5;
//char NOS[30] = "tcp://*:5500";
//char FEEDS[30] = "tcp://*:5500";
char nosaa[30];

void setup();

void setup(){
	printf("Setting up HFT system...\n");

	NotificationServiceDescription desc;
	strcpy(desc.address, "tcp://*:5500");
	strcpy(desc.publisherAdresses[0], "tcp://localhost:5501");
	strcpy(desc.publisherAdresses[1], "tcp://localhost:5502");
	strcpy(desc.publisherAdresses[2], "tcp://localhost:5503");
	strcpy(desc.publisherAdresses[3], "tcp://localhost:5504");
	
	NotificationService nos = createNotificationService(desc);
	up(nos);
}