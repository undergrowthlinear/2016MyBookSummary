package com.undergrowth.oom.opti;

/**
 * exceeds
 * java.lang.OutOfMemoryError: Requested array size exceeds VM limit`
 * @author zhangwu
 * @version 1.0.0
 * @date 2018-06-28-18:04
 */
public class ExceedsArray {
    public static void main(String[] args) throws Exception {
        for (int i = 3; i >= 0; i--) {
            try {
                int[] arr = new int[Integer.MAX_VALUE - i];
                System.out.format("Successfully initialized an array with %,d elements.\n", Integer.MAX_VALUE - i);
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }

    }
}