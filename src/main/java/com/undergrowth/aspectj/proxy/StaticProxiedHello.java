package com.undergrowth.aspectj.proxy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author zhangwu
 * @version 1.0.0
 * @date 2018-12-17-14:46
 */
public class StaticProxiedHello implements Hello {

    private static Logger log = LoggerFactory.getLogger(StaticProxiedHello.class);

    private Hello hello = new HelloImp();

    @Override
    public String sayHello(String str) {
        log.info("You said: " + str);
        return hello.sayHello(str);
    }
}