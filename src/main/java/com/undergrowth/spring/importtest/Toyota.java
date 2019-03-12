package com.undergrowth.spring.importtest;

import org.springframework.stereotype.Component;

/**
 * @author zhangwu
 * @version 1.0.0
 * @description Toyota
 * @date 2017-09-23-11:42
 */
@Component
public class Toyota implements Car {
    public void print() {
        System.out.println("I am Toyota");
    }
}
