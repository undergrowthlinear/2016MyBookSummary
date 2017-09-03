# junit 4.12 学习笔记
## 概述
- 参考
  - http://www.cnblogs.com/jinsdu/p/4646895.html
  - https://www.ibm.com/developerworks/cn/education/java/j-junit4/index.html
## Test---->测试
- TestCase---->Test
  - junit.framework.TestCase.runBare---->按顺序执行setUp、runTest、tearDown
    - junit.framework.TestCase.runTest---->最终通过反射调用
- TestSuite---->Test
  - TestSuite为Test的组合模式,支持运行多个测试案例
## Runner---->运行测试
- BlockJUnit4ClassRunner---->ParentRunner---->Runner
  - ParentRunner提供通用过滤/排序/子元素集合/注解信息解析等支持
  - BlockJUnit4ClassRunner为junit4运行的核心类,转换测试类的方法为一个个测试案例
- IgnoredClassRunner---->Runner
### RunnerBuilder---->运行生成器支持
- IgnoredBuilder---->RunnerBuilder
- JUnit4Builder---->RunnerBuilder
### JUnitCore---->运行支持
### TestRule---->运行测试规则支持,允许在测试时,添加更多的行为控制
- Timeout---->TestRule
  - org.junit.internal.runners.statements.FailOnTimeout.getResult---->通过FutureTask的超时等待完成超时Rule
## TestResult/TestFailure/Result/Assert---->测试结果/断言支持
- Assert---->一系列的断言方法支持
## 常用注解支持
- Test---->标记public void 的方法为测试案例,支持timeout/expected
- Rule---->支持方法/类上应用特定的规则
- Ignore---->在方法或者类上禁用测试
- RunWith---->使用指定的Runner执行测试用例
- Before/BeforeClass---->单个/所有测试案例前执行动作
- After/AfterClass---->单个/所有测试案例后执行动作
## 其他支持---->监听器
- TestListenerTest---->TestListener---->测试过程中的事件监听
## 测试
- 示例代码位于-- https://github.com/undergrowthlinear/2016MyBookSummary.git
- com.undergrowth.junit.TestAnnotation