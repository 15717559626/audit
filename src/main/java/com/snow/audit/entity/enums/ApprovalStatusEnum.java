package com.snow.audit.entity.enums;

import lombok.Getter;

/**
 * @Author lish
 * @Date 2025/6/24 15:41
 * @DESCRIBE
 */
@Getter
public enum ApprovalStatusEnum {
    APPROVING("approving", "审核中"),
    APPROVED("approved", "已通过"),
    REJECTED("rejected", "已拒绝");
    private final String code;
    private final String description;

    ApprovalStatusEnum(String code, String description) {
        this.code = code;
        this.description = description;
    }

    // 根据code获取枚举
    public static String fromCode(String code) {
        for (ApprovalStatusEnum status : ApprovalStatusEnum.values()) {
            if (status.code.equals(code)) {
                return status.getDescription();
            }
        }
        return "";
    }

    public static String fromCodeForWxMessage(String code) {
        for (ApprovalStatusEnum status : ApprovalStatusEnum.values()) {
            if (APPROVED.code.equals(code)) {
                return "审核通过";
            }
            if (REJECTED.code.equals(code)) {
                return "审核失败";
            }
        }
        return "";
    }

    @Override
    public String toString() {
        return code + " (" + description + ")";
    }
}
