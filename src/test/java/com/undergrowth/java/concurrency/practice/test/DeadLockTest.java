package com.undergrowth.java.concurrency.practice.test;

import com.undergrowth.java.concurrency.practice.CollectionHelper;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.*;

/**
 * 
* Description: TODO(这里用一句话描述这个类的作用)
* 死锁----每个人都拥有其他人需要的资源,而又等待其他人已经拥有的资源,每个人在获取需要的
* 资源之前都不会释放自己已经获得的资源
* 死锁有向图----线程为节点,等待的资源为边,当出现回环时,出现死锁
* Lock-Ordering Deadlock(锁顺序死锁/动态的锁顺序死锁)----不同顺序获取相同的锁
* 开放调用----调用方法无需持有锁
* 线程饥饿死锁(Thread Starvation Deadlock)----例如在单个线程池中,执行某个任务,执行任务时提交任务到线程池的队列中,等待提交的任务执行完成,
* 此时会出现死锁,因为提交的任务在队列中,等待当前任务执行完才能进行调用,而当前任务又等待提交任务的结果,这样形成了死循环,造成线程饥饿死锁
* 饥饿----由于线程的优先级导致有些线程无法调用到
* 活锁----由于执行毒药消息导致线程一直重复尝试错误的消息,将不可修复的错误当作可修复的错误
* 线程转储----Thread Dump----查看锁/线程/死锁等信息
* @author <a href="zhangwu@wxchina.coom">Wu.Zhang</a>
* Date 2016年6月19日
* @version  1.0.0
 */
public class DeadLockTest {

	int threadNum = 10000;
	private ExecutorService service = null;
	private CyclicBarrier barrier=new CyclicBarrier(threadNum);
	
	@Before
	public void Before() {
		service = Executors.newFixedThreadPool(threadNum);
	}
	
	class LeftRightDeadlock {
		private final Object left = new Object();
		private final Object right = new Object();

		public String leftRight() {
			synchronized (left) {
				synchronized (right) {
					return "left to right";
				}
			}
		}

		public String rightLeft() {
			synchronized (right) {
				synchronized (left) {
					return "right to left";
				}
			}
		}

	}
	
	@Test
	public void orderDeadlockTest(){
		final LeftRightDeadlock leftRightDeadlock=new LeftRightDeadlock();
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				Callable<String> leftRightCallable = new Callable<String>() {

					@Override
					public String call() throws Exception {
						// TODO Auto-generated method stub
						barrier.await();
						return leftRightDeadlock.leftRight();
					}
				};
				List<Future<String>> results = CollectionHelper.addCallableCollection(service, threadNum/2,
						leftRightCallable);
				CollectionHelper.iteratorResult(results);
			}
		}).start();
		;
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				Callable<String> rightLeftCallable = new Callable<String>() {

					@Override
					public String call() throws Exception {
						// TODO Auto-generated method stub
						barrier.await();
						return leftRightDeadlock.rightLeft();
					}
				};
				List<Future<String>> results = CollectionHelper.addCallableCollection(service, threadNum/2,
						rightLeftCallable);
				CollectionHelper.iteratorResult(results);
			}
		}).start();
		
		while (true)
			;
		
	}
	
	
	class ThreadStarvationDeadlock{
		private ExecutorService service=Executors.newSingleThreadExecutor();
		
		class RenderTask implements Callable<String>{

			@Override
			public String call() throws Exception {
				// TODO Auto-generated method stub
				Future<String> second=service.submit(new Callable<String>() {

					@Override
					public String call() throws Exception {
						// TODO Auto-generated method stub
						return "second";
					}
				});
				return RenderTask.class.getName()+",secondReturn:"+second.get();
			}
			
		}
	}
	
	@Test
	public void threadStarTest() throws InterruptedException, ExecutionException{
		final ThreadStarvationDeadlock threadStarvationDeadlock=new ThreadStarvationDeadlock();
		Callable<String> renderTask=threadStarvationDeadlock.new RenderTask();
		Future<String> future=threadStarvationDeadlock.service.submit(renderTask);
		System.out.println(future.get());
	}
	
}
