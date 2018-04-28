# hive 2.3.2 Driver简析
## 概述
- 参考
- https://segmentfault.com/a/1190000002766035
- https://blog.csdn.net/wzq6578702/article/details/71250081
- http://www.cnblogs.com/niceofday/p/6418803.html
## 代码流
### 第一步:Cli解析用户输入
```
org.apache.hadoop.hive.cli.CliDriver#main----入口
org.apache.hadoop.hive.cli.CliDriver#executeDriver----处理用户的输入(查询语句)
org.apache.hadoop.hive.cli.CliDriver#processFile
org.apache.hadoop.hive.cli.CliDriver#processReader
org.apache.hadoop.hive.cli.CliDriver#processLine
org.apache.hadoop.hive.cli.CliDriver#processCmd----开始解析命令
org.apache.hadoop.hive.cli.CliDriver#processLocalCmd----local解析
    Driver qp = (Driver) proc----启动驱动器进行编译/执行
    ret = qp.run(cmd).getResponseCode()
```
### 第二步:驱动器进行编译/执行
```
org.apache.hadoop.hive.ql.Driver#run----主要分为compile/execute
org.apache.hadoop.hive.ql.Driver#runInternal
org.apache.hadoop.hive.ql.Driver#compileInternal
    org.apache.hadoop.hive.ql.Driver#compile----编译(包含词法解析/语法解析/生成逻辑执行计划/利用优化器优化/生成物理计划)
    ASTNode tree = ParseUtils.parse(command, ctx)----词法分析,生成AST树
    org.apache.hadoop.hive.ql.parse.BaseSemanticAnalyzer#analyze
    org.apache.hadoop.hive.ql.parse.SemanticAnalyzer#analyzeInternal
    org.apache.hadoop.hive.ql.parse.SemanticAnalyzer#analyzeInternal----核心操作都在这里
        Operator sinkOp = genOPTree(ast, plannerCtx)
        pCtx = optm.optimize()----优化器优化
    org.apache.hadoop.hive.ql.parse.TaskCompiler#compile----转换为task
plan = new QueryPlan----物理计划
    org.apache.hadoop.hive.ql.parse.SemanticAnalyzer#getAllRootTasks
org.apache.hadoop.hive.ql.Driver#execute----执行
org.apache.hadoop.hive.ql.Driver#launchTask
```
### 第三步:显示结果
```
org.apache.hadoop.hive.ql.Driver#getResults----根据查询的结果
```
## 示例配置