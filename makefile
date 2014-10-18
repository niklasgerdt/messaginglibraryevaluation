MAIN=./src/main/c/
TEST=src/test/c/
UNITY_ROOT=$(TEST)/unity/src
INC_DIRS=-Isrc -I$(UNITY_ROOT)
SYMBOLS=-DTEST
CLEANUP = rm -f bin/* logs/*

all: clean zmq nano dummy

clean:
	$(CLEANUP)

zmq: zmqpub zmqsub zmqpubsub

zmqpub:
	gcc -D_GNU_SOURCE $(MAIN)pub.c $(MAIN)mom/zeromq.c $(MAIN)mod/event.c $(MAIN)mod/util.c -o bin/zmqpub -lzmq -std=c99

zmqsub:
	gcc -D_GNU_SOURCE $(MAIN)sub.c $(MAIN)mom/zeromq.c $(MAIN)mod/event.c $(MAIN)mod/util.c -o bin/zmqsub -lzmq -std=c99

zmqpubsub:
	gcc -D_GNU_SOURCE $(MAIN)pubsub.c $(MAIN)mom/zeromq.c $(MAIN)mod/util.c -o bin/zmqpubsub -lzmq -std=c99
	
nano: nanopub nanosub nanopubsub

nanopub:
	gcc -D_GNU_SOURCE -lrt $(MAIN)pub.c $(MAIN)mom/nanomsg.c $(MAIN)mod/event.c $(MAIN)mod/util.c -o bin/nanopub -lnanomsg -std=c99

nanosub:
	gcc -D_GNU_SOURCE -lrt $(MAIN)sub.c $(MAIN)mom/nanomsg.c $(MAIN)mod/event.c $(MAIN)mod/util.c -o bin/nanosub -lnanomsg -std=c99

nanopubsub:
	gcc -D_GNU_SOURCE -lrt $(MAIN)pubsub.c $(MAIN)mom/nanomsg.c $(MAIN)mod/util.c -o bin/nanopubsub -lnanomsg -std=c99

dummy:
	gcc -D_GNU_SOURCE -lrt $(MAIN)pub.c $(MAIN)mom/dummy.c $(MAIN)mod/event.c $(MAIN)mod/util.c -o bin/dummypub -lnanomsg -std=c99

tests:
	gcc $(TEST)sizeofspike.c -o bin/test.o
	bin/test.o
	$(CLEANUP)
	gcc -D_GNU_SOURCE $(TEST)eventTest.c -o bin/test.o -std=c99
	bin/test.o
