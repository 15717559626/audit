package com.snow.audit.entity.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.snow.audit.handler.StringToListTypeHandler;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class WxLoginVO {

    @ApiModelProperty(value = "用户ID")
    private Long userId;

    @TableField(value = "openid")
    @ApiModelProperty(value = "用户的OpenID")
    private String openid;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "首次登陆时间")
    private LocalDateTime userFirstLoginTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @ApiModelProperty(value = "最后登陆时间")
    private LocalDateTime userLastLoginTime;

    @ApiModelProperty(value = "手机号")
    private String phone;

    @ApiModelProperty(value = "用户名字")
    private String name;

    @ApiModelProperty(value = "所在部门")
    private String department;

    @ApiModelProperty(value = "职位")
    private String position;

    @ApiModelProperty(value = "用户令牌")
    private String token;

    @TableField(value = "string_field", typeHandler = StringToListTypeHandler.class)
    @ApiModelProperty(value = "用户权限")
    private List<String> permissions;

    @ApiModelProperty(value = "头像URL")
    private String avatar;

}
