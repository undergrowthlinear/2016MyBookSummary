package com.undergrowth.buddy;

import static net.bytebuddy.matcher.ElementMatchers.named;

import com.undergrowth.buddy.bean.RuntimeDefinitionImpl;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.SuperMethodCall;
import org.junit.Test;

/**
 * 注解测试
 *
 * @author zhangwu
 * @version 1.0.0
 * @date 2019-01-11-14:42
 */
public class AnnotationTest {

    @Test
    public void anTest() {
        Class cls = new ByteBuddy()
            .subclass(Object.class)
            .annotateType(new RuntimeDefinitionImpl())
            .method(named("toString"))
            .intercept(SuperMethodCall.INSTANCE)
            .annotateMethod(new RuntimeDefinitionImpl())
            .defineField("foo", Object.class)
            .annotateField(new RuntimeDefinitionImpl()).make().load(getClass().getClassLoader()).getLoaded();
        for (Method method :
            cls.getDeclaredMethods()) {
            System.out.println(method);
        }
        for (Field field :
            cls.getDeclaredFields()) {
            System.out.println(field);
        }
    }

    @Test
    public void an2Test() throws IOException {
        new ByteBuddy()
            .subclass(Object.class)
            .annotateType(new RuntimeDefinitionImpl())
            .method(named("toString"))
            .intercept(SuperMethodCall.INSTANCE)
            .annotateMethod(new RuntimeDefinitionImpl())
            .defineField("foo", Object.class)
            .annotateField(new RuntimeDefinitionImpl()).make().saveIn(new File("E:\\code\\github\\2016MyBookSummary\\src\\test\\java\\com\\undergrowth\\buddy"));
    }

}