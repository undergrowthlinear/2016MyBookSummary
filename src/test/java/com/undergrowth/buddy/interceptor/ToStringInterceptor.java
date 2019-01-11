package com.undergrowth.buddy.interceptor;

import javax.ws.rs.core.Context;

/**
 * @author zhangwu
 * @version 1.0.0
 * @date 2019-01-10-18:26
 */
public class ToStringInterceptor {

    public static String intercept(@Context Class<?> type) {
        return type.getSimpleName();
    }
}