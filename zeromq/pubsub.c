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

int main(){
    struct sigaction action;
    action.sa_handler = term;
    sigaction(SIGTERM, &action, NULL);

    printf("Setting up ZeroMQ pubsub-system\n");
    int rc;
    void *context = zmq_ctx_new();
    void *subscriber = zmq_socket(context, ZMQ_SUB);
    rc = zmq_connect(subscriber, "tcp://168.1.1.2:5501");
    assert (rc == 0);
    rc = zmq_connect(subscriber, "tcp://168.1.1.2:5502");
    assert (rc == 0);
    rc = zmq_connect(subscriber, "tcp://168.1.1.2:5503");
    assert (rc == 0);
    rc = zmq_connect(subscriber, "tcp://168.1.1.2:5504");
    assert (rc == 0);
    rc = zmq_connect(subscriber, "tcp://168.1.1.2:5505");
    assert (rc == 0);
    rc = zmq_connect(subscriber, "tcp://168.1.1.2:5506");
    assert (rc == 0);
    rc = zmq_setsockopt(subscriber, ZMQ_SUBSCRIBE, "", 0);
    assert (rc == 0);
    void *publisher = zmq_socket (context, ZMQ_PUB);
    rc = zmq_bind(publisher, "tcp://168.1.1.1:5601");
    assert (rc == 0);
    
    printf("ZeroMQ up\n");
    char buffer[100];
    while (termsig == 0){
        zmq_recv(subscriber, buffer, 100, 0);
        zmq_send(publisher, buffer, 100, 0);
    }
    printf("ZeroMQ down\n");

    zmq_close (subscriber);
    zmq_close (publisher);
    zmq_ctx_destroy (context);
    return 0;
}
