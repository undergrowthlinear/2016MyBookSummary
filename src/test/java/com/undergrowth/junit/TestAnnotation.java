package com.undergrowth.junit;

import java.util.ArrayList;
import org.junit.Test;

/**
 * @author zhangwu
 * @version 1.0.0
 * @description junit test
 * @date 2017-09-03-10:13
 */
public class TestAnnotation {

    @Test
    public void method() {
        org.junit.Assert.assertTrue(new ArrayList().isEmpty());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void outOfBounds() {
        new ArrayList<Object>().get(1);
    }

    @Test(timeout = 100)
    public void infinity() {
        while (true) {
            ;
        }
    }

}
