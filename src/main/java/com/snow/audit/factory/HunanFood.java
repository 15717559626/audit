package com.snow.audit.factory;

/**
 * @Description
 * @Author lisonghua
 * @Date 2023/12/6 10:14
 **/
public class HunanFood implements Restaurant{
    @Override
    public String breakfast() {
        return "杀猪粉";
    }

    @Override
    public String lunch() {
        return "辣椒炒肉";
    }

    @Override
    public String dinner() {
        return "猪三样";
    }
}
