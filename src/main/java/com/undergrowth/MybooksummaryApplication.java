package com.undergrowth;

import com.undergrowth.aspectj.AspectTest;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableAsync
@SpringBootApplication
public class MybooksummaryApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(MybooksummaryApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        AspectTest.main(null);
    }
}
