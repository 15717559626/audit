package com.snow.audit.config;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.api.impl.WxMaServiceImpl;
import cn.binarywang.wx.miniapp.config.impl.WxMaDefaultConfigImpl;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.config.WxMpConfigStorage;
import me.chanjar.weixin.mp.config.impl.WxMpDefaultConfigImpl;
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

    @Bean
    public WxMpService wxMpService() {
        WxMpService service = new WxMpServiceImpl();
        WxMpDefaultConfigImpl config = new WxMpDefaultConfigImpl();
        config.setAppId("wx7d2ddde3f94781a3");
        config.setSecret("b67fe0daa108522d120a0e72d21c339e");
        service.setWxMpConfigStorage(config);
        return service;
    }

}
