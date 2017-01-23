##2016书单总结--写给大忙人看的JavaSE8-Stream
   Stream是处理集合的关键抽象
 A sequence of elements supporting sequential and parallel aggregate operations.
 
1. Stream自己不会存储元素
    * 元素存储在底层集合或者根据需求产生出来
2. Stream操作符不会改变源对象
    * 返回持有新结果的Stream对象
3. Stream操作可延迟执行
4. 使用Stream通过三阶段构建流水线操作
    * 创建一个Stream
    * 将Stream进行转换
        * 流转换是指从一个流中读取数据，并将转换后的数据写入到另一个流中
    * 用终止操作符产生结果
5. Optional<T>---->对象或者是对一个T类型对象的封装
6. 收集结果---->collect
    * 一个能够创建目标类型实例的方法
    * 一个能够将元素添加到目标中的方法
    * 一个将两个对象整合到一起的方法
7. 函数式接口
    ![这里写图片描述](http://img.blog.csdn.net/20170123165853845?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvdW5kZXJncm93dGg=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)
8. 示例代码位于-- https://github.com/undergrowthlinear/2016MyBookSummary.git
    * com.undergrowth.java8.forimpatient.test.StreamLearnTest有关于创建Stream、转换Stream、
   接收结果的代码示例
   * 建议阅读java.util.stream.Stream源码以及相关的FunctionalInterface，还有java.util.stream.Collectors