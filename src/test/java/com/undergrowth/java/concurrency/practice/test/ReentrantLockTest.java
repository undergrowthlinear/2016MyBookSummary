package com.undergrowth.java.concurrency.practice.test;

import com.undergrowth.java.concurrency.practice.CollectionHelper;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.junit.Before;
import org.junit.Test;

/**
 * Description: TODO(这里用一句话描述这个类的作用)
 *
 * @author <a href="zhangwu@wxchina.coom">Wu.Zhang</a> Date 2016年6月19日
 * @version 1.0.0
 */
public class ReentrantLockTest {

    int threadNum = 100;
    private ExecutorService service = null;

    @Before
    public void Before() {
        service = Executors.newFixedThreadPool(threadNum);
    }

    class X {

        private final ReentrantLock lock = new ReentrantLock();
        // ...

        public void m() {
            lock.lock(); // block until condition holds
            try {
                Thread.currentThread();
                // ... method body
                Thread.sleep(1);
                System.out.println(
                    System.currentTimeMillis() + ",doSomeThing...," + Thread.currentThread().getId() + ","
                        + Thread.currentThread().getName());
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }
    }

    @Test
    public void xTest() throws InterruptedException, ExecutionException {
        //预期----所有线程的输出时间都是相差1ms 表示lock确实是互斥锁
        final X x = new X();
        Callable<Void> callable = new Callable<Void>() {

            @Override
            public Void call() throws Exception {
                x.m();
                return null;

            }
        };
        List<Future<Void>> results = CollectionHelper
            .addCallableCollection(service, threadNum, callable);
        CollectionHelper.iteratorResult(results);
    }

    class Y {

        final Lock lock = new ReentrantLock();

        public void m() {
            if (lock.tryLock()) {
                try {
                    // manipulate protected state
                    System.out.println(
                        "获取到锁," + Thread.currentThread().getId() + "," + Thread.currentThread().getName());
                    //Thread.sleep(1);
                } /*catch (InterruptedException e) {
          // TODO Auto-generated catch block
					e.printStackTrace();
				} */ finally {
                    lock.unlock();
                }
            } else {
                // perform alternative actions
                System.out.println(
                    "没有获取到锁," + Thread.currentThread().getId() + "," + Thread.currentThread().getName());
            }
        }
    }

    /**
     * 预期----当threadNum个线程执行时 有的线程获取到锁 有的线程无法获取到锁
     */
    @Test
    public void tryLockTest() {
        final Y y = new Y();
        Callable<Void> callable = new Callable<Void>() {

            @Override
            public Void call() throws Exception {
                y.m();
                return null;

            }
        };
        List<Future<Void>> results = CollectionHelper
            .addCallableCollection(service, threadNum, callable);
        CollectionHelper.iteratorResult(results);
    }


    class Z {

        final Lock lock = new ReentrantLock();

        /**
         * @param timeOut ms
         */
        public void m(int timeOut) throws InterruptedException {
            if (lock.tryLock(timeOut, TimeUnit.MILLISECONDS)) {
                try {
                    // manipulate protected state
                    System.out
                        .println("获取到锁," + Thread.currentThread().getId() + "," + Thread.currentThread()
                            .getName());
                } finally {
                    lock.unlock();
                }
            } else {
                // perform alternative actions
                System.out.println(
                    timeOut + "ms后没有获取到锁," + Thread.currentThread().getId() + "," + Thread.currentThread()
                        .getName());
            }

        }
    }

    /**
     * 预期----当threadNum个线程执行时 有的线程获取到锁 有的线程无法获取到锁
     */
    @Test
    public void tryLockTimeOutTest() {
        final Z z = new Z();
        final Random random = new Random();
        Callable<Void> callable = new Callable<Void>() {

            @Override
            public Void call() throws Exception {
                z.m(random.nextInt(100));
                return null;

            }
        };
        List<Future<Void>> results = CollectionHelper
            .addCallableCollection(service, threadNum, callable);
        CollectionHelper.iteratorResult(results);
    }

    class LockInterrupt {

        final Lock lock = new ReentrantLock();

        /**
         * @param timeOut ms
         */
        public void m() throws InterruptedException {
            lock.lockInterruptibly();
            try {
                // manipulate protected state
                System.out.println(
                    "获取到锁," + Thread.currentThread().getId() + "," + Thread.currentThread().getName());
                Thread.currentThread().interrupt();
            } finally {
                lock.unlock();
            }
        }

    }

    /**
     * 预期----当threadNum个线程执行时 有的线程获取到锁 有的线程无法获取到锁
     */
    @Test
    public void lockInterruptTest() {
        final LockInterrupt lockInterrupt = new LockInterrupt();
        Callable<Void> callable = new Callable<Void>() {

            @Override
            public Void call() throws Exception {
                lockInterrupt.m();
                return null;

            }
        };
        List<Future<Void>> results = CollectionHelper
            .addCallableCollection(service, threadNum, callable);
        CollectionHelper.iteratorResult(results);

    }
}
