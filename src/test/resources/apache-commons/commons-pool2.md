# apache-comnons系列之commons-pool2.4 学习笔记
## 概述
- 参考
  - http://www.jianshu.com/p/b0189e01de35
  - https://my.oschina.net/xinxingegeya/blog/391560
  - https://segmentfault.com/a/1190000003920723
  - https://www.idaima.com/article/1975
- ObjectPool
      - A pooling simple interface.
    - GenericObjectPool
      - A configurable {@link ObjectPool} implementation.
    - GenericObjectPoolConfig
      - A simple "struct" encapsulating the configuration for a {@link GenericObjectPool}.
- KeyedObjectPool
      - A "keyed" pooling interface.
    - GenericKeyedObjectPool
      - A configurable <code>KeyedObjectPool</code> implementation.
    - GenericKeyedObjectPoolConfig
      - A simple "struct" encapsulating the configuration for a {@link GenericKeyedObjectPool}.
- BaseGenericObjectPool
    - Base class that provides common functionality for {@link GenericObjectPool} and {@link GenericKeyedObjectPool}. The primary reason this class exists is reduce code duplication between the two pool implementations.
- BaseObjectPoolConfig
    - Provides the implementation for the common attributes shared by the sub-classes. New instances of this class will be created using the defaults defined by the public constants.
- GenericObjectPoolMXBean
    - Defines the methods that will be made available via JMX.
- PooledObjectFactory
      - An interface defining life-cycle methods for instances to be served by an {@link ObjectPool}.
   - BasePooledObjectFactory
      - A base implementation of <code>PoolableObjectFactory</code>.
- PooledObject
      - Defines the wrapper that is used to track the additional information, such as state, for the pooled objects. Implementations of this class are required to be thread-safe.
  - DefaultPooledObject
      - This wrapper is used to track the additional information, such as state, for the pooled objects.
- PooledObjectState
      - Provides the possible states that a {@link PooledObject} may be in.
  - IDLE/ALLOCATED/EVICTION/ABANDONED/RETURNING
- PoolUtils
  - This class consists exclusively of static methods that operate on or return ObjectPool or KeyedObjectPool related interfaces.
## 测试
- org.apache.commons.pool2.impl.TestDefaultPooledObject
    - allocate
      - Allocates the object.
      - synchronized/state = PooledObjectState.ALLOCATED;/lastBorrowTime/lastUseTime/borrowedCount
    - deallocate
      - Deallocates the object and sets it {@link PooledObjectState#IDLE IDLE} if it is currently {@link PooledObjectState#ALLOCATED ALLOCATED}.
      - synchronized/state = PooledObjectState.IDLE;/lastReturnTime
- org.apache.commons.pool2.TestBasePoolableObjectFactory
    - All operations defined here are essentially no-op's.
    - abstract T create()
    - abstract wrap(T obj);
- org.apache.commons.pool2.impl.TestGenericObjectPool
    - allObjects
    - idleObjects
    - ONAME_BASE
    - abandonedConfig
    - borrowObject
      - quivalent to <code>{@link #borrowObject(long) borrowObject}({@link #getMaxWaitMillis()})</code>.
      - idleObjects
        - idleObjects.pollFirst();
      - create
    - returnObject
      - If {@link #getMaxIdle() maxIdle} is set to a positive value and the  number of idle instances has reached this value, the returning instance is destroyed.
      - factory.validateObject(p)
      - factory.passivateObject(p);
      - idleObjects.addFirst(p);
      - updateStatsReturn(activeTime);
    - create
      - Attempts to create a new wrapped pooled object.
      - p = factory.makeObject();
      - createdCount.incrementAndGet();
      - allObjects.put(new IdentityWrapper<T>(p.getObject()), p);
    - destroy
      - Destroys a wrapped pooled object.
      - toDestory.invalidate();
      - idleObjects.remove(toDestory);/
      - allObjects.remove(new IdentityWrapper<T>(toDestory.getObject()));
      - factory.destroyObject(toDestory);
    - invalidateObject
      - Activation of this method decrements the active count and attempts to destroy the instance.
    - clear
      - Clears any objects sitting idle in the pool by removing them from the idle instance pool and then invoking the configured {@link PooledObjectFactory#destroyObject(PooledObject)} method on each idle instance.
      - idleObjects.poll();
      - destroy(p);
- org.apache.commons.pool2.impl.TestGenericKeyedObjectPool
    - keypool类似于pool,不同之处在于对同一key的操作委托给了ObjectDeque
    - 同时多了key的添加与已移除
    - poolMap
    - poolKeyList
    - keyLock
    - ONAME_BASE
    - ObjectDeque
      - Maintains information on the per key queue for a given key.
      - idleObjects
      - allObjects
      - createCount
    - borrowObject
      - register(key);
      - p = objectDeque.getIdleObjects().pollFirst();
      - p = create(key);
      - deregister(key);
    - register
      - Register the use of a key by an object.
      - register() and deregister() must always be used as a pair.
      - poolMap.get(k);
      - objectDeque.getNumInterested().incrementAndGet();
    - deregister
      - De-register the use of a key by an object.
      - register() and deregister() must always be used as a pair.
      - poolMap.get(k);
      - objectDeque.getNumInterested().decrementAndGet();
    - create
      - Create a new pooled object.
      - objectDeque = poolMap.get(key);
      - p = factory.makeObject(key);
      - objectDeque.getAllObjects().put(new IdentityWrapper<T>(p.getObject()), p);
  - TestPoolUtils
    - checkMinIdle
      - Periodically check the idle object count for the key in the keyedPool. At most one idle object will be added per period. If there is an exception
      - when calling {@link KeyedObjectPool#addObject(Object)} then no more checks for that key will be performed.