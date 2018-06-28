package com.undergrowth.oom.opti;

/**
 * java.lang.OutOfMemoryError: Java heap space oom
 * 以下代码非常简单, 程序试图分配容量为 2M 的 int 数组. 如果指定启动参数 -Xmx12m, 那么就会发生 java.lang.OutOfMemoryError: Java heap space 错误。而只要将参数稍微修改一下, 变成 -Xmx13m, 错误就不再发生。
 * @author zhangwu
 * @version 1.0.0
 * @date 2018-06-28-15:45
 */
public class JvmHeapSpace {
    static final int SIZE = 2 * 1024 * 1024;

    public static void main(String[] a) {
        int[] i = new int[SIZE];
    }

}