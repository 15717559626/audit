package com.snow.audit.factory;

/**
 * @Description
 * @Author lisonghua
 * @Date 2023/12/6 10:16
 **/
public class McDonald implements Restaurant{
    @Override
    public String breakfast() {
        return "麦满分";
    }

    @Override
    public String lunch() {
        return "薯条";
    }

    @Override
    public String dinner() {
        return "汉堡";
    }
}
