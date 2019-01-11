package com.undergrowth.buddy;

import static net.bytebuddy.matcher.ElementMatchers.named;

import com.undergrowth.buddy.bean.First;
import com.undergrowth.buddy.bean.Second;
import java.io.File;
import java.io.IOException;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.ClassFileVersion;
import net.bytebuddy.implementation.DefaultMethodCall;
import org.junit.Test;

/**
 * @author zhangwu
 * @version 1.0.0
 * @date 2019-01-11-16:16
 */
public class DefaultMethodTest {

    @Test
    public void defaultTest() throws IOException {
        new ByteBuddy(ClassFileVersion.JAVA_V8)
            .subclass(Object.class)
            .implement(First.class)
            .implement(Second.class)
            .method(named("qux")).intercept(DefaultMethodCall.prioritize(First.class))
            .make().saveIn(new File("E:\\code\\github\\2016MyBookSummary\\src\\test\\java\\com\\undergrowth\\buddy"));
    }

}