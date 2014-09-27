#include <zmq.h>
#include <assert.h>
#include <signal.h>

int termsig = 0;
void term(int signum)
{
    printf("Received SIGTERM, exiting...\n");
    termsig = 1;
}

//char events[100][10000];

int main(){
    struct sigaction action;
    action.sa_handler = term;
    sigaction(SIGTERM, &action, NULL);

    int rc;
    void *context = zmq_ctx_new();
    void *subscriber = zmq_socket(context, ZMQ_SUB);
    rc = zmq_connect(subscriber, "tcp://168.1.1.2:5601");
    assert (rc == 0);
    rc = zmq_setsockopt(subscriber, ZMQ_SUBSCRIBE, "", 0);
    assert (rc == 0);

    char buffer[100];
    while (termsig == 0){
        zmq_recv(subscriber, buffer, 100, 0);
    }

    zmq_close (subscriber);
    zmq_ctx_destroy (context);
    return 0;
}
