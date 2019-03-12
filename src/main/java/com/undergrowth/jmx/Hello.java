package com.undergrowth.jmx;

/**
 * @author zhangwu
 * @version 1.0.0
 * @description hello
 * @date 2017-09-10-10:14
 */
public class Hello implements HelloMBean {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void printHello() {
        System.out.println("Hello World, " + name);
    }

    public void printHello(String whoName) {
        System.out.println("Hello , " + whoName);
    }
}
