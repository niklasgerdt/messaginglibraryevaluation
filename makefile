MAIN=./src/main/c/
TEST=src/test/c/
UNITY_ROOT=$(TEST)/unity/src
INC_DIRS=-Isrc -I$(UNITY_ROOT)
SYMBOLS=-DTEST
CLEANUP = rm -f bin/*

all: clean zmq nano beans

clean:
	$(CLEANUP)

zmq: zmqpub zmqsub zmqpubsub

zmqpub:
	gcc -D_GNU_SOURCE $(MAIN)pub.c $(MAIN)mom/zeromq.c $(MAIN)mod/event.c $(MAIN)mod/util.c -o bin/zmqpub -lzmq -std=c99

zmqsub:
	gcc -D_GNU_SOURCE $(MAIN)sub.c $(MAIN)mom/zeromq.c $(MAIN)mod/event.c $(MAIN)mod/util.c -o bin/zmqsub -lzmq -std=c99

zmqpubsub:
	gcc -D_GNU_SOURCE $(MAIN)pubsub.c $(MAIN)mom/zeromq.c $(MAIN)mod/util.c -o bin/zmqpubsub -lzmq -std=c99
	
runzmq-N1-C1-P1000000: all
	bin/zmqpub 100000000 tcp://168.1.1.1:5001 A 100 &
	sleep 1
	bin/zmqpub 10000000 tcp://168.1.1.1:5002 B 100 &
	sleep 1
	bin/zmqpub 10000000 tcp://168.1.1.1:5003 C 100 &
	sleep 1
	bin/zmqpub 10000000 tcp://168.1.1.1:5004 D 100 &
	sleep 1
	bin/zmqsub tcp://168.1.1.2:6001 A &
	sleep 1
	bin/zmqsub tcp://168.1.1.2:6001 B &
	sleep 1
	bin/zmqsub tcp://168.1.1.2:6001 C &
	sleep 1
	bin/zmqsub tcp://168.1.1.2:6001 D &
	read sig;
	killall zmqsub zmqpub

runzmq-N2: all
	bin/zmqpubsub tcp://168.1.1.2:6001 tcp://168.1.1.1:5001 tcp://168.1.1.1:5002 tcp://168.1.1.1:5003 tcp://168.1.1.1:5004 &
	read sig;
	killall zmqsub zmqpub

runzmq-spike: all
#	bin/zmqpubsub tcp://*:6001 tcp://localhost:5001 tcp://localhost:5002 tcp://localhost:5003 tcp://localhost:5004 &
	bin/zmqsub tcp://localhost:6001 A &
	bin/zmqsub tcp://localhost:6001 B &
	bin/zmqsub tcp://localhost:6001 N &
	bin/zmqsub tcp://localhost:6001 AB &
	bin/zmqpub 1000000 tcp://*:5001 A 100 &
	bin/zmqpub 1000000 tcp://*:5002 B 100 &
	bin/zmqpub 1000000 tcp://*:5003 C 100 &
	bin/zmqpub 1000000 tcp://*:5004 D 100 &
	read sig;
	killall zmqsub zmqpubsub zmqpub
	
nano: nanopub nanosub nanopubsub

nanopub:
	gcc -D_GNU_SOURCE -lrt $(MAIN)pub.c $(MAIN)mom/nanomsg.c $(MAIN)mod/event.c $(MAIN)mod/util.c -o bin/nanopub -lnanomsg -std=c99

nanosub:
	gcc -D_GNU_SOURCE -lrt $(MAIN)sub.c $(MAIN)mom/nanomsg.c $(MAIN)mod/event.c $(MAIN)mod/util.c -o bin/nanosub -lnanomsg -std=c99

nanopubsub:
	gcc -D_GNU_SOURCE -lrt $(MAIN)pubsub.c $(MAIN)mom/nanomsg.c $(MAIN)mod/util.c -o bin/nanopubsub -lnanomsg -std=c99

runnano-spike: all
	bin/nanopubsub tcp://*:6001 tcp://localhost:5001 tcp://localhost:5002 tcp://localhost:5003 tcp://localhost:5004 &
	bin/nanosub tcp://localhost:6001 A &
	bin/nanosub tcp://localhost:6001 B &
	bin/nanosub tcp://localhost:6001 AB &
	bin/nanosub tcp://localhost:6001 N &
	bin/nanopub 100000 tcp://*:5001 A 100 &
	bin/nanopub 100000 tcp://*:5002 B 100 &
	bin/nanopub 100000 tcp://*:5003 C 100 &
	bin/nanopub 100000 tcp://*:5004 D 100 &
	read sig;
	killall nanosub nanopubsub nanopub

beans: beanspub beanssub

beanspub:
	gcc -D_GNU_SOURCE -lrt $(MAIN)pub.c $(MAIN)mom/beanstalkd.c $(MAIN)mod/event.c $(MAIN)mod/util.c -o bin/beanspub -lbeanstalk -std=c99

beanssub:
	gcc -D_GNU_SOURCE -lrt $(MAIN)sub.c $(MAIN)mom/beanstalkd.c $(MAIN)mod/event.c $(MAIN)mod/util.c -o bin/beanssub -lbeanstalk -std=c99

runbeans-spike: all
	beanstalkd -l 127.0.0.1 -p 5001 &
	sleep 1
	bin/beanssub 127.0.0.1:5001 A &
	sleep 1
	bin/beanssub 127.0.0.1:5001 B &
	sleep 1
	bin/beanssub 127.0.0.1:5001 AB &
	sleep 1
	bin/beanssub 127.0.0.1:5001 N &
	sleep 1
	bin/beanspub 100000 127.0.0.1:5001 A 100 &
	sleep 1
	bin/beanspub 100000 127.0.0.1:5001 B 100 &
	sleep 1
	bin/beanspub 100000 127.0.0.1:5001 C 100 &
	sleep 1
	bin/beanspub 100000 127.0.0.1:5001 N 100 &
	read sig;
	killall beanssub beanspub
	sleep 1
	killall beanstalkd 

tests:
	gcc $(TEST)sizeofspike.c -o bin/test.o
	bin/test.o
	$(CLEANUP)
	gcc -D_GNU_SOURCE $(TEST)eventTest.c -o bin/test.o -std=c99
	bin/test.o
