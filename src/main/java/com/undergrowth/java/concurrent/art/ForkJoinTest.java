package com.undergrowth.java.concurrent.art;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;

/**
 * fork_join测试
 *
 * @author zhangwu
 * @version 1.0.0
 * @date 2019-03-26-10:51
 */
public class ForkJoinTest {

    public static void main(String[] args) {
        Long maxNum = 10000000L;
        long start = 0;
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        CountTask countTask = new CountTask(1L, maxNum);
        start = System.currentTimeMillis();
        Future<Long> result = forkJoinPool.submit(countTask);
        try {
            System.out.println("1到" + maxNum + ":" + result.get() + "\t" + (System.currentTimeMillis() - start));
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        long sum = 0;
        start = System.currentTimeMillis();
        for (long i = 1; i <= maxNum; i++) {
            sum += i;
        }
        System.out.println("1到" + maxNum + ":" + sum + "\t" + (System.currentTimeMillis() - start));
    }

}