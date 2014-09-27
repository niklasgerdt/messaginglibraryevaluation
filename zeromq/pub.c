#include <zmq.h>
#include <assert.h>
#include <signal.h>
#include <stdio.h>

int termsig = 0;
void term(int signum)
{
    printf("Received SIGTERM, exiting...\n");
    termsig = 1;
}

void pause(long nanos){

}

int main(int argc, char *argv[]){
    struct sigaction action;
    action.sa_handler = term;
    sigaction(SIGTERM, &action, NULL);
 
    printf("Setting up ZeroMQ pubsub-system\n");
    int rc;
    void *context = zmq_ctx_new();
    void *publisher = zmq_socket (context, ZMQ_PUB);
    rc = zmq_bind (publisher, "tcp://*:5501");
    assert (rc == 0);

    printf("ZeroMQ up\n");
    char buffer[100];
    while (termsig == 0){
        zmq_send(publisher, "test-message", 12, 0);
    }
    printf("ZeroMQ down\n");

    zmq_close (publisher);
    zmq_ctx_destroy (context);
    return 0;
}
