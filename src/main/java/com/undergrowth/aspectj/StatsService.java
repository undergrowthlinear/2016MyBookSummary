package com.undergrowth.aspectj;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author zhangwu
 * @version 1.0.0
 * @date 2018-12-17-11:34
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.CLASS)
@Documented
public @interface StatsService {
}