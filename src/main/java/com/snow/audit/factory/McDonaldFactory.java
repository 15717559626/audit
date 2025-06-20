package com.snow.audit.factory;

/**
 * @Description
 * @Author lisonghua
 * @Date 2023/12/6 11:24
 **/
public class McDonaldFactory implements RestaurantFactory{

    @Override
    public Restaurant getRestaurant() {
        return new McDonald();
    }
}
