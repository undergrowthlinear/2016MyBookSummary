package com.undergrowth.zookeeper.paxos.test;

import com.undergrowth.zookeeper.paxos.CuratorLearn;
import org.junit.Before;
import org.junit.Test;

/**
 * @author zhangwu
 * @version 1.0.0
 * @description 创建Zookeeper模拟器
 * @date 2017-01-15-19:54
 */
public class CuratorZookeeperServerTest {

	CuratorLearn curator = null;

	@Before
	public void before() {
		curator = new CuratorLearn();
	}

	@Test
	public void serverClusterTest() {
		curator.serverCluster(3);
	}

	@Test
	public void testingServerTest() {
		String pathname="d:\\";
		String path="/zookeeper";
		curator.testingServer(pathname, path);
		while (true);
	}

}
