package com.undergrowth;

import org.junit.Test;

/**
 * 线程中断异常
 *
 * @author zhangwu
 * @version 1.0.0
 * @date 2018-08-13-10:35
 */
public class ThreadInterTest {

    @Test
    public void interTest() {
        Thread.currentThread().interrupt();
        throw new RuntimeException("test " + Thread.currentThread().getName());
    }


}
