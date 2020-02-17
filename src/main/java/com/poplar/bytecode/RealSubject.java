package com.poplar.bytecode;


public class RealSubject implements Subject {

    @Override
    public void request() {
        System.out.println("From Real Subject");
    }

    @Override
    final public void response() {
        System.out.println("From Real Subject");
    }

    @Override
    public void exceptions() {
        System.out.println("From Real Subject");
    }
}
