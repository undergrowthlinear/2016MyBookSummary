package com.undergrowth.aspectj;

import java.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author zhangwu
 * @version 1.0.0
 * @date 2018-12-17-11:34
 */
public class AspectTest {

    private static Logger log = LoggerFactory.getLogger(AspectTest.class);

    public static void main(String[] args) {
        AspectTest.print();
    }

    @StatsService
    public static void print() {
        log.info("Now: {}", LocalDateTime.now());
    }

}