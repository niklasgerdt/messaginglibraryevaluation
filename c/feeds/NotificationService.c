
#include <zmq.h>
#include <time.h>
#include <assert.h>

int main()
{
    int rc;
    void *context = zmq_ctx_new ();
    void *subscriber = zmq_socket(context, ZMQ_SUB);
    rc = zmq_connect(subscriber, "tcp://localhost:5501");
    assert (rc == 0);
    rc = zmq_connect(subscriber, "tcp://localhost:5502");
    assert (rc == 0);
    rc = zmq_connect(subscriber, "tcp://localhost:5503");
    assert (rc == 0);
    rc = zmq_connect(subscriber, "tcp://localhost:5504");
    assert (rc == 0);
    rc = zmq_connect(subscriber, "tcp://localhost:5505");
    assert (rc == 0);
    rc = zmq_connect(subscriber, "tcp://localhost:5506");
    assert (rc == 0);
    rc = zmq_connect(subscriber, "tcp://localhost:5507");
    assert (rc == 0);
    rc = zmq_connect(subscriber, "tcp://localhost:5508");
    assert (rc == 0);
    rc = zmq_connect(subscriber, "tcp://localhost:5509");
    assert (rc == 0);
    rc = zmq_setsockopt(subscriber, ZMQ_SUBSCRIBE, "", 0);
    assert (rc == 0);

    void *publisher = zmq_socket (context, ZMQ_PUB);
    rc = zmq_bind (publisher, "tcp://*:5500");
    assert (rc == 0);

    printf("Starting Notification Server\n");

	int i = 0;
    char buffer[100000];
    while (1){
        zmq_recv(subscriber, buffer, 100000, 0);
        printf("received msg\n");
        zmq_send(publisher, buffer, 100000, 0);
    }

    zmq_close (subscriber);
    zmq_close (publisher);
    zmq_ctx_destroy (context);
    return 0;
}