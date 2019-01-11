package com.undergrowth.buddy.bean;

import java.util.Arrays;
import java.util.List;

/**
 * @author zhangwu
 * @version 1.0.0
 * @date 2019-01-11-16:06
 */
public class MemoryDatabase {
    public List<String> load(String info) {
        return Arrays.asList(info + ": foo", info + ": bar");
    }
}