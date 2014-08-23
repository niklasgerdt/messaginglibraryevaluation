#include <stdio.h>
#include <zmq.h>
#include <assert.h>
#include "../NotificationService.h"

NotificationService createNotificationService(NotificationServiceDescription desc){
	printf("Creating Notification Service..\n");
	NotificationService nos;
	
	int rc;
    nos.context = zmq_ctx_new ();
    
    nos.publisher = zmq_socket (nos.context, ZMQ_PUB);
    rc = zmq_bind (nos.publisher, desc.address);
    assert (rc == 0);

    int i;
    nos.subscriber = zmq_socket(nos.context, ZMQ_SUB);
    for (i = 0; i < 4; i++){
   	    rc = zmq_connect(nos.subscriber, desc.publisherAdresses[i]);
   	    assert (rc == 0);
    }

    return nos;
}

void up(NotificationService nos){
    printf("Starting Notification Server\n");

    char buffer[100000];
    while (1){
        zmq_recv(nos.subscriber, buffer, 100000, 0);
        printf("received msg\n");
        zmq_send(nos.publisher, buffer, 100000, 0);
    }

    zmq_close(nos.subscriber);
    zmq_close(nos.publisher);
    zmq_ctx_destroy(nos.context);
}
