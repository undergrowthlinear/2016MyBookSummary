package com.undergrowth.spring.importtest;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author zhangwu
 * @version 1.0.0
 * @description JavaConfigB
 * @date 2017-09-23-11:44
 */
@Configuration
public class JavaConfigB {
  @Bean(name="toyota")
  public Car getToyota(){
    return new Toyota();
  }
}
