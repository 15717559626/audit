package com.snow.audit.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("t_bu_user")
public class BuUserEntity implements Serializable {

	@TableId(type = IdType.ASSIGN_ID)
	@ApiModelProperty(value = "用户ID")
	private Long userId;

	@TableField(value = "openid")
	@ApiModelProperty(value = "用户的OpenID")
	private String openid;

	@TableField(value = "mp_openid")
	@ApiModelProperty(value = "用户的mpOpenID")
	private String mpOpenid;

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

	@ApiModelProperty(value = "用户权限")
	private String permissions;

	@ApiModelProperty(value = "头像URL")
	private String avatar;

	@ApiModelProperty(value = "审批人标识")
	private Integer auditFlag;

	public static BuUserEntity buildBuUserEntity(String openid) {
		BuUserEntity buUserEntity = new BuUserEntity();
		buUserEntity.setOpenid(openid);
		buUserEntity.setUserFirstLoginTime(LocalDateTime.now());
		buUserEntity.setUserLastLoginTime(LocalDateTime.now());
		return buUserEntity;
	}

}