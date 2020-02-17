package com.poplar.classload;

/**
 * Created By poplar on 2019/11/7
 */
public class ClassLoadTest8 {

    static {
        System.out.println("ClassLoadTest9");
    }

    public static void main(String[] args) {
        System.out.println(Test8.t);
    }
}


class Test8 {
    static {
        System.out.println("Test 8 ========");
    }

    public static final Thread t =  new Thread() {
        {
            System.out.println("Test 8 thread");
        }
    };
}

