package com.undergrowth.oom.opti;

/**
 * java.lang.OutOfMemoryError: Java heap space oom 当未写equals的时候 equals不使用id进行相等进行判断 导致数据量不断增加 内存泄露 -Xmx12m
 *
 * @author zhangwu
 * @version 1.0.0
 * @date 2018-06-28-15:48
 */

import java.util.HashMap;
import java.util.Map;

public class KeylessEntry {

    static class Key {
        Integer id;

        Key(Integer id) {
            this.id = id;
        }

        @Override
        public int hashCode() {
            return id.hashCode();
        }

        /*@Override
        public boolean equals(Object o) {
            boolean response = false;
            if (o instanceof Key) {
                response = (((Key)o).id).equals(this.id);
            }
            return response;
        }*/
    }

    public static void main(String[] args) {
        Map m = new HashMap();
        while (true) {
            for (int i = 0; i < 10000; i++) {
                if (!m.containsKey(new Key(i))) {
                    m.put(new Key(i), "Number:" + i);
                }
            }
            System.out.println("m.size()=" + m.size());
        }
    }
}