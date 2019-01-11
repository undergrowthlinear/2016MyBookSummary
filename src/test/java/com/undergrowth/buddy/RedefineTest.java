package com.undergrowth.buddy;

import java.lang.reflect.Method;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.agent.ByteBuddyAgent;
import net.bytebuddy.dynamic.loading.ClassReloadingStrategy;
import org.junit.Test;

/**
 * @author zhangwu
 * @version 1.0.0
 * @date 2019-01-11-11:30
 */
public class RedefineTest {

    class Foo {
        String m() {
            return "foo";
        }
    }

    class Bar {
        String m() {
            return "bar";
        }
    }

    /**
     * com.undergrowth.buddy.RedefineTest$Foo	foo
     bar
     m	java.lang.String com.undergrowth.buddy.RedefineTest$Foo.m()
     */
    @Test
    public void redefineTest() {
        ByteBuddyAgent.install();
        Foo foo = new Foo();
        System.out.println(Foo.class.getName() + "\t" + foo.m());
        new ByteBuddy()
            .redefine(Bar.class)
            .name(Foo.class.getName())
            .make()
            .load(Foo.class.getClassLoader(), ClassReloadingStrategy.fromInstalledAgent());
        System.out.println(foo.m());
        for (Method method :
            foo.getClass().getDeclaredMethods()) {
            System.out.println(method.getName() + "\t" + method);
        }
    }

    /**
     * com.undergrowth.buddy.RedefineTest$Foo	foo
     bar
     m	java.lang.String com.undergrowth.buddy.RedefineTest$Foo.m()
     */
    @Test
    public void rebaseTest() {
        ByteBuddyAgent.install();
        Foo foo = new Foo();
        System.out.println(Foo.class.getName() + "\t" + foo.m());
        new ByteBuddy()
            .rebase(Bar.class)
            .name(Foo.class.getName())
            .make()
            .load(Foo.class.getClassLoader(), ClassReloadingStrategy.fromInstalledAgent());
        System.out.println(foo.m());
        for (Method method :
            foo.getClass().getDeclaredMethods()) {
            System.out.println(method.getName() + "\t" + method);
        }
    }

}