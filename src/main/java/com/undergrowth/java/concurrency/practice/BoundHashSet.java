package com.undergrowth.java.concurrency.practice;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Semaphore;

/**
 * 
* Description: TODO(通过信号量控制Set的元素个数)
* @author <a href="zhangwu@wxchina.coom">Wu.Zhang</a>
* Date 2016年6月18日
* @version  1.0.0
 * @param <T>
 */
public class BoundHashSet<T> {

	private  Set<T> boundSet;
	private Semaphore semaphore;
	public BoundHashSet(int bound){
		boundSet=Collections.synchronizedSet(new HashSet<T>(bound));
		semaphore=new Semaphore(bound);
	}
	
	public boolean addElement(T ele) throws InterruptedException {
		semaphore.acquire();//获取信号量
		boolean isAdded = false;
		try {
			isAdded = boundSet.add(ele);
			System.out.println("ele:"+ele+",isAdded:"+isAdded+",availablePermits:"+semaphore.availablePermits());
			return isAdded;
		} finally {
			// TODO: handle finally clause
			if (!isAdded) //添加失败 释放信号量
				semaphore.release();
		}
		
	}
	
	public boolean removeElement(T ele) {
		boolean isRemove = false;
		isRemove = boundSet.remove(ele);
		System.out.println("ele:"+ele+",isRemove:"+isRemove+",availablePermits:"+semaphore.availablePermits());
		if (isRemove) //成功删除元素,释放信号量
			semaphore.release();
		System.out.println("ele:"+ele+",isRemove:"+isRemove+",availablePermits:"+semaphore.availablePermits());
		return isRemove;
	}
	
}
