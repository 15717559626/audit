package com.snow.audit.factory;

/**
 * @Description
 * @Author lisonghua
 * @Date 2023/12/6 10:44
 **/
public class HunanFoodFactory implements RestaurantFactory {

    @Override
    public Restaurant getRestaurant() {
        return new HunanFood();
    }
}
