package com.snow.audit.controller;

import cn.binarywang.wx.miniapp.api.WxMaService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @Description
 * @Author lisonghua
 * @Date 2023/12/1 15:29
 **/
@Api(tags = "测试模块")
@RestController
@Slf4j
public class HelloController {

    @Autowired
    private WxMaService wxMaService;


    @ApiOperation(value = "测试接口")
    @GetMapping("/hello")
    public String sayHello(@RequestParam("name") String name){
        return "hello";

    }

}
