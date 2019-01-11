package com.undergrowth.buddy;

import static net.bytebuddy.matcher.ElementMatchers.named;

import com.undergrowth.buddy.bean.MemoryDatabase;
import com.undergrowth.buddy.interceptor.LoggerInterceptor;
import java.io.File;
import java.io.IOException;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.MethodDelegation;
import org.junit.Test;

/**
 * 拦截器 类似于切面
 *
 * @author zhangwu
 * @version 1.0.0
 * @date 2019-01-11-16:07
 */
public class InterceptLogTest {

    @Test
    public void interTest() throws IllegalAccessException, InstantiationException {
        MemoryDatabase loggingDatabase = new ByteBuddy()
            .subclass(MemoryDatabase.class)
            .method(named("load")).intercept(MethodDelegation.to(LoggerInterceptor.class))
            .make()
            .load(getClass().getClassLoader())
            .getLoaded()
            .newInstance();
        loggingDatabase.load("a");
    }

    @Test
    public void inter2Test() throws IllegalAccessException, InstantiationException, IOException {
        new ByteBuddy()
            .subclass(MemoryDatabase.class)
            .method(named("load")).intercept(MethodDelegation.to(LoggerInterceptor.class))
            .make()
            .saveIn(new File("E:\\code\\github\\2016MyBookSummary\\src\\test\\java\\com\\undergrowth\\buddy"));
    }

}