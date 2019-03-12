/*   
* Copyright (c) 2016 by WuZhang 
*             All rights reserved                         
*/
package com.undergrowth.java.concurrency.practice.test;

import com.undergrowth.java.concurrency.practice.CollectionHelper;
import com.undergrowth.java.concurrency.practice.SafeSequence;
import com.undergrowth.java.concurrency.practice.UnsafeSequence;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * @author <a href="zhangwu@wxchina.coom">Wu.Zhang</a>
 * @version 1.0.0
 * @Description: TODO(用于Java Concurrency in Practice的简介----> 每个线程拥有自己的虚拟机栈、局部变量、程序计数器, 共享进程中堆上的共享变量, 共享方法区(永久内存区/常量区)的class与meta信息
 * Java的内存模型支持一次编写,随处运行 3-4-3 3----并发的来源----提升资源的利用率、提升模块的公平性、提高任务交互的便利性 4----线程的优势----利用多核处理器(摩尔定律----当成本不变的前提下,每个18-24个月,集成电路上的芯片数目翻一倍,性能提升一倍)、建模的简化、异步执行的简化、灵敏度的提高
 * 3----线程的问题---- 安全性(永远不让糟糕的事情发生----多线程访问共享数据,执行的顺序问题)、 活跃性(正确的事情最终要发生----死锁、饥饿)、 性能(尽快的执行----线程上下文的切换、cpu资源的消耗) ) Q1:为什么匿名内部类或者局部内部类只能访问final局部变量?
 * A1:通常来说局部内部类访问局部变量是没有问题的,但是关键在于局部内部类与局部变量的生命周期不一致,当进行某个方法调用时,产生局部变量a,局部内部类b， 当方法执行完,局部变量a销毁,但是局部内部类b可能还存在,此时如果在局部内部类b中访问不存在的局部变量a时,就会出现问题,当使用final修饰局部变量时,局部内部类会
 * 拷贝一份局部变量,因为局部变量为final,所以不用担心局部变量销毁或者改变
 *
 * synchronized----使用对象的内置锁(互斥锁)与内置条件队列进行同步/wait nofity nitofyAll为内部条件队列的api/可重入(获取锁的操作粒度是线程) CountDownLatch--为同步工具类之Latch(闭锁)--多个线程等待一个任务/一个线程等待多个任务
 * FutureTask
 *
 * ThreadDump分析: http://blog.csdn.net/rachel_luo/article/details/8920596 线程的状态 new(创建)--start-->Runnable(就绪)--调度器调度-->Running(运行)--run-->Dead(死亡)
 * Running(运行)--synchronized--Block(锁池/entry set)--获得锁-->Runnable(就绪) Running(运行)--wait--Block(等待池/wait set)--notify/notifyAll-->Block(锁池/entry
 * set)--获得锁-->Runnable(就绪) Running(运行)--sleep/join--Block(其他等待)--条件达到-->Runnable(就绪)
 * @Date 2016年3月15日
 */
public class ConcurrencyProfile {

    int threadNum = 10000;
    private OutputStream oStream = null;
    private BufferedWriter writer = null;
    private ExecutorService service = null;
    // 加上同步工具类闭锁 保证活跃线程数和竞争程度
    private CountDownLatch startGate = new CountDownLatch(1);
    private CountDownLatch endGate = new CountDownLatch(threadNum);

    @Before
    public void Before() {
        try {
            service = Executors.newFixedThreadPool(threadNum);
            oStream = new FileOutputStream("output.txt");
            writer = new BufferedWriter(new OutputStreamWriter(oStream));
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @After
    public void after() throws InterruptedException {
        endGate.await();// 等待所有异步执行的任务执行完成
        if (service != null) {
            service.shutdown();
            while (!service.awaitTermination(60, TimeUnit.SECONDS)) {
                ;
            }
            if (service.isTerminated()) {
                if (writer != null) {
                    try {
                        writer.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
                if (oStream != null) {
                    try {
                        oStream.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }

    }

    /**
     * @throws ExecutionException
     * @throws InterruptedException
     *
     */
    @Test
    public void unSafaTest() throws InterruptedException, ExecutionException {
        final UnsafeSequence unsafeSequence = new UnsafeSequence();
        final AtomicInteger count = new AtomicInteger(0);
        Callable<Void> callable = new Callable<Void>() {

            @Override
            public Void call() throws Exception {
                // TODO Auto-generated method stub

                // TODO Auto-generated method stub
                try {
          /*
                     * System.out.println(Thread.currentThread().getId() + "," +
					 * Thread.currentThread().getName() + "," +
					 * count.incrementAndGet());
					 */
                    startGate.await();// 等待所有线程准备好 一起执行
                    String str =
                        "ThreadId:" + Thread.currentThread().getId() + "," + Thread.currentThread().getName()
                            + ","
                            + "\nValueIs:" + unsafeSequence.getNext();
                    writer.write(str + System.getProperty("line.separator"));
                    writer.flush();
                    endGate.countDown();
                    return null;
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                return null;

            }
        };
        List<Future<Void>> results = CollectionHelper
            .addCallableCollection(service, threadNum, callable);
        startGate.countDown();
        System.out.println(
            Thread.currentThread().getId() + "," + Thread.currentThread().getName() + "," + results
                .size());
        ;
        CollectionHelper.iteratorResult(results);

    }

    @Test
    public void safaTest() throws InterruptedException, ExecutionException {
        final SafeSequence safeSequence = new SafeSequence();
        final AtomicInteger count = new AtomicInteger(0);
        Callable<Void> callable = new Callable<Void>() {

            @Override
            public Void call() throws Exception {
                // TODO Auto-generated method stub

                // TODO Auto-generated method stub
                try {
					/*
					 * System.out.println(Thread.currentThread().getId() + "," +
					 * Thread.currentThread().getName() + "," +
					 * count.incrementAndGet());
					 */
                    startGate.await();// 等待所有线程准备好 一起执行
                    String str =
                        "ThreadId:" + Thread.currentThread().getId() + "," + Thread.currentThread().getName()
                            + ","
                            + "\nValueIs:" + safeSequence.getNext();
                    writer.write(str + System.getProperty("line.separator"));
                    writer.flush();
                    endGate.countDown();
                    return null;
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                return null;

            }
        };
        List<Future<Void>> results = CollectionHelper
            .addCallableCollection(service, threadNum, callable);
        startGate.countDown();
        System.out.println(
            Thread.currentThread().getId() + "," + Thread.currentThread().getName() + "," + results
                .size());
        ;
        CollectionHelper.iteratorResult(results);
    }
}
