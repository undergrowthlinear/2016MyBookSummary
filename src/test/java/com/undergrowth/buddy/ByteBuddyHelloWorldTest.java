package com.undergrowth.buddy;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.NamingStrategy;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.implementation.FixedValue;
import net.bytebuddy.matcher.ElementMatchers;
import org.junit.Test;

/**
 * @author zhangwu
 * @version 1.0.0
 * @date 2019-01-10-17:11
 */
public class ByteBuddyHelloWorldTest {


    @Test
    public void helloTest() throws IllegalAccessException, InstantiationException {
        Class<?> dynamicType = new ByteBuddy()
            .subclass(Object.class)
            .method(ElementMatchers.named("toString"))
            .intercept(FixedValue.value("Hello World!"))
            //.intercept(MethodDelegation.to(ToStringInterceptor.class))
            .make()
            .load(getClass().getClassLoader())
            .getLoaded();

        System.out.println(dynamicType.newInstance().toString());
    }

    @Test
    public void hello2Test() {
        DynamicType.Unloaded<?> dynamicType = new ByteBuddy()
            .with(new NamingStrategy.AbstractBase() {
                @Override
                protected String name(TypeDescription superClass) {
                    return "i.love.ByteBuddy." + superClass.getSimpleName();
                }
            })
            .subclass(Object.class)
            .make();
        System.out.println(dynamicType.getClass().getName());
    }

}