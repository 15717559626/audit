package com.snow.audit.singleton;

/**
 * @Description
 * @Author lisonghua
 * @Date 2023/12/6 10:33
 **/
public enum SingletonDemo {

    INSTANCE;

    public String message(){
        return "我是单例对象的一个方法";
    }

    private String message2(){
        return "我是单例对象的二个方法";
    }
}
