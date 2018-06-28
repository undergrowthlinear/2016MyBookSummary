package com.undergrowth.oom.opti;

/**
 * MicroGenerator
 * The java.lang.OutOfMemoryError: PermGen space message indicates that the Permanent Generation’s area in memory is exhausted
 * -Xmx200M -XX:MaxPermSize=16M
 * @author zhangwu
 * @version 1.0.0
 * @date 2018-06-28-16:48
 */
import javassist.ClassPool;

public class MicroGenerator {
    public static void main(String[] args) throws Exception {
        for (int i = 0; i < 100_000_000; i++) {
            generate("eu.plumbr.demo.Generated" + i);
        }
    }

    public static Class generate(String name) throws Exception {
        ClassPool pool = ClassPool.getDefault();
        return pool.makeClass(name).toClass();
    }
}