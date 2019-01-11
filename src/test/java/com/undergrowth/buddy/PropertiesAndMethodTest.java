package com.undergrowth.buddy;

import static net.bytebuddy.matcher.ElementMatchers.isDeclaredBy;
import static net.bytebuddy.matcher.ElementMatchers.named;
import static net.bytebuddy.matcher.ElementMatchers.takesArguments;

import com.undergrowth.buddy.bean.Foo;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.FixedValue;
import org.junit.Test;

/**
 *
 * 属性和方法
 *
 * @author zhangwu
 * @version 1.0.0
 * @date 2019-01-11-11:59
 */
public class PropertiesAndMethodTest {

    @Test
    public void methodTest() throws IllegalAccessException, InstantiationException {
        String toString = new ByteBuddy()
            .subclass(Object.class)
            .name("example.Type")
            .make()
            .load(getClass().getClassLoader())
            .getLoaded()
            .newInstance() // Java reflection API
            .toString();
        System.out.println(toString);
    }

    @Test
    public void methodValueTest() throws IllegalAccessException, InstantiationException {
        String toString = new ByteBuddy()
            .subclass(Object.class)
            .name("example.Type")
            .method(named("toString")).intercept(FixedValue.value("Hello World!"))
            .make()
            .load(getClass().getClassLoader())
            .getLoaded()
            .newInstance()
            .toString();
        System.out.println(toString);
    }


    @Test
    public void methodValue2Test() throws IllegalAccessException, InstantiationException {
        Foo dynamicFoo = new ByteBuddy()
            .subclass(Foo.class)
            .method(isDeclaredBy(Foo.class)).intercept(FixedValue.value("One!"))
            .method(named("foo")).intercept(FixedValue.value("Two!"))
            .method(named("foo").and(takesArguments(1))).intercept(FixedValue.value("Three!"))
            .make()
            .load(getClass().getClassLoader())
            .getLoaded()
            .newInstance();
        System.out.println(dynamicFoo.bar());
        System.out.println(dynamicFoo.foo());
        System.out.println(dynamicFoo.foo('a'));

    }

}