package com.undergrowth.cache;

/**
 * 产品bean
 *
 * @author zhangwu
 * @version 1.0.0
 * @date 2018-05-21-11:52
 */
public class Product {
    private Integer pId;
    private String name;

    public Product(Integer pId, String name) {
        this.pId = pId;
        this.name = name;
    }

    public Integer getpId() {
        return pId;
    }

    public void setpId(Integer pId) {
        this.pId = pId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Product{" +
            "pId=" + pId +
            ", name='" + name + '\'' +
            '}';
    }
}