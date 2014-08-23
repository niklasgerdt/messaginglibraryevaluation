#include <zmq.h>
#include <stdio.h>
#include <assert.h>
#include <time.h>

int main ()
{
    void *context = zmq_ctx_new ();
    void *subscriber = zmq_socket (context, ZMQ_SUB);
    int rc = zmq_connect (subscriber, "tcp://localhost:5500");
    assert (rc == 0);

    char *filter = "";
    rc = zmq_setsockopt(subscriber, ZMQ_SUBSCRIBE, filter, 0);
    assert (rc == 0);

    struct timespec ts;
    clock_gettime(CLOCK_REALTIME, &ts);

    int i = 0;

    while (1) {
        char buffer [100000];
        zmq_recv(subscriber, buffer, 100000, 0);
        i++;
        if (i % 10000 == 0){
            struct timespec ts2;
            clock_gettime(CLOCK_REALTIME, &ts2);
            long nanos = 0;
            if (ts2.tv_nsec >= ts.tv_nsec){
                nanos = ts2.tv_nsec - ts.tv_nsec;
            }
            else {
                nanos = ts2.tv_nsec + (-1.0 + ts.tv_nsec);
            }

            printf("received 10000 news in %d nanoseconds\n", nanos);          
            clock_gettime(CLOCK_REALTIME, &ts);     
        }
    }

    zmq_close (subscriber);
    zmq_ctx_destroy (context);
    return 0;
}