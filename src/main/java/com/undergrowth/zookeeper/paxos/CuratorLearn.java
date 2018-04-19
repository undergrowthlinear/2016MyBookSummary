package com.undergrowth.zookeeper.paxos;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.atomic.AtomicValue;
import org.apache.curator.framework.recipes.atomic.DistributedAtomicInteger;
import org.apache.curator.framework.recipes.barriers.DistributedDoubleBarrier;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCache.StartMode;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListenerAdapter;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.retry.RetryNTimes;
import org.apache.curator.utils.EnsurePath;
import org.apache.curator.utils.ZKPaths;
import org.apache.curator.utils.ZKPaths.PathAndNode;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Description: TODO(这里用一句话描述这个类的作用) curator----guava is to java what curator is to zookeeper netflix/fluent
 *
 * @author <a href="undergrowth@126.com">undergrowth</a>
 * @version 1.0.0
 * @date 2016年8月4日
 */
public class CuratorLearn {

    private Logger logger = LoggerFactory.getLogger(CuratorLearn.class);
    CuratorFramework client = null;

    public void connect(String connectString) {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        client = CuratorFrameworkFactory.newClient(connectString, 15000, 10000, retryPolicy);
        client.start();
        logger.info("connect to " + connectString + " successfully");
    }

    public void connectFluent(String connectString, String namespace) {
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        client = CuratorFrameworkFactory.builder().connectString(connectString).sessionTimeoutMs(5000)
            .retryPolicy(retryPolicy).namespace(namespace).build();
        client.start();
        logger.info("connect to " + connectString + " successfully");
    }

    public String createNode(String path, String data) {
        try {
            return client.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT).forPath(path,
                data.getBytes());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            logger.error(e.getMessage());
            // e.printStackTrace();
        }
        return null;
    }

    public void createNodeAsync(String path, String data) {
        CountDownLatch countDownLatch = new CountDownLatch(2);
        ExecutorService executor = Executors.newCachedThreadPool();
        try {
            client.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT)
                .inBackground(new CustomBackgrundCallbaxk(countDownLatch), executor)
                .forPath(path, data.getBytes());
            client.create().creatingParentsIfNeeded().withMode(CreateMode.PERSISTENT)
                .inBackground(new CustomBackgrundCallbaxk(countDownLatch)).forPath(path, data.getBytes());
            countDownLatch.await();
            executor.shutdown();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            logger.error(e.getMessage());
        }
    }

    public boolean deleteNode(String path) {
        Stat stat = new Stat();
        try {
            client.getData().storingStatIn(stat).forPath(path);
            logger.info(
                "stat:" + stat.getCtime() + "\t" + stat.getCversion() + "\t" + stat.getMzxid() + "\t"
                    + stat.getVersion());
            client.delete().deletingChildrenIfNeeded().withVersion(stat.getVersion()).forPath(path);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            logger.error(e.getMessage());
            return false;
        }
        return true;
    }

    public String getData(String path) {
        Stat stat = new Stat();
        try {
            String result = new String(client.getData().storingStatIn(stat).forPath(path));
            logger.info("stat:" + stat);
            return result;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            logger.error(e.getMessage());
            return null;
        }
    }

    public boolean setData(String path, String newData) {
        Stat stat = new Stat();
        try {
            client.getData().storingStatIn(stat).forPath(path);
            logger.info("stat:" + stat);
            logger.info("stat:" + client.setData().withVersion(stat.getVersion())
                .forPath(path, newData.getBytes()));
            logger.info("stat:" + client.setData().withVersion(stat.getVersion())
                .forPath(path, newData.getBytes()));
            return true;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            logger.error(e.getMessage());
            return false;
        }
    }

    /**
     * 修改数据、删除节点、新增节点都会触发通知事件
     */
    public boolean nodeCache(String path, int count, String newData) {
        CountDownLatch countDownLatch = new CountDownLatch(count);
        ExecutorService executor = Executors.newCachedThreadPool();
        NodeCache nodeCache = new NodeCache(client, path, false);
        try {
            // 启动
            nodeCache.start(true);
            nodeCache.getListenable()
                .addListener(new CustomNodeCacheListener(nodeCache, countDownLatch), executor);
            client.setData().forPath(path, newData.getBytes());
            Thread.sleep(5000);
            // 会触发
            client.delete().deletingChildrenIfNeeded().forPath(path);
            countDownLatch.await();
            return true;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            logger.error(e.getMessage());
            return false;
        }
    }

    public boolean pathChildrenCache(String path, int count, String newData) {
        CountDownLatch countDownLatch = new CountDownLatch(count);
        ExecutorService executor = Executors.newCachedThreadPool();
        PathChildrenCache childrenCache = new PathChildrenCache(client, path, false);
        try {
            childrenCache.start(StartMode.POST_INITIALIZED_EVENT);
            childrenCache.getListenable()
                .addListener(new CustomChildrenCache(childrenCache, countDownLatch), executor);
      /*
             * client.create().withMode(CreateMode.PERSISTENT).forPath(path);
			 * Thread.sleep(5000);
			 */
            client.create().withMode(CreateMode.PERSISTENT).forPath(path + "/cc");
            Thread.sleep(5000);
            client.setData().forPath(path + "/cc", newData.getBytes());
            Thread.sleep(5000);
            client.delete().forPath(path + "/cc");
            Thread.sleep(5000);
            return true;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            logger.error(e.getMessage());
            return false;
        }
    }

    public boolean masterSelect(String leaderPath) {
        LeaderSelector leaderSelector = new LeaderSelector(client, leaderPath,
            new LeaderSelectorListenerAdapter() {

                @Override
                public void takeLeadership(CuratorFramework client) throws Exception {
                    // TODO Auto-generated method stub
                    logger.info(Thread.currentThread().getName() + ":成为master");
                    Thread.sleep(5000);
                    logger.info(Thread.currentThread().getName() + ":释放master");
                }
            });
        leaderSelector.autoRequeue();
        leaderSelector.start();
        try {
            Thread.sleep(Integer.MAX_VALUE);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            logger.error(e.getMessage());
            return false;
        }
        return true;
    }

    public boolean disLock(int num) {
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        for (int i = 0; i < num; i++) {
            new Thread(new Runnable() {

                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    try {
                        countDownLatch.await();
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss|SSS");
                        logger.info(simpleDateFormat.format(new Date(System.currentTimeMillis())));
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        logger.error(e.getMessage());
                    }
                }
            }).start();
        }
        countDownLatch.countDown();
        return true;
    }

    /**
     * 分布式锁
     */
    public boolean distributeLock(String lockPath, int num) {
        final InterProcessMutex mutex = new InterProcessMutex(client, lockPath);
        final CountDownLatch countDownLatch = new CountDownLatch(1);
        final CountDownLatch endGate = new CountDownLatch(num);
        for (int i = 0; i < num; i++) {
            new Thread(new Runnable() {

                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    try {
                        countDownLatch.await();
                        mutex.acquire();
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss|SSS");
                        logger.info(simpleDateFormat.format(new Date(System.currentTimeMillis())));
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        logger.error(e.getMessage());
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    } finally {
                        try {
                            mutex.release();
                            endGate.countDown();
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                            logger.error(e.getMessage());
                        }
                    }
                }
            }).start();
        }
        countDownLatch.countDown();
        try {
            endGate.await();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            logger.error(e.getMessage());
        }
        return true;
    }

    /**
     * 分布式原子计数
     */
    public boolean distriAtomic(String path) {
        DistributedAtomicInteger atomicInteger = new DistributedAtomicInteger(client, path,
            new RetryNTimes(3, 100));
        try {
            AtomicValue<Integer> res = atomicInteger.add(10);
            return res.succeeded();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            logger.error(e.getMessage());
        }
        return false;
    }

    public boolean cyclicBarrier(int num) {
        CyclicBarrier cyclicBarrier = new CyclicBarrier(num);
        ExecutorService executorService = Executors.newCachedThreadPool();
        for (int i = 0; i < num; i++) {
            executorService.submit(new CustomRunnable(cyclicBarrier));
        }
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            logger.error(e.getMessage());
            return false;
        }
        executorService.shutdown();
        return true;
    }

    public class CustomRunnable implements Runnable {

        private CyclicBarrier cyclicBarrier;

        public CustomRunnable(CyclicBarrier cyclicBarrier) {
            // TODO Auto-generated constructor stub
            this.cyclicBarrier = cyclicBarrier;
        }

        @Override
        public void run() {
            // TODO Auto-generated method stub
            logger.info(Thread.currentThread().getName() + ":准备开始跑步了");
            try {
                cyclicBarrier.await();
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                logger.error(e.getMessage());
            } catch (BrokenBarrierException e) {
                // TODO Auto-generated catch block
                logger.error(e.getMessage());
            }
            logger.info(Thread.currentThread().getName() + ":跑步");
        }

    }

    public class CustomRunnable2 implements Runnable {

        private DistributedDoubleBarrier doubleBarrier;

        public CustomRunnable2(DistributedDoubleBarrier doubleBarrier) {
            // TODO Auto-generated constructor stub
            this.doubleBarrier = doubleBarrier;
        }

        @Override
        public void run() {
            // TODO Auto-generated method stub
            try {
                doubleBarrier.enter();
                logger.info(Thread.currentThread().getName() + ":准备开始跑步了");
                doubleBarrier.leave();
                logger.info(Thread.currentThread().getName() + ":跑步");
            } catch (Exception e) {
                // TODO Auto-generated catch block
                logger.error(e.getMessage());
            }
        }

    }

    public boolean distriCyclicBarrier(String connectString, String barrierPath, int memberQty) {

        ExecutorService executorService = Executors.newCachedThreadPool();
        for (int i = 0; i < memberQty; i++) {
            RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
            CuratorFramework client = CuratorFrameworkFactory
                .newClient(connectString, 15000, 10000, retryPolicy);
            client.start();
            DistributedDoubleBarrier doubleBarrier = new DistributedDoubleBarrier(client, barrierPath,
                memberQty);
            executorService.submit(new CustomRunnable2(doubleBarrier));
        }
        try {
            Thread.sleep(50000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            logger.error(e.getMessage());
            return false;
        }
        executorService.shutdown();
        return true;
    }

    public boolean zkpaths(String namespace, String path, String child) {
        try {
            ZooKeeper zo = client.getZookeeperClient().getZooKeeper();
            logger.info(ZKPaths.fixForNamespace(namespace, path));
            logger.info(ZKPaths.makePath(path, child));
            logger.info(ZKPaths.getNodeFromPath(path + child));
            PathAndNode pathAndNode = ZKPaths.getPathAndNode(path + child);
            logger.info(pathAndNode.getPath());
            logger.info(pathAndNode.getNode());
            ZKPaths.mkdirs(zo, path + "/zkpathsub");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            logger.error(e.getMessage());
            return false;
        }
        return true;
    }

    public boolean ensurePath(String path) {
        EnsurePath ensurePath = new EnsurePath(path);
        try {
            ensurePath.ensure(client.getZookeeperClient());
            ensurePath.ensure(client.getZookeeperClient());
            EnsurePath ensurePath2 = client.newNamespaceAwareEnsurePath("curator2");
            ensurePath2.ensure(client.getZookeeperClient());
        } catch (Exception e) {
            logger.error(e.getMessage());
            return false;
        }
        return true;
    }

  /*public boolean serverCluster(int num) {
    // System.setProperty("java.io.tmpdir", "");
    TestingCluster cluster = new TestingCluster(num);
    try {
      cluster.start();
      TestingZooKeeperServer leader = null;
      for (TestingZooKeeperServer zooKeeperServer : cluster.getServers()) {
        logger.info(zooKeeperServer.getInstanceSpec().getServerId() + "-"
            + zooKeeperServer.getQuorumPeer().getQuorumSize() + "-"
            + zooKeeperServer.getInstanceSpec().getDataDirectory() + "-"
            + zooKeeperServer.getQuorumPeer().getServerState());
        if ("leading".equals(zooKeeperServer.getQuorumPeer().getServerState())) {
          leader = zooKeeperServer;
        }
      }
      cluster.stop();
    } catch (Exception e) {
      // TODO Auto-generated catch block
      logger.error(e.getMessage());
      return false;
    }
    return true;
  }

  public boolean testingServer(String pathname, String path) {
    try {
        TestingServer testingServer = new TestingServer(2181, new File(pathname));
      RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
      client = CuratorFrameworkFactory
          .newClient(testingServer.getConnectString(), 15000, 10000, retryPolicy);
      client.start();
      logger.info(client.getChildren().forPath(path).size() + "");
    } catch (Exception e) {
      // TODO Auto-generated catch block
      logger.error(e.getMessage());
      return false;
    }
    return true;
  }*/

}
