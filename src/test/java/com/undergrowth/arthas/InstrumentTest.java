package com.undergrowth.arthas;

/**
 * https://blog.csdn.net/ykdsg/article/details/12080071#
 *
 * java Instrumentation指的是可以用独立于应用程序之外的代理（agent）程序来监测和协助运行在JVM上的应用程序。 这种监测和协助包括但不限于获取JVM运行时状态，替换和修改类定义等。
 *
 * Java SE5中使用JVM TI替代了JVM PI和JVM DI。提供一套代理机制，支持独立于JVM应用程序之外的程序以代理的方式连接和访问JVM。
 *
 * 开发者可以在一个普通 Java 程序（带有 main 函数的 Java 类）运行时， 通过 – javaagent 参数指定一个特定的 jar 文件（包含 Instrumentation 代理）来启动 Instrumentation 的代理程序。
 *
 *
 * premain(这个jar包的manifest文件中包含Premain-Class属性，并且改属性的值为代理类全路径名/ 代理类必须提供一个public static void premain(String args, Instrumentation inst)或 public static
 * void premain(String args) 方法。)
 *
 *
 * 1.agentmain(manifest中指定Agent-Class属性，值为代理类全路径/代理类需要提供public static void agentmain(String args, Instrumentation inst)或 public static void
 * agentmain(String args)方法。并且再二者同时存在时以前者优先。args和inst和premain中的一致。) 2.Attach API中的VirtualMachine代表一个运行中的VM。其提供了loadAgent()方法，可以在运行时动态加载一个代理jar
 */

public class InstrumentTest {
}
