package com.undergrowth.interview;

/**
 * @author zhangwu
 * @version 1.0.0
 * @description 验证String的内存使用
 * @date 2017-02-09-14:28
 */
public class InternMain {

    public static void main(String[] args) {
        String s1 = new String("iop");
        String s2 = s1 + "jjhhh";
        System.out.println(s2);
    }

}
