package com.poplar.bytecode;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

public class HelloMethodInterceptor implements MethodInterceptor {
    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        System.out.println("Before: " + method.getName());
        Object object = methodProxy.invokeSuper(o, objects);
//        Object object = method.invoke(o, objects);
        System.out.println("After: " + method.getName());
        return object;
    }
}
