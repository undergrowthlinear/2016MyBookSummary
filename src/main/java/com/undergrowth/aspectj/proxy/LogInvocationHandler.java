package com.undergrowth.aspectj.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author zhangwu
 * @version 1.0.0
 * @date 2018-12-17-14:47
 */
public class LogInvocationHandler implements InvocationHandler {

    private static Logger log = LoggerFactory.getLogger(LogInvocationHandler.class);

    private Hello hello;

    public LogInvocationHandler(Hello hello) {
        this.hello = hello;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if ("sayHello".equals(method.getName())) {
            log.info("You said: " + Arrays.toString(args));
        }
        return method.invoke(hello, args);
    }
}