package com.undergrowth.junit;

import com.google.common.collect.Lists;
import java.util.List;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author zhangwu
 * @version 1.0.0
 * @description BeforeClass
 * @date 2017-09-03-10:29
 */
public class BeforeClassTest {

    private static List array;

    @BeforeClass
    public static void onlyOnce() {
        array = Lists.newArrayList(1, 2);
    }

    @Test
    public void one() {
        Assert.assertEquals(2, array.size());
    }

    @Test
    public void two() {

    }

    @AfterClass
    public static void logout() {

    }

}
