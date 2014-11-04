package a;

import org.zeromq.ZMQ;

public class Subscriber {

    public static void main(String[] a){
        final ZMQ.Context ctx = ZMQ.context(1);
        final ZMQ.Socket sub = ctx.socket(ZMQ.SUB);
//        sub.connect("tcp://localhost:6001");
        sub.connect("ipc://001");
        sub.subscribe("".getBytes());

        while (true){
            String msg = sub.recvStr();
            System.out.println(msg);
        }
    }
}
