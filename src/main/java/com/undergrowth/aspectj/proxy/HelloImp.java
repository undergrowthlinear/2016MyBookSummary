package com.undergrowth.aspectj.proxy;

/**
 * @author zhangwu
 * @version 1.0.0
 * @date 2018-12-17-14:46
 */
public class HelloImp implements Hello {
    @Override
    public String sayHello(String str) {
        return "HelloImp: " + str;
    }
}