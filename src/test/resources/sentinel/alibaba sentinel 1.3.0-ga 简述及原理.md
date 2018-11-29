# alibaba sentinel 1.3.0-GA 简述及原理
## 概述
- 参考
- https://github.com/alibaba/Sentinel/wiki/%E4%BB%8B%E7%BB%8D  ----官网
- https://github.com/sentinel-group/sentinel-awesome           ----其他人文档
- https://github.com/spring-cloud-incubator/spring-cloud-alibaba/blob/master/Roadmap-zh.md alibaba的spring分支相关蓝图
- https://github.com/spring-cloud-incubator/spring-cloud-alibaba alibaba的spring cloud分支
- 简述
    - 对于alibaba 中间件团队全面拥抱开源,个人觉得是件特别牛逼和伟大的事情(虽然跟我没啥关系),估计在目前的互联网领域还真没多少有些像阿里这个级别的,之前用过fastjson/druid,还有像arthas等之类的,都觉得很赞
    - 微服务的整个体系,公司之前都是用的eureka+ribbon+feign+hystrix为基础构建的,其实对于很多小公司而言,当没有进行比较系统的压力测试的时候,其实根本就不知道单机的吞吐量/rt/集群的吞吐量之类的,而不用说像大公司的服务降级/系统过载保护,公司线上就遇到过由于单个服务拖垮整个集群的例子,后面对数据库进行了业务的物理隔离解决类似问题,前两天看到sentinel就觉得是个好东西,用于流量的监控/系统服务的降级/以及系统过载保护等等,至少个人感觉对于很多公司而言,节省大量的人力开发成本,花了几天看了整个文档,又把示例和源码大致撸了边,留个念想
### 核心Code
```
To get total statistics of the same resource in different context, same resource
  shares the same {@link ClusterNode} globally
  
代码里面找到的 就是看到了这句话 才理解了整个代码体系 意思是说同一个资源在不同的线程中记录不同的统计信息(eg:DefaultNode记录),但是同一资源的所有线程统计是记录在ClusterNode,且对于同一资源是全局共享一份的
```
#### ProcessorSlot(插槽处理器基类)
- NodeSelectorSlot/ClusterBuilderSlot/StatisticSlot/LogSlot/SystemSlot/AuthoritySlot/FlowSlot/DegradeSlot
- 通过DefaultSlotChainBuilder.build创建插槽处理链,对每次请求进行链式处理
```
ProcessorSlotChain chain = new DefaultProcessorSlotChain();
    chain.addLast(new NodeSelectorSlot());
    chain.addLast(new ClusterBuilderSlot());
    chain.addLast(new LogSlot());
    chain.addLast(new StatisticSlot());
    chain.addLast(new SystemSlot());
    chain.addLast(new AuthoritySlot());
    chain.addLast(new FlowSlot());
    chain.addLast(new DegradeSlot())
```
- NodeSelectorSlot(用于创建DefaultNode,挂在于树形节点最后)
- ClusterBuilderSlot(创建ClusterNode或者已创建关联到相关的DefaultNode)
- StatisticSlot(node级别的计数/cluster级别的计数/syatem级别的计数/热点数据的计数均在此)
```
            fireEntry(context, resourceWrapper, node, count, args);
            node.increaseThreadNum();
            node.addPassRequest();

            if (context.getCurEntry().getOriginNode() != null) {
                context.getCurEntry().getOriginNode().increaseThreadNum();
                context.getCurEntry().getOriginNode().addPassRequest();
            }

            if (resourceWrapper.getType() == EntryType.IN) {
                Constants.ENTRY_NODE.increaseThreadNum();
                Constants.ENTRY_NODE.addPassRequest();
            }

            for (ProcessorSlotEntryCallback<DefaultNode> handler : StatisticSlotCallbackRegistry.getEntryCallbacks()) {
                handler.onPass(context, resourceWrapper, node, count, args);
            }
```
- FlowSlot(根据相关规则和node统计信息,判断有没有超过流量阈值)
    - com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager.checkFlow
    - com.alibaba.csp.sentinel.slots.block.flow.FlowRule.passCheck
    - Node selectedNode = selectNodeByRequesterAndStrategy(origin, context, node);----重要的一行,根据规则的origin、strategy是返回DefaultNode级别还是ClusterNode级别的统计信息,进而进行阈值的判断
#### Node(资源的统计信息)
- StatisticNode/DefaultNode/ClusterNode/EntranceNode
- EntranceNode为单个资源树形结构的顶层,每一个entry创建一个,均挂在与machine-root目录下
- DefaultNode每一个entry即创建一个,记录每一个资源每一个线程的线程数与请求数,继承于StatisticNode
- ClusterNode统一资源全局共享一份,记录每一个资源所有线程的线程数与请求数,继承于StatisticNode
- StatisticNode里面的三个参数负责请求线程和请求次数的记录
    - rollingCounterInSecond----一秒钟内的统计信息
    - rollingCounterInMinute----一分钟内的统计信息
    - curThreadNum----资源请求线程数
#### @SentinelResource 和 SentinelResourceAspect
- 利用aspectj进行aop的处理
- 把SentinelResourceAspect#invokeResourceWithSentinel这个方法看一遍,就知道怎么回事了
```
  String resourceName = getResourceName(annotation.value(), originMethod);
        EntryType entryType = annotation.entryType();
        Entry entry = null;
        try {
            ContextUtil.enter(resourceName);
            entry = SphU.entry(resourceName, entryType);
            Object result = pjp.proceed();
            return result;
        } catch (BlockException ex) {
            return handleBlockException(pjp, annotation, ex);
        } finally {
            if (entry != null) {
                entry.exit();
            }
            ContextUtil.exit();
        }
```
- 如上,创建context/entry,即走上面分析的slot和node逻辑,未违反规则,则执行目标方法,如果违反,则抛出异常,进行BlockException/DegradeException的相应回调blockHandler/fallback
#### 示例
- 拿源码的示例,com.alibaba.csp.sentinel.demo.flow.FlowQpsDemo#main进行举例吧
    - 调整threadCount=1,便于调试
    - com.alibaba.csp.sentinel.SphU.entry
        - com.alibaba.csp.sentinel.CtSph.entry
            - context = MyContextUtil.myEnter  ----创建Context,创建sentinel_default_context的EntranceNode为基节点
            - chain = lookProcessChain          ----即是上面的slot链式
            - chain.entry(context, resourceWrapper, null, count, args) ---- 链式的处理,分别调用链式里面的slot进行上面分析的处理
            
