package com.snow.audit.entity.enums;

import lombok.Getter;

/**
 * @Author lish
 * @Date 2025/6/24 15:41
 * @DESCRIBE
 */
@Getter
public enum ApprovalStatus {
    SUBMITTED("submitted", "已提交"),
    APPROVING("approving", "审批中"),
    APPROVED("approved", "已通过"),
    REJECTED("rejected", "已拒绝"),
    CANCELLED("cancelled", "已撤销");

    private final String code;
    private final String description;

    ApprovalStatus(String code, String description) {
        this.code = code;
        this.description = description;
    }

    // 根据code获取枚举
    public static String fromCode(String code) {
        for (ApprovalStatus status : ApprovalStatus.values()) {
            if (status.code.equals(code)) {
                return status.getDescription();
            }
        }
        return "";
    }

    @Override
    public String toString() {
        return code + " (" + description + ")";
    }
}
