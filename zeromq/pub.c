#include <zmq.h>
#include <assert.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "times.h"
#include "terminator.h"

int main(int argc, char *argv[]){
    setPauseLenNanos(argv[1]);
    setTerm();
 
    printf("Setting up ZeroMQ pubsub-system\n");
    int rc;
    void *context = zmq_ctx_new();
    void *publisher = zmq_socket(context, ZMQ_PUB);
    rc = zmq_bind (publisher, argv[2]);
    assert (rc == 0);   
    printf("ZeroMQ up\n");  
    printf("Pub bind to %s\n", argv[2]);

    char buffer[100];
    while (terminate() == 0){
        zmq_send(publisher, "test-message", 12, 0);
        pause(currentNanos());
        printf("sent\n");
    }
    printf("ZeroMQ down\n");

    zmq_close (publisher);
    zmq_ctx_destroy (context);
    return 0;
}
