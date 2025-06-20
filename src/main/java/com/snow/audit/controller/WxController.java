package com.snow.audit.controller;

import com.snow.audit.common.ApiResponse;
import com.snow.audit.entity.param.WxLoginPram;
import com.snow.audit.entity.vo.AuditUserVO;
import com.snow.audit.entity.vo.WxLoginVO;
import com.snow.audit.service.IBuUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author lish
 * @Date 2025/6/17 16:38
 * @DESCRIBE
 */
@Api(tags = "xDms接口")
@Slf4j
@RestController
@RequestMapping("/api/wx")
public class WxController {

    @Resource
    private IBuUserService userService;

    @ApiOperation("小程序用户登录")
    @PostMapping("/wechat-login")
    public ApiResponse<WxLoginVO> wxLogin(@RequestBody WxLoginPram wxLoginPram){
        return ApiResponse.success(userService.wxLogin(wxLoginPram.getCode()));
    }

    @ApiOperation("获取小程序用户信息")
    @PostMapping("/getUserInfo")
    public ApiResponse<WxLoginVO> getUserInfo(@RequestBody WxLoginPram wxLoginPram){
        return ApiResponse.success(userService.getWxLoginVO(wxLoginPram.getOpenid()));
    }

    @ApiOperation("获取审批人列表")
    @GetMapping("/getUserList")
    public ApiResponse<List<AuditUserVO>> getUserList(){
        return ApiResponse.success(userService.getAuditUserList());
    }
}
