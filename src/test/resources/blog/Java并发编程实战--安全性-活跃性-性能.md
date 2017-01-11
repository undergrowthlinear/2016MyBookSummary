##2016书单总结--Java并发编程实战--安全性-活跃性示例
Java的内存模型支持一次编写,随处运行
每个线程拥有自己的虚拟机栈、局部变量、程序计数器,共享进程中堆上的共享变量,共享方法区(永久内存区/常量区)的class与meta信息

1. 并发的来源----提升资源的利用率、提升模块的公平性、提高任务交互的便利性
2. 线程的优势----利用多核处理器(摩尔定律----当成本不变的前提下,每个18-24个月,集成电路上的芯片数目翻一倍,性能提升一倍)、建模的简化、异步执行的简化、灵敏度的提高
3. 线程的问题--安全性(永远不让糟糕的事情发生----多线程访问共享数据,执行的顺序问题)
            活跃性(正确的事情最终要发生----死锁、饥饿)
            性能(尽快的执行----线程上下文的切换、cpu资源的消耗)
4. 线程的状态--
** 
new(创建)--start-->Runnable(就绪)--调度器调度-->Running(运行)--run-->Dead(死亡)
 Running(运行)--synchronized--Block(锁池/entry set)--获得锁-->Runnable(就绪)
 Running(运行)--wait--Block(等待池/wait set)--notify/notifyAll-->Block(锁池/entry set)--获得锁-->Runnable(就绪)
 Running(运行)--sleep/join--Block(其他等待)--条件达到-->Runnable(就绪)
**
5. 同步工具类--
**
synchronized----使用对象的内置锁(互斥锁)与内置条件队列进行同步/wait nofity nitofyAll为内部条件队列的api/可重入(获取锁的操作粒度是线程)
CountDownLatch--为同步工具类之Latch(闭锁)--多个线程等待一个任务/一个线程等待多个任务/一次性状态
FutureTask--表示一种抽象的可生成结果的运算
Semaphore--控制同时访问特定资源的数目/不可重入
CyclicBarrier--升级版闭锁/可重置状态/等待线程到达栅栏位置,通过栅栏后,可执行特定操作
**
6. 死锁--每个人都拥有其他人需要的资源,而又等待其他人已经拥有的资源,每个人在获取需要的
**
死锁有向图----线程为节点,等待的资源为边,当出现回环时,出现死锁
Lock-Ordering Deadlock(锁顺序死锁/动态的锁顺序死锁)----不同顺序获取相同的锁
开放调用----调用方法无需持有锁
线程饥饿死锁(Thread Starvation Deadlock)----例如在单个线程池中,执行某个任务,执行任务时提交任务到线程池的队列中,等待提交的任务执行完成,
此时会出现死锁,因为提交的任务在队列中,等待当前任务执行完才能进行调用,而当前任务又等待提交任务的结果,这样形成了死循环,造成线程饥饿死锁
饥饿----由于线程的优先级导致有些线程无法调用到
活锁----由于执行毒药消息导致线程一直重复尝试错误的消息,将不可修复的错误当作可修复的错误
线程转储----Thread Dump----查看锁/线程/死锁等信息
在运行测试代码时，可通过visualvm查看死锁情况
**
7. 内置锁与显示锁
**
Q1:内置锁与显示锁的区别
A1:synchronized----内置锁----当资源被占用时,调用者进入休眠状态
Lock----显示锁----支持无条件/可中断/可定时/可轮询
Q2:乐观锁与悲观锁的区别
Q2:悲观锁在每次访问数据时,都进行加锁操作,适用于多写的场景
乐观锁----每次访问数据不进行加锁,当更新操作时,会去回补哪些操作过数据,适用于多读的场景
Q3:自旋锁
A3:程序通过循环等待而不是休眠
see----ReentrantLockTest/ReentrantReadWriteLockTest
内置锁与显示锁相比----
显示锁提供了一些扩展的功能,例如可定时/可中断/可轮询,性能方面差别不大,与内置锁具有相同的互斥性/内存可见性/可重入
读写锁允许多个读线程并发的访问被保护的数据,当以读操作为主的数据结构时,可提升性能
**
8. 示例代码位于-- https://github.com/undergrowthlinear/2016MyBookSummary.git
**
关于多线程情况下交替访问会导致竞态条件的代码示例在com.undergrowth.java.concurrency.practice.test.ConcurrencyProfile中
   关于CountDownLatch在com.undergrowth.java.concurrency.practice.test.SynchronizedToolTest.countDownLatchTest有示例演示
   关于FutureTask在com.undergrowth.java.concurrency.practice.test.SynchronizedToolTest.futureTaskTest有示例演示
   关于Semaphore在com.undergrowth.java.concurrency.practice.test.SynchronizedToolTest.semaphoreAddDelTest信号量的添加与获取
   关于CyclicBarrier在com.undergrowth.java.concurrency.practice.test.SynchronizedToolTest.cyclicBarrierTest有示例演示
   关于线程饥饿死锁在com.undergrowth.java.concurrency.practice.test.DeadLockTest.threadStarTest有代码示例
   关于顺序死锁在com.undergrowth.java.concurrency.practice.test.DeadLockTest.orderDeadlockTest代码示例
   关于显示锁在com.undergrowth.java.concurrency.practice.test.LockTest.boundBufferTest有示例代码
   关于可重进入锁com.undergrowth.java.concurrency.practice.test.ReentrantLockTest有代码示例
   关于读写锁在com.undergrowth.java.concurrency.practice.test.ReentrantReadWriteLockTest.rwDictionary有代码示例
**   