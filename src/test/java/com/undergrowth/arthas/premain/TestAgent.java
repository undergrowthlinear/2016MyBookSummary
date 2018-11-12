package com.undergrowth.arthas.premain;

/**
 * @author zhangwu
 * @version 1.0.0
 * @date 2018-11-12-17:04
 */
public class TestAgent {
    public static void main(String[] args) {
        TestAgent ta = new TestAgent();
        ta.test();
    }

    public void test() {
        System.out.println("I'm TestAgent");
    }

}