UNITY_ROOT=../src
INC_DIRS=-Isrc -I$(UNITY_ROOT)
SYMBOLS=-DTEST
CLEANUP = rm -f bin/*

all: clean pub sub pubsub

clean:
	$(CLEANUP)

zmq: zmqpub zmqsub zmqpubsub

zmqpub:
	gcc -D_GNU_SOURCE pub.c zmqpubsub.c event.c ../modules/terminator.c times.c -o bin/zmqpub -lzmq -std=c99

zmqsub:
	gcc -D_GNU_SOURCE sub.c zmqpubsub.c event.c ../modules/terminator.c times.c -o bin/zmqsub -lzmq -std=c99

zmqpubsub:
	gcc -D_GNU_SOURCE pubsub.c zmqpubsub.c ../modules/terminator.c -o bin/zmqpubsub -lzmq -std=c99
