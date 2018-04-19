package com.undergrowth.spring.importtest;

import org.springframework.stereotype.Component;

/**
 * @author zhangwu
 * @version 1.0.0
 * @description Volkswagen
 * @date 2017-09-23-11:43
 */
@Component
public class Volkswagen implements Car{
  public void print(){
    System.out.println("I am Volkswagen");
  }
}
