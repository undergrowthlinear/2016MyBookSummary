package com.undergrowth;

/**
 * @author zhangwu
 * @version 1.0.0
 * @date 2019-02-18-17:56
 */
public class GcTest {

    // -XX:+PrintGCDetails
    public static void main(String[] args) {
        byte[] allocation1, allocation2;
        allocation1 = new byte[32900*1024];
        allocation2 = new byte[900*1024];
    }


}