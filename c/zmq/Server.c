
#include <zmq.h>
#include <stdio.h>
#include <assert.h>

int main (void)
{
    //  Prepare our context and publisher
    void *context = zmq_ctx_new ();
    void *publisher = zmq_socket (context, ZMQ_PUB);
    int rc = zmq_bind (publisher, "tcp://*:5556");
    assert (rc == 0);
    rc = zmq_bind (publisher, "ipc://weather.ipc");
    assert (rc == 0);

    int i = 0;
    for (i = 0; i < 1*1000*1000; i++){
   //  Send message to all subscribers
        char event [10];
        zmq_send(publisher, event, 10, 0);
    }

    zmq_close (publisher);
    zmq_ctx_destroy (context);
    return 0;
}