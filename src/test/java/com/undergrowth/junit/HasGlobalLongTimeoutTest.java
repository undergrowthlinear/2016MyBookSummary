package com.undergrowth.junit;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

/**
 * @author zhangwu
 * @version 1.0.0
 * @description HasGlobalLongTimeout
 * @date 2017-09-03-10:52
 */
public class HasGlobalLongTimeoutTest {

    @Rule
    public Timeout globalTimeout = new Timeout(20);

    @Test
    public void run1() throws InterruptedException {
        Thread.sleep(100);
    }

    @Test
    public void infiniteLoop() {
        while (true) {
        }
    }

}
