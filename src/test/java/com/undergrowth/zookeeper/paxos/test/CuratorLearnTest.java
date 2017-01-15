package com.undergrowth.zookeeper.paxos.test;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import com.undergrowth.zookeeper.paxos.CuratorLearn;

/**
 * 
 * Description: TODO(这里用一句话描述这个类的作用) curator----guava is to java what curator is
 * to zookeeper netflix/fluent
 * 
 * @author <a href="undergrowth@126.com">undergrowth</a>
 * @date 2016年8月4日
 * @version 1.0.0
 */
public class CuratorLearnTest {

	CuratorLearn curator = null;
	String connectString = null;
	String namespace = null;
	String nodeName = null;
	String nodeValue = null;

	@Before
	public void before() {
		curator = new CuratorLearn();
		//connectString = "192.168.126.129:2181,192.168.126.131:2181,192.168.126.131:3181";
		connectString = "127.0.0.1:2181";
		namespace = "curator";
		nodeName = "/curatornodetest/child1";
		nodeValue = "curatornodetest nodeValue data";
		curator.connectFluent(connectString, namespace);
	}

	@Test
	public void connectTest() {
		// curator.connect(connectString);
	}

	@Test
	public void connectFluentTest() {
		// curator.connectFluent(connectString, namespace);
	}

	@Test
	public void createNodeTest() {
		assertEquals("create fail", nodeName, curator.createNode(nodeName, nodeValue));
	}

	@Test
	public void deleteNodeTest() {
		nodeName = "/curatornodetest/child2";
		assertTrue("delete fail", curator.deleteNode(nodeName));
	}

	@Test
	public void getDataTest() {
		assertEquals("getData fail", nodeValue, curator.getData(nodeName));
	}

	@Test
	public void setDataTest() {
		String newData = "change new data";
		assertFalse("setData fail", curator.setData(nodeName, newData));
	}

	@Test
	public void createNodeAsyncTest() {
		nodeName = "/curatornodetest/child2";
		curator.createNodeAsync(nodeName, nodeValue);
	}

	@Test
	public void nodeCacheTest() {
		nodeName = "/curatornodetest/child2";
		curator.nodeCache(nodeName, 2, "new CacheData");
	}

	@Test
	public void pathChildrenCacheTest() {
		String newData = "change child2 new data";
		nodeName = "/curatornodetest/child2";
		curator.pathChildrenCache(nodeName, 3, newData);
	}

	@Test
	public void masterSelectTest1() {
		nodeName = "/curatornodetest/master";
		curator.masterSelect(nodeName);
	}

	@Test
	public void masterSelectTest2() {
		nodeName = "/curatornodetest/master";
		curator.masterSelect(nodeName);
	}

	@Test
	public void disLockTest() {
		curator.disLock(100);
	}

	@Test
	public void distributeLockTest() {
		nodeName = "/curatornodetest/distributeLock";
		curator.distributeLock(nodeName, 100);
	}

	@Test
	public void distriAtomicTest() {
		nodeName = "/curatornodetest/distriAtomic";
		curator.distriAtomic(nodeName);
	}

	@Test
	public void cyclicBarrierTest() {
		curator.cyclicBarrier(100);
	}

	@Test
	public void distriCyclicBarrierTest() {
		nodeName = "/curatornodetest/barrierPath";
		curator.distriCyclicBarrier(connectString, nodeName, 3);
	}

	@Test
	public void zkpathsTest() {
		nodeName = "/curatornodetest/zkpaths";
		curator.zkpaths(namespace, nodeName, "/sub");
	}

	@Test
	public void ensurePathTest() {
		nodeName = "/curatornodetest/ensurePath";
		curator.ensurePath(nodeName);
	}
	


}
