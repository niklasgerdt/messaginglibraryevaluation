
#include <zmq.h>
#include <stdio.h>
#include <assert.h>

int main (int argc, char *argv [])
{
    //  Socket to talk to server
    printf ("Collecting updates from weather server…\n");
    void *context = zmq_ctx_new ();
    void *subscriber = zmq_socket (context, ZMQ_SUB);
    int rc = zmq_connect (subscriber, "tcp://localhost:5556");
    assert (rc == 0);

    //  Subscribe to zipcode, default is NYC, 10001
    char *filter = "";
    rc = zmq_setsockopt(subscriber, ZMQ_SUBSCRIBE, filter, 0);
    assert (rc == 0);

    while (1) {
        char buffer [10];
        zmq_recv(subscriber, buffer, 255, 0);
        printf("*");
    }

    zmq_close (subscriber);
    zmq_ctx_destroy (context);
    return 0;
}