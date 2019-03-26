package com.undergrowth.java.concurrent.art;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

/**
 * 线程不安全的Hashmap(因为采用链式地址存储,put/resize都有可能存在线程不安全),效率低下的Hashtable(synchronized),分段锁的ConcurrentHashMap
 *
 * @author zhangwu
 * @version 1.0.0
 * @date 2019-03-25-16:58
 */
public class HashContainerTest {

    /**
     * @param args
     * @throws InterruptedException
     */
    public static void main(String[] args) throws InterruptedException {
        int num = 100000;
        long sumTime = 0;
        int loopCount = 5;
        for (int l = 0; l < loopCount; l++) {
            // HashMap hash = new HashMap();
            //  Hashtable hash = new Hashtable();
             ConcurrentHashMap hash = new ConcurrentHashMap();
            CountDownLatch countDownLatch = new CountDownLatch(num);
            long start = System.currentTimeMillis();
            Thread t = new Thread(() -> {
                for (int i = 0; i < num; i++) {
                    new Thread(() -> {
                        hash.put(UUID.randomUUID().toString(), "");
                        countDownLatch.countDown();
                    }, "" + i).start();
                }
            });
            t.start();
            countDownLatch.await();
            long end = (System.currentTimeMillis() - start);
            sumTime += end;
            System.out.println(num + ":" + hash.size() + "\t" + end);
        }
        System.out.println("avg time " + sumTime + "/" + loopCount + ":" + sumTime / loopCount);

    }

}