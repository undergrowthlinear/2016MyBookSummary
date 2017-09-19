package com.undergrowth.java.concurrency.practice.test;

import java.util.Arrays;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.junit.Test;

/**
 * Description: TODO(这里用一句话描述这个类的作用) Q1:内置锁与显示锁的区别 A1:synchronized----内置锁----当资源被占用时,调用者进入休眠状态
 * Lock----显示锁----支持无条件/可中断/可定时/可轮询 Q2:乐观锁与悲观锁的区别 Q2:悲观锁在每次访问数据时,都进行加锁操作,适用于多写的场景
 * 乐观锁----每次访问数据不进行加锁,当更新操作时,会去回补哪些操作过数据,适用于多读的场景 Q3:自旋锁 A3:程序通过循环等待而不是休眠
 * see----ReentrantLockTest/ReentrantReadWriteLockTest 内置锁与显示锁相比----
 * 显示锁提供了一些扩展的功能,例如可定时/可中断/可轮询,性能方面差别不大,与内置锁具有相同的互斥性/内存可见性/可重入 读写锁允许多个读线程并发的访问被保护的数据,当以读操作为主的数据结构时,可提升性能
 *
 * @author <a href="zhangwu@wxchina.coom">Wu.Zhang</a> Date 2016年6月18日
 * @version 1.0.0
 */
public class LockTest {

  private BlockingQueue blockingQueue = new ArrayBlockingQueue(10);

  class BoundedBuffer {

    final Lock lock = new ReentrantLock();
    final Condition notFull = lock.newCondition();
    final Condition notEmpty = lock.newCondition();

    final Object[] items = new Object[100];
    int putptr, takeptr, count;

    public void put(Object x) throws InterruptedException {
      lock.lock();
      try {
        while (count == items.length) {
          notFull.await();
        }
        items[putptr] = x;
        if (++putptr == items.length) {
          putptr = 0;
        }
        ++count;
        notEmpty.signal();
      } finally {
        lock.unlock();
      }
    }

    public Object take() throws InterruptedException {
      lock.lock();
      try {
        while (count == 0) {
          notEmpty.await();
        }
        Object x = items[takeptr];
        items[takeptr] = null;
        if (++takeptr == items.length) {
          takeptr = 0;
        }
        --count;
        notFull.signal();
        return x;
      } finally {
        lock.unlock();
      }
    }
  }

  @Test
  public void boundBufferTest() throws InterruptedException {
    final BoundedBuffer buffer = new BoundedBuffer();
    System.out.println(
        "putptr:" + buffer.putptr + ",takeptr:" + buffer.takeptr + ",count:" + buffer.count
            + ",data:" + Arrays.toString(buffer.items));
    for (int i = 0; i < 50; i++) {
      buffer.put(i);
    }
    System.out.println(
        "putptr:" + buffer.putptr + ",takeptr:" + buffer.takeptr + ",count:" + buffer.count
            + ",data:" + Arrays.toString(buffer.items));
    for (int i = 0; i < 40; i++) {
      buffer.take();
    }
    System.out.println(
        "putptr:" + buffer.putptr + ",takeptr:" + buffer.takeptr + ",count:" + buffer.count
            + ",data:" + Arrays.toString(buffer.items));
    for (int i = 0; i < 70; i++) {
      buffer.put(i);
    }
    System.out.println(
        "putptr:" + buffer.putptr + ",takeptr:" + buffer.takeptr + ",count:" + buffer.count
            + ",data:" + Arrays.toString(buffer.items));
    for (int i = 0; i < 40; i++) {
      System.out.println(buffer.take());
      ;
    }
    System.out.println(
        "putptr:" + buffer.putptr + ",takeptr:" + buffer.takeptr + ",count:" + buffer.count
            + ",data:" + Arrays.toString(buffer.items));
  }


}
