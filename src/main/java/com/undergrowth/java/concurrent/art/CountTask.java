package com.undergrowth.java.concurrent.art;

import java.util.concurrent.RecursiveTask;

/**
 * 负责任务拆分 逻辑计算
 *
 * @author zhangwu
 * @version 1.0.0
 * @date 2019-03-26-10:53
 */
public class CountTask extends RecursiveTask<Long> {
    private Long param = 10000000L;
    private Long start;
    private Long end;

    public CountTask(Long start, Long end) {
        this.start = start;
        this.end = end;
    }

    @Override
    protected Long compute() {
        Long sum = 0L;
        boolean canCompute = (end - start) <= param;
        if (canCompute) {
            for (Long i = start; i <= end; i++) {
                sum += i;
            }
        } else {
            Long middle = (start + end) / 2;
            CountTask leftTask = new CountTask(start, middle);
            CountTask rightTask = new CountTask(middle + 1, end);
            leftTask.fork();
            rightTask.fork();
            Long leftResult = leftTask.join();
            Long rightResult = rightTask.join();
            sum += leftResult + rightResult;
        }
        return sum;
    }
}