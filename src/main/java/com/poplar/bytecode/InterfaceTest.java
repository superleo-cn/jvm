package com.poplar.bytecode;


import java.util.ArrayList;
import java.util.List;

public class InterfaceTest  implements Runnable {

    private interface PrivateInterface {
        static String getString() {
            return "123";
        }
    }

    public static void main(String[] args) {
        System.out.println(PrivateInterface.getString());
    }

    @Override
    public void run() {

    }

    private void a() {
        List<Integer> list = new ArrayList<>();
        list.get(0);

        Runnable r = new InterfaceTest();
        r.run();
        new Thread(r).start();

    }
}
