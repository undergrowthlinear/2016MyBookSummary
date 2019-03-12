package com.undergrowth.buddy;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.NamingStrategy;
import net.bytebuddy.description.type.TypeDescription;
import net.bytebuddy.dynamic.DynamicType;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;
import net.bytebuddy.implementation.FixedValue;
import net.bytebuddy.matcher.ElementMatchers;
import org.junit.Test;

/**
 * @author zhangwu
 * @version 1.0.0
 * @date 2019-01-10-17:11
 */
public class ByteBuddyHelloWorldTest {

    /**
     * 1.创建已知类的子类 2.类型重定义----修改/新增/删除类的方法 3.类型重定基底----
     */

    @Test
    public void helloTest() throws IllegalAccessException, InstantiationException {
        Class<?> dynamicType = new ByteBuddy()
            .subclass(Object.class).name("test.test.Test")
            .method(ElementMatchers.named("toString"))
            .intercept(FixedValue.value("Hello World!"))
            //.intercept(MethodDelegation.to(ToStringInterceptor.class))
            .make()
            .load(getClass().getClassLoader())
            .getLoaded();
        System.out.println(dynamicType.getName());
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

    class Foo {
        String bar() {
            return "bar";
        }
    }

    @Test
    public void refineTest() {
        DynamicType.Builder builderSub = new ByteBuddy().subclass(Foo.class);
        DynamicType.Builder builderRedefine = new ByteBuddy().redefine(Foo.class);
        DynamicType.Builder builderRebase = new ByteBuddy().rebase(Foo.class);
        System.out.println(builderSub);
        System.out.println(builderRedefine);
        System.out.println(builderRebase);
    }

    @Test
    public void classLoadTest() {
        Class<?> type = new ByteBuddy()
            .subclass(Object.class)
            .make()
            .load(getClass().getClassLoader(), ClassLoadingStrategy.Default.WRAPPER)
            .getLoaded();
        System.out.println(type);
    }

}