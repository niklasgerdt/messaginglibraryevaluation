#include <zmq.h>
#include <assert.h>
#include <signal.h>
#include <stdio.h>
#include "../modules/terminator.h"

//char events[100][10000];

int main(int argc, char *argv[]){
	initTerminator();

	printf("Setting up ZeroMQ pubsub-system\n");
    int rc;
    void *context = zmq_ctx_new();
    void *subscriber = zmq_socket(context, ZMQ_SUB);
    rc = zmq_connect(subscriber, argv[1]);
    assert (rc == 0);
    rc = zmq_setsockopt(subscriber, ZMQ_SUBSCRIBE, "", 0);
    assert (rc == 0);

    printf("ZeroMQ up\n");
    char buffer[100];
    while (killSignal == 0){
        zmq_recv(subscriber, buffer, 100, 0);
        printf("%s\n", buffer);
    }

    printf("ZeroMQ down\n");
    zmq_close (subscriber);
    zmq_ctx_destroy (context);
    return 0;
}
