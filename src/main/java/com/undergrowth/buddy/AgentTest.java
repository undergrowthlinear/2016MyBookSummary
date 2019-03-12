package com.undergrowth.buddy;

/**
 * 来源 https://blog.csdn.net/f59130/article/details/78494572
 *
 * @author zhangwu
 * @version 1.0.0
 * @date 2019-01-10-17:30
 */
public class AgentTest {
    private void fun1() throws Exception {
        System.out.println("this is fun 1.");
        Thread.sleep(500);
    }

    private void fun2() throws Exception {
        System.out.println("this is fun 2.");
        Thread.sleep(500);
    }

    public static void main(String[] args) throws Exception {
        AgentTest test = new AgentTest();
        test.fun1();
        test.fun2();

    }
}