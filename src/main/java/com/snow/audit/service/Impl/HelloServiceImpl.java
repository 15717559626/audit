package com.snow.audit.service.Impl;

import com.snow.audit.factory.*;
import com.snow.audit.service.HelloService;
import com.snow.audit.singleton.SingletonDemo;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * @Description
 * @Author lisonghua
 * @Date 2023/12/1 15:30
 **/
@Service
public class HelloServiceImpl implements HelloService {

    @Override
    public String hello(String name) {
        return Thread.currentThread() + LocalDateTime.now().toString() + name;
    }

    @Override
    public String eatFood(String type) {
        String msg = "";
        RestaurantFactory hunanFoodFactory = null;
        if ("kfc".equals(type)){
            hunanFoodFactory = new HunanFoodFactory();
        }else if ("mc".equals(type)){
            hunanFoodFactory = new McDonaldFactory();
        }else if ("hunan".equals(type)){
            hunanFoodFactory = new HunanFoodFactory();
        }
        Restaurant restaurant = hunanFoodFactory.getRestaurant();
        LocalTime currentTime = LocalTime.now();
        if (currentTime.isAfter(LocalTime.of(7, 0)) && currentTime.isBefore(LocalTime.of(12, 0))) {
            msg = ("当前时间是早上，应该吃 " + restaurant.breakfast());
        } else if (currentTime.isAfter(LocalTime.of(12, 0)) && currentTime.isBefore(LocalTime.of(17, 0))) {
            msg = ("当前时间是下午，应该吃 "+ restaurant.lunch());
        } else {
            msg = ("当前时间是晚上,应该是 "+ restaurant.dinner());
        }
        //System.out.println(msg);
        return msg;
    }

    @Override
    public String singletonTest(String type) {
        return SingletonDemo.INSTANCE.message();
    }

}
