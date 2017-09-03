package com.undergrowth.java.concurrency.practice;

/**
 * Description: TODO(多线程情况下交替访问会导致竞态条件(Race Condition))
 *
 * @author <a href="zhangwu@wxchina.coom">Wu.Zhang</a> Date 2016年6月18日
 * @version 1.0.0
 */
@NotThreadSafeTest
public class UnsafeSequence {

  private int value = 0;

  public int getNext() {
    return value++;
  }

}
