package com.undergrowth.filter;

import org.junit.Assert;
import org.junit.Test;

/**
 * XSSCheckFilter
 *
 * @author zhangwu
 * @version 1.0.0
 * @date 2018-08-31-14:50
 */
public class XSSCheckFilterTest {

    @Test
    public void test(){
        Assert.assertEquals("核查错误",true,isSafe("http://localhost:8080/helloWorld/hello?name=%27he%27"));
        Assert.assertEquals("核查错误",false,isSafe("http://localhost:8080/helloWorld/hello?name=%27%3Cscript%27"));
        Assert.assertEquals("核查错误",false,isSafe("http://localhost:8080/helloWorld/hello?name=%27<script%27"));
        Assert.assertEquals("核查错误",false,isSafe("http://localhost:8080/helloWorld/hello?name=%27%3C/script%27"));
        Assert.assertEquals("核查错误",false,isSafe("http://localhost:8080/helloWorld/hello?name=%27%3Ciframe%27"));
        Assert.assertEquals("核查错误",false,isSafe("http://localhost:8080/helloWorld/hello?name=%27%3C/iframe%27"));
        Assert.assertEquals("核查错误",false,isSafe("http://localhost:8080/helloWorld/hello?name=%27%3Cframe%27"));
        Assert.assertEquals("核查错误",false,isSafe("http://localhost:8080/helloWorld/hello?name=%27%3C/frame%27"));
        Assert.assertEquals("核查错误",false,isSafe("http://localhost:8080/helloWorld/hello?name=%27%3Cset-cookie%27"));
    }

    private static boolean isSafe(String str) {
        if (null != str && str.length() > 0) {
            for (String s : XSSCheckFilter.getSafeless()) {
                if (str.toLowerCase().contains(s)) {
                    return false;
                }
            }
        }
        return true;
    }


}