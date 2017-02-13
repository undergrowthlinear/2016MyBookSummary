##2016书单总结--Lucene实战(第二版)--基础篇
    Lucene实战基于Lucene3.0,本示例以3.5为基础
    Lucene由道格.卡丁编写的用于文本索引与搜索的高性能、可扩展的信息检索工具库
    
1. 通过5个部分进行总结，分别为
    - 收集--包括如何获取文本，例如使用tika提取文本
    - 分析--分析器的原理，词汇单元过程化，词汇单元过滤器
    - 索引--如何构建倒排索引，构建实时索引，更新、删除索引
    - 搜索--查找需要信息，按照不同条件进行搜索
    - 高级应用--其他语言、案例分析、调优
2. 索引
    - 常见概念
        * 文档(Document)--包含一个或者多个域的容器
        * 域(Field)--是否可以索引(Index)、是否可以存储(Store)、是否可以分词(Token)
        * 索引段--Lucene索引包含一个或者多个段
    - 常见构建索引步骤
        * 1.创建Directory
        * 2.创建IndexWriter 
        * 3.创建Document 
        * 4.为Document创建Field
        * 5.使用IndexWriter将Document写入索引 
        * 6.关闭InderWriter
    - 构建实时索引
       * 将IndexWriter委托给NRTManager进行索引的添加、删除、修改 
3. 搜索
    - 常见概念
        * 加权基准(norms)--Score(norms)=文档加权*域加权*VSP的tf*idf*lengthNorm
    - 常见构建搜索步骤
        * 1.创建Directory
        * 2.创建IndexReader
        * 3.使用IndexReader创建IndexSearcher
        * 4.创建Query 
        * 5.使用IndexSearcher搜索Query,返回TopDocs
        * 6.显示TopDocs的ScoreDoc
        * 7.通过IndexReader/docId获取Document 
        * 8.通过Document的get获取字段 
        * 9.关闭IndexReader
    - 构建实时搜索
        * 将InderSearcher的获取、注销委托给SearcherManager进行管理
4. 分析
    * 分析器--将域(Field)转为项(Term)的过程
    * 分析器由词汇单元过程化(Tokenization)和词汇单元过滤器组成
5. 收集
6. 示例代码位于-- https://github.com/undergrowthlinear/2016MyBookSummary.git
    * com.undergrowth.lucene.in.action.LuceneIndexQueryTest演示了如何构建索引、同时展示了布尔搜索、
    查询解析搜索、范围搜索、词语搜索、模糊搜索、通配符搜索、前缀搜索
    * com.undergrowth.lucene.in.action.IndexSearcherManagerToolTest演示近实时搜索的修改、删除索引、以及搜索
    * com.undergrowth.lucene.in.action.AnalyzerToolTest演示分析器中对应的词汇单元属性
    * com.undergrowth.lucene.in.action.TikaParserTest演示使用tika进行解析各种文档