# elasticsearch 5.2.2 学习笔记之源码阅读7-搜索流程总览之query_string查询
## 概述
- 参考
    - https://github.com/elastic/elasticsearch/tree/v5.2.2
    - https://blog.csdn.net/undergrowth/article/details/82851401
    - https://blog.csdn.net/undergrowth/article/details/82885628
## 搜索流程总览之query_string查询
- query_string查询
    - postman post http://localhost:9200/testindex/testlog/_search
```
{
    "query": {
        "query_string": {
            "query": "Elasticsearch"
        }
    }
}
```
### 核心接口与类
```
将query内部的query_string转为QueryBuilder(QueryParseContext#parseInnerQueryBuilder利用SearchModule#registerQueryParsers注册的解析器,将query_string转为QueryStringQueryBuilder)

搜索过程分两阶段操作,第一步为分片查询获取id,第二步为获取文档信息
```
- 前置加载过程与 https://blog.csdn.net/undergrowth/article/details/82851401 类似,主要看各个Action的不同
- 本文涉及到的Action
    - 查询
        - SearchAction/RestSearchAction/TransportSearchAction
- 使用AbstractComponent和AbstractLifecycleComponent管理核心组件的配置与生命周期
    - AbstractComponent
        - AbstractLifecycleComponent
            - SearchService(搜索相关服务的支持)
                - SearchService#executeQueryPhase(执行第一步分片查询)
                - SearchService#executeFetchPhase(执行第二步获取文档信息)
- SearchModule(搜索模块的支持,注册评分函数/查询解析器/排序/聚合/获取子阶段)
    - registerScoreFunctions
    - registerQueryParsers(eg:注册QueryStringQueryBuilder.NAME,用于将query的query_string转换为QueryStringQueryBuilder,构建成lucene的Query)
    - registerRescorers/registerSorts
- es搜索相关概念
    - SearchRequestParsers(将RestRequest转换为SearchRequest)
    - SearchSourceBuilder
    - ParseField(es查询中带解析的查询字段)
    - SearchContext(用于保存查询过程中的状态信息)
    - ShardSearchRequest(分片搜索请求)
    - SearchRequest(搜索请求)
    - SearchPhase(每个查询分为查询和获取两个阶段)
        - QueryPhase(根据查询在每一个分片返回文档id/得分/排序信息)
        - FetchPhase(获取特定的top文档信息返回给客户端)
    - QueryBuilder
        - AbstractQueryBuilder(将es查询转换为lucene查询的基类)
            - QueryStringQueryBuilder(query_string的查询构造器)
    - MapperQueryParser(es层的查询解析器,继承与lucene的QueryParser)
- lucene层相关概念支持
    - Query(lucene层的查询基类)
        - WildcardQuery(通配符查询)
        - TermQuery(精确查询)
    - QueryParser(lucene层的查询解析器,可替代Query的各种子类)
        - 参考---- https://blog.csdn.net/yelllowcong/article/details/78698506
    - TopDocs(搜索返回的结果集)
        - ScoreDoc(代表匹配的某一个文档)
        - TopFieldDocs(查询时引入排序机制的返回结果集)
        - SortField(存储排序相关的字段信息)
### 以query_string查询例讲解加载过程
- postman post http://localhost:9200/testindex/testlog/_search
    - 前置加载过程参看 https://blog.csdn.net/undergrowth/article/details/82851401
    - 分别来看SearchAction/RestSearchAction/TransportSearchAction三者间的消息扭转
        - 通过RestSearchAction的prepareRequest调用parseSearchRequest/parseSearchSource将RestRequest转换为SearchRequest,默认的searchType为QUERY_THEN_FETCH,这里调用SearchSourceBuilder#parseXContent转化请求的from/size/queryBuilder/sorts等,同时利用context.parseInnerQueryBuilder()将query_string转为QueryStringQueryBuilder
        - 通过之前说的回转到TransportSearchAction#doExecute,通过searchAsyncAction根据默认的QUERY_THEN_FETCH找到SearchQueryThenFetchAsyncAction并启动,
        转到父类AbstractSearchAsyncAction#start,遍历shardsIts进行第一步的分片查询performFirstPhase,对于位于其他节点的分片,通过sendExecuteFirstPhase发送ShardSearchTransportRequest请求,调用SearchQueryThenFetchAsyncAction#sendExecuteFirstPhase发送QUERY_ACTION_NAME的action请求,进行分片查询
        - 分片客户端--前面提到过,对于分片客户端而言,通过QUERY_ACTION_NAME找到其处理器回调SearchService#executeQueryPhase在客户端进行分片查询,首先通过createAndPutContext创建SearchContext,这里比较重要的是创建的时候调用SearchService#parseSource,进而调用QueryShardContext#toQuery回调AbstractQueryBuilder#toQuery/QueryStringQueryBuilder#doToQuery完成将QueryBuilder转换为Query的过程(这里是利用了MapperQueryParser进行解析,详情参见上面引用博客)
        - 分片客户端--调用loadOrExecuteQueryPhase后调用QueryPhase#execute,通过searcher.search(query, collector)执行查询,返回结果集
        - 当分片客户端成功返回结果后调用AbstractSearchAsyncAction#onFirstPhaseResult处理结果集,当所有分片都返回数据后,转向下一阶段innerMoveToSecondPhase,回调SearchQueryThenFetchAsyncAction#moveToSecondPhase,遍历需要返回的docIdsToLoad,通过executeFetch发送sendExecuteFetch请求给id所在的节点获取文档数据
        - 分片客户端--通过FETCH_ID_ACTION_NAME转向SearchService#executeFetchPhase,先找到SearchContext,再通过FetchPhase#execute,最终通过FetchPhase#loadStoredFields的readerContext.reader().document(docId, fieldVisitor)获取文档信息返回
    


        
