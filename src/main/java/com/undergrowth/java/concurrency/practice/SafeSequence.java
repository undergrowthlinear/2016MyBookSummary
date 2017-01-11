package com.undergrowth.java.concurrency.practice;

@ThreadSafeTest
public class SafeSequence {
	private int value = 0;

	//使用synchronized 使用对象锁进行同步
	public synchronized int getNext() {
		return value++;
	}

}