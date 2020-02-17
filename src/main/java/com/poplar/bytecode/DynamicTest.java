package com.poplar.bytecode;


import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class DynamicTest {

    public static void main(String[] args) throws InterruptedException {
        System.setProperty("sun.misc.ProxyGenerator.saveGeneratedFiles", "true");
        RealSubject realSubject = new RealSubject();
        for(;;) {
            Subject subject = (Subject) Proxy.newProxyInstance(realSubject.getClass().getClassLoader(), realSubject.getClass().getInterfaces(), new DynamicSubject(realSubject));
            subject.request();
//            RealSubject subject2 = (RealSubject) subject;
            Thread.sleep(10);
        }

//        RealSubject.response();
    }

}
