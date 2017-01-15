package com.undergrowth.zookeeper.paxos;

import java.util.concurrent.CountDownLatch;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.api.BackgroundCallback;
import org.apache.curator.framework.api.CuratorEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomBackgrundCallbaxk implements BackgroundCallback {

	private Logger logger= LoggerFactory.getLogger(CustomBackgrundCallbaxk.class);
	private CountDownLatch countDownLatch=null;
	
	public CustomBackgrundCallbaxk(CountDownLatch countDownLatch) {
		// TODO Auto-generated constructor stub
		this.countDownLatch=countDownLatch;
	}

	@Override
	public void processResult(CuratorFramework client, CuratorEvent event) throws Exception {
		// TODO Auto-generated method stub
		logger.info(Thread.currentThread().getName()+":"+event.getPath()+":"+event.getType()+":"+event.getResultCode());
		countDownLatch.countDown();
	}

}
