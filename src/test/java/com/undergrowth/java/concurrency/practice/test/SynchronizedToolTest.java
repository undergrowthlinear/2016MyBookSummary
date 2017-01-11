package com.undergrowth.java.concurrency.practice.test;

import com.undergrowth.java.concurrency.practice.BoundHashSet;
import com.undergrowth.java.concurrency.practice.CollectionHelper;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 
* Description: TODO(同步工具类)
* synchronized----使用对象的内置锁(互斥锁)与内置条件队列进行同步/wait nofity nitofyAll为内部条件队列的api/可重入(获取锁的操作粒度是线程)
* CountDownLatch--为同步工具类之Latch(闭锁)--多个线程等待一个任务/一个线程等待多个任务/一次性状态
* FutureTask--表示一种抽象的可生成结果的运算
* Semaphore--控制同时访问特定资源的数目/不可重入
* CyclicBarrier--升级版闭锁/可重置状态/等待线程到达栅栏位置,通过栅栏后,可执行特定操作
* @author <a href="zhangwu@wxchina.coom">Wu.Zhang</a>
* Date 2016年6月18日
* @version  1.0.0
 */
public class SynchronizedToolTest {

	int threadNum = 100;
	private ExecutorService service = null;
	// 加上同步工具类闭锁 保证活跃线程数和竞争程度
	private CountDownLatch startGate = new CountDownLatch(1);
	private CountDownLatch endGate = new CountDownLatch(threadNum);
	//Semaphore
	final BoundHashSet<Integer> boundHashSet=new BoundHashSet<>(threadNum/2);

	@Before
	public void Before() {
		service = Executors.newFixedThreadPool(threadNum);
	}
	
	/**
	 * 
	* Description: TODO(主从循环调用 从先调用50次 主再调用50次 交替100次)
	* @author <a href="zhangwu@wxchina.coom">Wu.Zhang</a>
	* Date 2016年6月18日
	* @version  1.0.0
	 */
	class MasterSlaveLoop{
		
		private boolean isMain=false;
		
		public synchronized void mainLoop(int loopTime){
			if(!isMain)
				try {
					this.wait(); //将当前线程阻塞,添加到条件队列,等待唤醒
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			for(int i=1;i<=50;i++){
				System.out.println("mainLoop,loopTime:"+loopTime+",isMain:"+isMain+",count:"+i);
			}
			isMain=false;
			this.notifyAll(); //唤醒内置条件队列中的线程
			
		}
		
		public synchronized void slaveLoop(int loopTime){
			if(isMain)
				try {
					this.wait(); //将当前线程阻塞,添加到条件队列,等待唤醒
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			for(int i=1;i<=50;i++){
				System.out.println("slaveLoop,loopTime:"+loopTime+",isMain:"+isMain+",count:"+i);
			}
			isMain=true;
			this.notifyAll(); //唤醒内置条件队列中的线程
		}
	}
	
	@Test
	public void masterSlaveTest(){
		final MasterSlaveLoop masterSlaveLoop=new MasterSlaveLoop();
		final int loopNum=100;
		Thread thread=new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				for(int i=1;i<=loopNum;i++){
					masterSlaveLoop.slaveLoop(i);
				}
			}
		});
		thread.start();
		for(int i=1;i<=loopNum;i++){
			masterSlaveLoop.mainLoop(i);
		} 
	}
	
	@Test
	public void countDownLatchTest(){
		Callable<Void> callable = new Callable<Void>() {

			@Override
			public Void call() throws Exception {
				// TODO Auto-generated method stub

				// TODO Auto-generated method stub
				try {
					
					startGate.await();// 等待所有线程准备好 一起执行
					String str = Thread.currentThread().getId() + "," + Thread.currentThread().getName();
					System.out.println(str);
					endGate.countDown();
					return null;
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return null;

			}
		};
		List<Future<Void>> results = CollectionHelper.addCallableCollection(service, threadNum, callable);
		startGate.countDown();
		try {
			endGate.await();//等待所有线程执行完
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(
				Thread.currentThread().getId() + "," + Thread.currentThread().getName() + "," + results.size());
		;
		//CollectionHelper.iteratorResult(results);
	}
	
	@Test
	public void futureTaskTest(){
		Callable<String> callable = new Callable<String>() {

			@Override
			public String call() throws Exception {
				// TODO Auto-generated method stub

				String str = Thread.currentThread().getId() + "," + Thread.currentThread().getName();
				return str;

			}
		};
		FutureTask<String> futureTask=new FutureTask<String>(callable);
		try {
			System.out.println(System.currentTimeMillis());
			service.submit(futureTask);
			//等待获取结果
			System.out.println(futureTask.get());
			System.out.println(System.currentTimeMillis());
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	public void semaphoreAddTest() throws InterruptedException, ExecutionException{
		final AtomicInteger count=new AtomicInteger(0);
		Callable<Boolean> callable = new Callable<Boolean>() {

			@Override
			public Boolean call() throws Exception {
				// TODO Auto-generated method stub

				return boundHashSet.addElement(count.incrementAndGet());

			}
		};
		System.out.println(System.currentTimeMillis()/1000+"s");
		List<Future<Boolean>> results = CollectionHelper.addCallableCollection(service, threadNum, callable);
		CollectionHelper.iteratorResult(results);
		System.out.println("semaphoreAddTest:"+System.currentTimeMillis()/1000+"s");
	}
	
	@Test
	public void semaphoreDelTest() throws InterruptedException, ExecutionException{
		//删除元素
		final AtomicInteger countDecr=new AtomicInteger(0);
		Callable<Boolean> callableDecr = new Callable<Boolean>() {

			@Override
			public Boolean call() throws Exception {
				// TODO Auto-generated method stub

				return boundHashSet.removeElement(countDecr.incrementAndGet());

			}
		};
		List<Future<Boolean>> resultsDecr = CollectionHelper.addCallableCollection(service, threadNum/2, callableDecr);
		CollectionHelper.iteratorResult(resultsDecr);
		System.out.println("semaphoreDelTest:"+System.currentTimeMillis()/1000+"s");
	}
	
	@Test
	public void semaphoreAddDelTest() {
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					semaphoreAddTest();
				} catch (InterruptedException | ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					semaphoreDelTest();
				} catch (InterruptedException | ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
		try {
			Thread.sleep(1000*60);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void cyclicBarrierTest() throws InterruptedException, ExecutionException {
		//当最后一个线程达到栅栏时,执行此操作
		final CyclicBarrier barrier = new CyclicBarrier(threadNum, new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				System.out.println("所有线程都已执行完,"+ System.currentTimeMillis());
			}
		});
		Callable<String> callable = new Callable<String>() {

			@Override
			public String call() throws Exception {
				// TODO Auto-generated method stub
				// 计算什么
				int num = 0;
				while (num < 1000000)
					num++;
				barrier.await();
				return "执行完运算," + Thread.currentThread().getId() + "," + Thread.currentThread().getName() + ","
						+ System.currentTimeMillis();
			}
		};
		List<Future<String>> results = CollectionHelper.addCallableCollection(service, threadNum, callable);
		CollectionHelper.iteratorResult(results);
		//重置栅栏 再玩一次
		barrier.reset();
		results = CollectionHelper.addCallableCollection(service, threadNum, callable);
		CollectionHelper.iteratorResult(results);
		
	}
	
	
}
