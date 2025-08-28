package com.snow.audit.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.snow.audit.entity.Approval;
import com.snow.audit.entity.ApprovalRecord;
import com.snow.audit.entity.BuUserEntity;
import com.snow.audit.entity.Leave;
import com.snow.audit.entity.vo.AuditUserVO;
import com.snow.audit.entity.vo.WxLoginVO;

import java.util.List;

/**
 * @Author lish
 * @Date 2025/6/17 17:04
 * @DESCRIBE
 */
public interface IBuUserService extends IService<BuUserEntity> {

    /**
     * 微信登录
     */
    WxLoginVO wxLogin(String code);

    /**
     * 根据openId获取用户信息
     */
    WxLoginVO getWxLoginVO(String openid);

    /**
     * 根据审批下拉用户
     */
    List<AuditUserVO> getAuditUserList();

    /**
     * 发送审批结果给指定用户
     */
    void sendSubscribeMessage(Long userId, String templateId, String page, ApprovalRecord record);

    /**
     * 发送待审批消息提醒用户
     */
    void sendSubscribeMessage(Long userId, String templateId, String page, Leave leave);

    /**
     * 服务号发送待审批消息提醒用户
     */
    void sendMpTemplateMessage(Long userId, String templateId, String page, Leave leave);

}
