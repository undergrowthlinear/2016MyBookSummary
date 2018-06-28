package com.undergrowth.oom.opti;

/**
 * TestWrapper
 * GC overhead limit exceeded
 * -Xmx12m -XX:+UseParallelGC/-Xmx12m -XX:+UseG1GC
 * @author zhangwu
 * @version 1.0.0
 * @date 2018-06-28-16:32
 */
import java.util.Map;
import java.util.Random;
public class TestWrapper {
    public static void main(String args[]) throws Exception {
        Map map = System.getProperties();
        Random r = new Random();
        while (true) {
            map.put(r.nextInt(), "value");
        }
    }
}