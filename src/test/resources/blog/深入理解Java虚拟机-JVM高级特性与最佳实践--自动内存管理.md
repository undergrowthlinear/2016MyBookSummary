##深入理解Java虚拟机-JVM高级特性与最佳实践--自动内存管理
Java区别于C++在于其自动内存管理，主要从5个方面介绍自动内存管理

1. 内存区域
    JVM可运行的内存区域主要由5部分组成，程序计数器、虚拟机栈、本地方法栈、Java堆、方法区
程序计数器与本地方法栈
    ![这里写图片描述](http://img.blog.csdn.net/20170108001859448?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvdW5kZXJncm93dGg=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)
    虚拟机栈
  ![这里写图片描述](http://img.blog.csdn.net/20170108001941728?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvdW5kZXJncm93dGg=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)
方法区
![这里写图片描述](http://img.blog.csdn.net/20170108002004962?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvdW5kZXJncm93dGg=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)
Java堆
这里还提到了直接内存，在NIO中会用到直接内存，类似于C++的内存分配，不过其分配的内存不受JVM控制
![这里写图片描述](http://img.blog.csdn.net/20170108002033591?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvdW5kZXJncm93dGg=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)
2. 内存溢出
    程序计数器、虚拟机栈、本地方法栈、Java堆、方法区5个部分输了程序计数器没有内存溢出外，其他区域均有
    内存溢出，同时也有参数进行调节
    虚拟机栈-->-Xss
    本地方法栈-->
    方法区中的永久区-->-XPermSize
    Java堆-->-Xms -Xmx
3. 内存分配
    几个准则如下;
    ![这里写图片描述](http://img.blog.csdn.net/20170108002204619?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvdW5kZXJncm93dGg=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)
4. 内存回收
    由7个垃圾收集器组成，分别为年轻代采用的复制收集算法，年老代采用标记-清除算法，分别功能如下图：
    ![这里写图片描述](http://img.blog.csdn.net/20170108002221515?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvdW5kZXJncm93dGg=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)
5. 内存模型
    由工作内存与主内存组成，工作内存与主内存之间通过LOADy与SAVE指令交换值，示意图如下：
    ![这里写图片描述](http://img.blog.csdn.net/20170108002239464?watermark/2/text/aHR0cDovL2Jsb2cuY3Nkbi5uZXQvdW5kZXJncm93dGg=/font/5a6L5L2T/fontsize/400/fill/I0JBQkFCMA==/dissolve/70/gravity/SouthEast)