package com.undergrowth.aspectj;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author zhangwu
 * @version 1.0.0
 * @date 2018-12-17-11:33
 */
@Aspect
public class StatsServiceInterceptor {

    private static Logger log = LoggerFactory.getLogger(StatsServiceInterceptor.class);

    @Pointcut("@annotation(com.undergrowth.aspectj.StatsService)")
    public void statsServiceAnnotationPointcut() {
    }

    @Around("execution(* *(..)) && statsServiceAnnotationPointcut()")
    public Object invoke(ProceedingJoinPoint pjp) {
        try {
            log.info("before invoke target.");
            return pjp.proceed();
        } catch (Throwable e) {
            log.error("invoke occurs error:", e);
            return null;
        } finally {
            log.info("after invoke target.");
        }
    }

}
