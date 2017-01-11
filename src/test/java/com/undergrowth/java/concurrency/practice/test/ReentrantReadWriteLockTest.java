package com.undergrowth.java.concurrency.practice.test;

import com.undergrowth.java.concurrency.practice.CollectionHelper;
import org.junit.Before;
import org.junit.Test;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 
* Description: TODO(这里用一句话描述这个类的作用)
* @author <a href="zhangwu@wxchina.coom">Wu.Zhang</a>
* Date 2016年6月19日
* @version  1.0.0
 */
public class ReentrantReadWriteLockTest {
	
	int threadNum = 100;
	private ExecutorService service = null;
	
	@Before
	public void Before() {
		service = Executors.newFixedThreadPool(threadNum);
	}
	
	
	class CachedData {
		Object data;
		volatile boolean cacheValid=false;
		final ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();

		void processCachedData() {
		   rwl.readLock().lock();
		   if (!cacheValid) {
		     // Must release read lock before acquiring write lock
		     rwl.readLock().unlock();
		     rwl.writeLock().lock();
		     try {
		       // Recheck state because another thread might have
		       // acquired write lock and changed state before we did.
		       if (!cacheValid) {
		         data = Thread.currentThread().getId() + "," + Thread.currentThread().getName();
		         cacheValid = true;
		       }
		       // Downgrade by acquiring read lock before releasing write lock
		       rwl.readLock().lock();
		     } finally {
		       rwl.writeLock().unlock(); // Unlock write, still hold read
		     }
		   }
		   try {
		     use(data);
		   } finally {
		     rwl.readLock().unlock();
		   }
		 }

		private void use(Object data2) {
			// TODO Auto-generated method stub
			System.out.println(data2);
		}
	}
	
	@Test
	public void reentrantReadWriteLockTest(){
		//预期----所有线程输出的内容都是一样的
		final CachedData cachedData=new CachedData();
		Callable<Void> callable = new Callable<Void>() {

			@Override
			public Void call() throws Exception {
				cachedData.processCachedData();
				return null;

			}
		};
		List<Future<Void>> results = CollectionHelper.addCallableCollection(service, threadNum, callable);
		CollectionHelper.iteratorResult(results);
	}
	
	class RWDictionary<K,V> {
		private final Map<K, V> m = new TreeMap<K, V>();
		private final ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
		private final Lock r = rwl.readLock();
		private final Lock w = rwl.writeLock();

		public V get(K key) {
			r.lock();
			try {
				return m.get(key);
			} finally {
				r.unlock();
			}
		}

		public Set<K> allKeys() {
			r.lock();
			try {
				return  m.keySet();
			} finally {
				r.unlock();
			}
		}

		public V put(K key, V value) {
			w.lock();
			try {
				return m.put(key, value);
			} finally {
				w.unlock();
			}
		}

		public void clear() {
			w.lock();
			try {
				m.clear();
			} finally {
				w.unlock();
			}
		}
	}
	
	/**
	 * 预期----不断添加数据/删除数据,不会出现异常情况
	 */
	@Test
	public void rwDictionary(){
		final RWDictionary<Integer, Integer> rwDictionary = new RWDictionary<Integer, Integer>();
		//添加数据
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				final AtomicInteger count = new AtomicInteger(0);
				Callable<Void> putDataCallable = new Callable<Void>() {

					@Override
					public Void call() throws Exception {
						// TODO Auto-generated method stub
						while (true) {
							Integer putData = rwDictionary.put(count.incrementAndGet(), count.incrementAndGet());
							System.out.println("putData," + putData);
							// 插入数据后 休眠一段时间 不然其它线程无法获取数据
							Thread.sleep(500);
						}
					}
				};
				List<Future<Void>> results = CollectionHelper.addCallableCollection(service, 1, putDataCallable);
				CollectionHelper.iteratorResult(results);
			}
		}).start();
		// 获取数据
		new Thread(new Runnable() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				Callable<Void> getDataCallable = new Callable<Void>() {

					@Override
					public Void call() throws Exception {
						// TODO Auto-generated method stub
						while (true) {
							Set<Integer> keys = rwDictionary.allKeys();
							Set<Integer> keysCopy=new HashSet<>(keys);
							for (Iterator<Integer> iterator = keysCopy.iterator(); iterator.hasNext();) {
								Integer key = (Integer) iterator.next();
								System.out.println("getData," + key + ":" + rwDictionary.get(key));
							}
							Thread.sleep(1000);
						}
					}
				};
				List<Future<Void>> resultsGet = CollectionHelper.addCallableCollection(service, 1, getDataCallable);
				CollectionHelper.iteratorResult(resultsGet);
			}
		}).start();
		while(true);
	}
}
