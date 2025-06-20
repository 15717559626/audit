package com.snow.audit.config;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.api.impl.WxMaServiceImpl;
import cn.binarywang.wx.miniapp.config.impl.WxMaDefaultConfigImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author lish
 * @Date 2025/6/17 17:15
 * @DESCRIBE
 */
@Configuration
public class WxConfiguration {

    @Bean
    public WxMaService wxMaService() {
        WxMaServiceImpl service = new WxMaServiceImpl();
        WxMaDefaultConfigImpl config = new WxMaDefaultConfigImpl();
        config.setAppid("wx017f711dd19ba162");
        config.setSecret("ff3e00fefb17bcf2e5eddec3630dc887");
        service.setWxMaConfig(config);
        return service;
    }

}
