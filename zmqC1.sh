PAUSE=$1

bin/zmqpub $PAUSE tcp://168.1.1.1:5001 A 100 &
bin/zmqpub $PAUSE tcp://168.1.1.1:5002 B 100 &
bin/zmqpub $PAUSE tcp://168.1.1.1:5003 C 100 &
bin/zmqpub $PAUSE tcp://168.1.1.1:5004 D 100 &
bin/zmqsub tcp://168.1.1.2:6001 N &
bin/zmqsub tcp://168.1.1.2:6001 N &
bin/zmqsub tcp://168.1.1.2:6001 N &
bin/zmqsub tcp://168.1.1.2:6001 N &