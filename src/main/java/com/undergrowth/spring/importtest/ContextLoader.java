package com.undergrowth.spring.importtest;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * @author zhangwu
 * @version 1.0.0
 * @description ContextLoader
 * @date 2017-09-23-11:46
 */
public class ContextLoader {
    public static void main(String args[]) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(ParentConfig.class);
        Car car = (Toyota) context.getBean("toyota");
        car.print();
        car = (Volkswagen) context.getBean("volkswagen");
        car.print();
        context.close();
    }
}
