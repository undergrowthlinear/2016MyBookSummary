package com.undergrowth.spring.importtest;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zhangwu
 * @version 1.0.0
 * @description JavaConfigA
 * @date 2017-09-23-11:43
 */
@Configuration
public class JavaConfigA {
    @Bean(name = "volkswagen")
    public Car getVolkswagen() {
        return new Volkswagen();
    }
}
