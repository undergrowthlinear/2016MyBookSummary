package com.undergrowth.zookeeper.paxos;

import java.util.concurrent.CountDownLatch;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomChildrenCache implements PathChildrenCacheListener {

  private Logger logger = LoggerFactory.getLogger(CustomChildrenCache.class);
  private PathChildrenCache childrenCache;
  private CountDownLatch countDownLatch;

  public CustomChildrenCache(PathChildrenCache childrenCache, CountDownLatch countDownLatch) {
    // TODO Auto-generated constructor stub
    this.childrenCache = childrenCache;
    this.countDownLatch = countDownLatch;
  }

  @Override
  public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
    // TODO Auto-generated method stub
    switch (event.getType()) {
      case CHILD_ADDED:
        logger.info("CHILD_ADDED:" + event.getData().getPath());
        break;
      case CHILD_UPDATED:
        logger.info("CHILD_UPDATED:" + event.getData().getPath());
        break;
      case CHILD_REMOVED:
        logger.info("CHILD_REMOVED:" + event.getData().getPath());
        break;
      default:
        break;
    }
    countDownLatch.countDown();
  }

}
