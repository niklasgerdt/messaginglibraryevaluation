#include <zmq.h>


typedef struct PubSub {
    void *context;
    void *publisher;
}

PubSub adv();
void unadv();
void pub(char);

PubSub adv(){
    void *context = zmq_ctx_new ();
    void *publisher = zmq_socket (context, ZMQ_PUB);
    int rc = zmq_bind (publisher, "tcp://*:5556");
    assert (rc == 0);
}

void pub(char *event){
    zmq_send(publisher, event, 10, 0);
}

void unadv(){
    zmq_close (publisher);
    zmq_ctx_destroy (context);
}    