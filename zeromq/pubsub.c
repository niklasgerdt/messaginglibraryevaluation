#include <zmq.h>
#include <assert.h>

int main(){
    int rc;
    void *context = zmq_ctx_new();
    void *subscriber = zmq_socket(context, ZMQ_SUB);
    rc = zmq_connect(subscriber, "tcp://localhost:5500");
    assert (rc == 0);
    rc = zmq_setsockopt(subscriber, ZMQ_SUBSCRIBE, "", 0);
    assert (rc == 0);

    void *publisher = zmq_socket (context, ZMQ_PUB);
    rc = zmq_bind (publisher, "tcp://*:5600");
    assert (rc == 0);

    char buffer[100];
    while (1){
        zmq_recv(subscriber, buffer, 100, 0);
        zmq_send(publisher, buffer, 100, 0);
    }

    zmq_close (subscriber);
    zmq_close (publisher);
    zmq_ctx_destroy (context);
    return 0;
}
