PAUSE=$1

bin/dummypub $PAUSE tcp://0.0.0.0:0000 A 100 &
bin/dummypub $PAUSE tcp://0.0.0.0:0000 B 100 &
bin/dummypub $PAUSE tcp://0.0.0.0:0000 C 100 &
bin/dummypub $PAUSE tcp://0.0.0.0:0000 D 100 &

read sig;
killall dummypub