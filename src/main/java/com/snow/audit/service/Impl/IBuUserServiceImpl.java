package com.snow.audit.service.Impl;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.bean.WxMaJscode2SessionResult;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.snow.audit.common.ApiException;
import com.snow.audit.entity.BuUserEntity;
import com.snow.audit.entity.vo.AuditUserVO;
import com.snow.audit.entity.vo.WxLoginVO;
import com.snow.audit.mapper.BuUserMapper;
import com.snow.audit.service.IBuUserService;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.error.WxErrorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 微信用户类 BuUserService
 *
 * @author fuce_自动生成
 * @Title: BuUserService.java 
 * @Package com.fc.v2.service 
 * @email ${email}
 * @date 2025-06-17 17:00:58  
 **/
@Service
@Slf4j
public class IBuUserServiceImpl extends ServiceImpl<BuUserMapper, BuUserEntity> implements IBuUserService {

    @Autowired
    private WxMaService wxMaService;

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
        if (buUser == null){
            buUser = BuUserEntity.buildBuUserEntity( result.getOpenid());
            this.save(buUser);
        }else {
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

    private AuditUserVO convertToAuditUserVO(BuUserEntity buUserEntity) {
        AuditUserVO auditUserVO = new AuditUserVO();
        auditUserVO.setId(buUserEntity.getUserId());
        auditUserVO.setName(buUserEntity.getName());
        auditUserVO.setDepartment(buUserEntity.getDepartment());
        auditUserVO.setPosition(buUserEntity.getPosition());
        return auditUserVO;
    }

}
