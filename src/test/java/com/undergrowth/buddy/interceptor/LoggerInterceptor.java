package com.undergrowth.buddy.interceptor;

import java.util.List;
import java.util.concurrent.Callable;
import net.bytebuddy.implementation.bind.annotation.SuperCall;

/**
 * @author zhangwu
 * @version 1.0.0
 * @date 2019-01-11-16:05
 */
public class LoggerInterceptor {

    public static List<String> log(@SuperCall Callable<List<String>> zuper)
        throws Exception {
        System.out.println("Calling database");
        try {
            return zuper.call();
        } finally {
            System.out.println("Returned from database");
        }
    }

}