package com.undergrowth.aspectj.proxy;

import java.lang.reflect.Proxy;

/**
 * @author zhangwu
 * @version 1.0.0
 * @date 2018-12-17-14:49
 */
public class StaticAndDynamicProxyTest {

    public static void main(String[] args) {
        String helloStr = "hello world";
        StaticProxiedHello staticProxiedHello = new StaticProxiedHello();
        staticProxiedHello.sayHello(helloStr);
        //
        Hello hello = (Hello) Proxy.newProxyInstance(
            StaticAndDynamicProxyTest.class.getClassLoader(), // 1. 类加载器
            new Class<?>[]{Hello.class}, // 2. 代理需要实现的接口，可以有多个
            new LogInvocationHandler(new HelloImp()));// 3. 方法调用的实际处理者
        hello.sayHello(helloStr);
        // System.out.println(hello.sayHello(helloStr));
    }

}