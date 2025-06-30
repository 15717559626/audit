package com.snow.audit.service.Impl;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.binarywang.wx.miniapp.bean.WxMaSubscribeMessage;
import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.snow.audit.common.ApiException;
import com.snow.audit.entity.Approval;
import com.snow.audit.entity.ApprovalRecord;
import com.snow.audit.entity.BuUserEntity;
import com.snow.audit.entity.Leave;
import com.snow.audit.entity.enums.ApprovalStatusEnum;
import com.snow.audit.entity.vo.AuditUserVO;
import com.snow.audit.entity.vo.WxLoginVO;
import com.snow.audit.mapper.BuUserMapper;
import com.snow.audit.service.IBuUserService;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 微信用户类 BuUserService
 *
 **/
@Service
@Slf4j
public class IBuUserServiceImpl extends ServiceImpl<BuUserMapper, BuUserEntity> implements IBuUserService {

    @Autowired
    private WxMaService wxMaService;

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final DateTimeFormatter DATE_TIME_FORMATTER_SHORT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    @Override
    public WxLoginVO wxLogin(String code) {
        log.info("微信登录开始");
        WxMaJscode2SessionResult result;
        try {
            result = wxMaService.jsCode2SessionInfo(code);
        } catch (WxErrorException e) {
            log.error("wxLogin:eshopLogin:通过微信获取openID异常", e);
            throw new ApiException(e.getMessage());
        }

        BuUserEntity buUser = this.lambdaQuery().eq(BuUserEntity::getOpenid, result.getOpenid()).one();
        if (buUser == null) {
            buUser = BuUserEntity.buildBuUserEntity(result.getOpenid());
            this.save(buUser);
        } else {
            buUser.setUserLastLoginTime(LocalDateTime.now());
            this.updateById(buUser);
        }
        return BeanUtil.copyProperties(buUser, WxLoginVO.class);
    }

    @Override
    public WxLoginVO getWxLoginVO(String openid) {
        BuUserEntity buUser = this.lambdaQuery().eq(BuUserEntity::getOpenid, openid).one();
        return BeanUtil.copyProperties(buUser, WxLoginVO.class);
    }

    @Override
    public List<AuditUserVO> getAuditUserList() {
        return this.lambdaQuery()
                .eq(BuUserEntity::getAuditFlag, 1)
                .list()
                .stream()
                .map(this::convertToAuditUserVO)
                .collect(Collectors.toList());
    }

    @Override
    public void sendSubscribeMessage(Long userId, String templateId, String page, ApprovalRecord record) {
        try {
            BuUserEntity userEntity = this.getById(userId);
            if (userEntity == null){
                return;
            }
            WxMaSubscribeMessage wxMssVo = new WxMaSubscribeMessage();
            wxMssVo.setToUser(userEntity.getOpenid());//用户的openid
            wxMssVo.setTemplateId(templateId);//订阅消息模板id
            wxMssVo.setPage(page);
            List<WxMaSubscribeMessage.MsgData> dataList = new ArrayList<>();
            //先暂时写死模板，后续根据模板动态拼接key
            WxMaSubscribeMessage.MsgData dataOne = new WxMaSubscribeMessage.MsgData("phrase2", ApprovalStatusEnum.fromCodeForWxMessage(record.getResult()));
            WxMaSubscribeMessage.MsgData dataTwo = new WxMaSubscribeMessage.MsgData("time3", record.getApproveTime().format(DATE_TIME_FORMATTER));
            WxMaSubscribeMessage.MsgData dataThree = new WxMaSubscribeMessage.MsgData("thing6", StringUtils.isNotBlank(record.getComment())?record.getComment():ApprovalStatusEnum.fromCodeForWxMessage(record.getResult()));
            WxMaSubscribeMessage.MsgData dataFour = new WxMaSubscribeMessage.MsgData("thing4", "审批人: " + record.getApproverName());
            dataList.add(dataOne);
            dataList.add(dataTwo);
            dataList.add(dataThree);
            dataList.add(dataFour);
            wxMssVo.setData(dataList);
            log.info("审核结果推送用户:{},请求参数:{}", userEntity.getOpenid(), JSONObject.toJSONString(wxMssVo));
            wxMaService.getSubscribeService().sendSubscribeMsg(wxMssVo);
        } catch (WxErrorException e) {
            log.error("审核结果推送失败,{}", e.getError().getErrorMsg());
        }
    }

    @Override
    public void sendSubscribeMessage(Long userId, String templateId, String page, Leave leave) {
        try {
            BuUserEntity userEntity = this.getById(userId);
            if (userEntity == null){
                return;
            }
            WxMaSubscribeMessage wxMssVo = new WxMaSubscribeMessage();
            wxMssVo.setToUser(userEntity.getOpenid());//用户的openid
            wxMssVo.setTemplateId(templateId);//订阅消息模板id
            wxMssVo.setPage(page);
            List<WxMaSubscribeMessage.MsgData> dataList = new ArrayList<>();
            //先暂时写死模板，后续根据模板动态拼接key
            WxMaSubscribeMessage.MsgData dataOne = new WxMaSubscribeMessage.MsgData("thing1", leave.getApplicantName());
            WxMaSubscribeMessage.MsgData dataTwo = new WxMaSubscribeMessage.MsgData("time2", leave.getCreateTime().format(DATE_TIME_FORMATTER));
            WxMaSubscribeMessage.MsgData dataThree = new WxMaSubscribeMessage.MsgData("thing3", leave.getReason());
            WxMaSubscribeMessage.MsgData dataFour = new WxMaSubscribeMessage.MsgData("time4", leave.getStartDate().format(DATE_TIME_FORMATTER_SHORT));
            WxMaSubscribeMessage.MsgData dataFive = new WxMaSubscribeMessage.MsgData("time5", leave.getEndDate().format(DATE_TIME_FORMATTER_SHORT));
            dataList.add(dataOne);
            dataList.add(dataTwo);
            dataList.add(dataThree);
            dataList.add(dataFour);
            dataList.add(dataFive);
            wxMssVo.setData(dataList);
            log.info("待审批消息推送用户:{},请求参数:{}", userEntity.getOpenid(), JSONObject.toJSONString(wxMssVo));
            wxMaService.getSubscribeService().sendSubscribeMsg(wxMssVo);
        } catch (WxErrorException e) {
            log.error("待审批消息推送失败,{}", e.getError().getErrorMsg());
        }
    }

    private AuditUserVO convertToAuditUserVO(BuUserEntity buUserEntity) {
        AuditUserVO auditUserVO = new AuditUserVO();
        auditUserVO.setId(buUserEntity.getUserId());
        auditUserVO.setName(buUserEntity.getName());
        auditUserVO.setDepartment(buUserEntity.getDepartment());
        auditUserVO.setPosition(buUserEntity.getPosition());
        return auditUserVO;
    }

}
