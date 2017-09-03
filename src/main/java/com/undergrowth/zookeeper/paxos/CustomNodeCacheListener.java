package com.undergrowth.zookeeper.paxos;

import java.util.concurrent.CountDownLatch;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomNodeCacheListener implements NodeCacheListener {

  private Logger logger = LoggerFactory.getLogger(CustomNodeCacheListener.class);
  private NodeCache nodeCache;
  private CountDownLatch countDownLatch;

  public CustomNodeCacheListener(NodeCache nodeCache, CountDownLatch countDownLatch) {
    // TODO Auto-generated constructor stub
    this.nodeCache = nodeCache;
    this.countDownLatch = countDownLatch;
  }

  @Override
  public void nodeChanged() throws Exception {
    // TODO Auto-generated method stub
    if (nodeCache != null && nodeCache.getCurrentData() != null) {
      logger.info(Thread.currentThread().getName() + ":" + nodeCache.toString() + ":"
          + new String(nodeCache.getCurrentData().getData()));
    }
    countDownLatch.countDown();
  }

}
