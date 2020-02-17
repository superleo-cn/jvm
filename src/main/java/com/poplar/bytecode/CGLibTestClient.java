package com.poplar.bytecode;

import net.sf.cglib.core.DebuggingClassWriter;
import net.sf.cglib.proxy.Enhancer;

public class CGLibTestClient {
    public static void main(String[] args) {
        System.setProperty(DebuggingClassWriter.DEBUG_LOCATION_PROPERTY, "/Users/kylin/Documents/dev/java/opensource/jvm-learn/");
        Enhancer enhancer = new Enhancer();
        //继承被代理类
        enhancer.setSuperclass(HelloService.class);
        //设置回调
        enhancer.setCallback(new HelloMethodInterceptor());
        //设置代理类对象
        HelloService helloService = (HelloService) enhancer.create();
        //在调用代理类中方法时会被我们实现的方法拦截器进行拦截
        helloService.sayBey();
    }
}
