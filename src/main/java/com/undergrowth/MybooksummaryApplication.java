package com.undergrowth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class MybooksummaryApplication {

  public static void main(String[] args) {
    SpringApplication.run(MybooksummaryApplication.class, args);
  }
}
