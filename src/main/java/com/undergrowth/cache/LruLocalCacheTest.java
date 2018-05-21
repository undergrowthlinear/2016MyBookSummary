package com.undergrowth.cache;

/**
 * 最少使用测试
 *
 * @author zhangwu
 * @version 1.0.0
 * @date 2018-05-21-11:20
 */
public class LruLocalCacheTest {

    public static void main(String[] args) {
        int num = 5;
        LruLocalCache lruLocalCache = new LruLocalCache(5);
        for (int i = 0; i < num; i++
            ) {
            lruLocalCache.put(i, i);
        }
        System.out.println(lruLocalCache);
        System.out.println("第二个元素---->" + lruLocalCache.get(1));
        lruLocalCache.put(num + 1, num + 1);
        System.out.println(lruLocalCache);
        lruLocalCache.put(num + 2, num + 2); //第三个元素移除
        System.out.println(lruLocalCache);
    }

}