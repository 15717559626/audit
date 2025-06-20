package com.snow.audit.entity.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author lish
 * @Date 2025/6/17 16:43
 * @DESCRIBE
 */
@Data
public class WxLoginPram {

    @ApiModelProperty("登录code")
    private String code;

    @ApiModelProperty("openid")
    private String openid;

}
