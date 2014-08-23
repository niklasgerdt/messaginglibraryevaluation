
#include <zmq.h>
#include <time.h>
#include <assert.h>

int main()
{

    void *context = zmq_ctx_new ();
    void *publisher = zmq_socket (context, ZMQ_PUB);
    int rc = zmq_bind (publisher, "tcp://*:5501");
    assert (rc == 0);

    struct timespec ts;
    ts.tv_sec = 0;
    ts.tv_nsec = 1*1000;

	int i = 0;
    while (i < 1*1000*1000*1000){
   //  Send message to all subscribers
        char event [100];
        zmq_send(publisher, event, 100, 0);
        nanosleep (&ts, NULL);
	    i++;
    }

    zmq_close (publisher);
    zmq_ctx_destroy (context);
    return 0;

}