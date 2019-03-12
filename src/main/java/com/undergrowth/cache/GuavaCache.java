package com.undergrowth.cache;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import java.util.concurrent.TimeUnit;

/**
 * guava缓存验证
 *
 * @author zhangwu
 * @version 1.0.0
 * @date 2018-05-21-11:38
 */
public class GuavaCache {

    private int maxSize = 2;

    private LoadingCache<Integer, Product> cacheBuilder = CacheBuilder.newBuilder().expireAfterAccess(1, TimeUnit.MINUTES).maximumSize(maxSize).build(
        new CacheLoader<Integer, Product>() {
            @Override
            public Product load(Integer key) throws Exception {
                System.out.println("from key:" + key);
                return new Product(key, String.valueOf(key) + Math.random());
            }
        });

    public int getMaxSize() {
        return maxSize;
    }

    public LoadingCache<Integer, Product> getCacheBuilder() {
        return cacheBuilder;
    }
}