UNITY_ROOT=../src
INC_DIRS=-Isrc -I$(UNITY_ROOT)
SYMBOLS=-DTEST
CLEANUP = rm -f bin/*

all: clean pub sub pubsub

clean:
	$(CLEANUP)

pub:
	gcc -D_GNU_SOURCE pub.c event.c ../modules/terminator.c times.c -o bin/pub -lzmq -std=c99 -g

sub:
	gcc sub.c ../modules/terminator.c -o bin/sub -lzmq

pubsub:
