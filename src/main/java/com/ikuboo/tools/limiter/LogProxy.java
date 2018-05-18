package com.ikuboo.tools.limiter;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class LogProxy implements InvocationHandler {
    private LogProxy() {
    }

    private Object target;

    public static Object getInstance(Object o) {
        LogProxy pm = new LogProxy();
        pm.target = o;
        Object result = Proxy.newProxyInstance(o.getClass().getClassLoader(), o
                .getClass().getInterfaces(), pm);
        return result;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args)
            throws Throwable {
        if(method.isAnnotationPresent(LimiterAnn.class)){
            LimiterAnn  la = method.getAnnotation(LimiterAnn .class);
            System.out.println(la.limiter() + "--->执行的是"+method.getName()+"方法.");
        }
        Object obj = method.invoke(target, args);
        return obj;
    }
}
