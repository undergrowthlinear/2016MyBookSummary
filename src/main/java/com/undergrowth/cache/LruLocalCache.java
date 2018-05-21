package com.undergrowth.cache;

import java.util.LinkedHashMap;
import java.util.Map.Entry;

/**
 * 最少是用缓存,非线程安全
 *
 * @author zhangwu
 * @version 1.0.0
 * @date 2018-05-21-11:16
 */
public class LruLocalCache extends LinkedHashMap {

    private int maxCacheSize;

    public LruLocalCache(int maxCacheSize) {
        super(maxCacheSize, 0.75f, true);
        this.maxCacheSize = maxCacheSize;
    }

    @Override
    protected boolean removeEldestEntry(Entry eldest) {
        return this.size() >= maxCacheSize + 1;
    }
}