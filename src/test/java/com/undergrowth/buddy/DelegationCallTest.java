package com.undergrowth.buddy;

import static net.bytebuddy.matcher.ElementMatchers.named;

import com.undergrowth.buddy.bean.Source;
import com.undergrowth.buddy.bean.Target;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.MethodDelegation;
import org.junit.Test;

/**
 * 委托方法调用
 *
 * @author zhangwu
 * @version 1.0.0
 * @date 2019-01-11-14:34
 */
public class DelegationCallTest {

    @Test
    public void delegationTest() throws IllegalAccessException, InstantiationException {
        String helloWorld = new ByteBuddy()
            .subclass(Source.class)
            .method(named("hello")).intercept(MethodDelegation.to(Target.class))
            .make()
            .load(getClass().getClassLoader())
            .getLoaded()
            .newInstance()
            .hello("World");
        System.out.println(helloWorld);
    }


}