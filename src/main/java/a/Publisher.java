package a;

import org.zeromq.ZMQ;

public class Publisher {

    public static void main(String[] a) throws InterruptedException {
        final ZMQ.Context ctx = ZMQ.context(1);
        final ZMQ.Socket pub = ctx.socket(ZMQ.PUB);
        pub.bind("ipc://001");
//        pub.bind("tcp://*:6001");

        while (true){
            System.out.println("Publishing");
            pub.send("Test");
            Thread.sleep(1000);
        }
    }
}
