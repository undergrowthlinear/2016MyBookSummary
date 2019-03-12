package com.undergrowth.oom.opti;

/**
 * Metaspace java.lang.OutOfMemoryError: Metaspace is: either too many classes or too big classes being loaded to the Metaspace.
 * -XX:MaxMetaspaceSize=64m
 *
 * @author zhangwu
 * @version 1.0.0
 * @date 2018-06-28-17:14
 */
public class Metaspace {
    static javassist.ClassPool cp = javassist.ClassPool.getDefault();

    public static void main(String[] args) throws Exception {
        for (int i = 0; ; i++) {
            Class c = cp.makeClass("eu.plumbr.demo.Generated" + i).toClass();
        }
    }
}