package com.undergrowth.cache;

import com.google.common.cache.LoadingCache;
import java.util.concurrent.ExecutionException;

/**
 * guava cache测试
 *
 * @author zhangwu
 * @version 1.0.0
 * @date 2018-05-21-14:58
 */
public class GuavaCacheTest {

    public static void main(String[] args) throws ExecutionException {
        GuavaCache guavaCache = new GuavaCache();
        LoadingCache<Integer, Product> cache = guavaCache.getCacheBuilder();
        /*Product product = cache.get(1, new Callable<Product>() {
            @Override
            public Product call() throws Exception {
                System.out.println("load from db");
                return new Product(1, "1 number");
            }
        });*/
        Product product1 = cache.get(1);
        Product product2 = cache.get(2);
        Product product3 = cache.get(3);
        System.out.println(product1 + "---->" + product2 + "---->" + product3);
        //System.out.println("---->" + product1);
        System.out.println(cache.getIfPresent(1) + "---->" + cache.getIfPresent(2) + "---->" + cache.getIfPresent(3));
    }

}