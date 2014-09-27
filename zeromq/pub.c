#include <zmq.h>
#include <assert.h>
#include <signal.h>
#include <stdio.h>

int kill = 0;

void term(int signum)
{
    printf("Received SIGTERM, exiting...\n");
    kill = 1;
}

void pause(long nanos){

}

int main(int argc, char *argv[]){
    struct sigaction action;
    action.sa_handler = term;
    sigaction(SIGTERM, &action, NULL);
 
    int rc;
    void *context = zmq_ctx_new();
    void *publisher = zmq_socket (context, ZMQ_PUB);
    rc = zmq_bind (publisher, "tcp://*:5500");
    assert (rc == 0);

    char buffer[100];
    while (kill == 0){
        zmq_send(publisher, "test-message", 12, 0);
    }


    zmq_close (publisher);
    zmq_ctx_destroy (context);
    return 0;
}
