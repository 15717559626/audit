package com.snow.audit.factory;

/**
 * @Description
 * @Author lisonghua
 * @Date 2023/12/6 10:17
 **/
public class KfChicken implements Restaurant{
    @Override
    public String breakfast() {
        return "油条";
    }

    @Override
    public String lunch() {
        return "鸡翅";
    }

    @Override
    public String dinner() {
        return "鸡腿";
    }
}
