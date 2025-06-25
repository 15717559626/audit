package com.snow.audit.entity.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.snow.audit.entity.enums.ApprovalStatusEnum;
import com.snow.audit.entity.enums.LeaveStatusEnum;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @Author lish
 * @Date 2025/6/24 9:47
 * @DESCRIBE
 */
@Data
public class ApprovalRecordExportVO {

    @ExcelProperty(value = "请假人姓名", index = 0)
    @ColumnWidth(12)
    private String applicantName;

    @ExcelProperty(value = "请假时间", index = 1)
    @ColumnWidth(20)
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    private LocalDateTime applyTime;

    @ExcelProperty(value = "请假时长", index = 2)
    @ColumnWidth(12)
    private String days;

    @ExcelProperty(value = "审批人姓名", index = 3)
    @ColumnWidth(15)
    private String approverName;

    @ExcelProperty(value = "审批结果", index = 4)
    @ColumnWidth(12)
    private String result;

    @ExcelProperty(value = "审批意见", index = 5)
    @ColumnWidth(30)
    private String comment;

    @ExcelProperty(value = "审批时间", index = 6)
    @DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    @ColumnWidth(20)
    private LocalDateTime approveTime;


    // 构造函数 - 从原VO转换
    public ApprovalRecordExportVO(ApprovalRecordVO vo) {
        this.applicantName = vo.getApplicantName() != null ? vo.getApplicantName() : "";
        this.applyTime = vo.getApplyTime();
        this.days = vo.getDays() != null ? vo.getDays() : "";
        this.approverName = vo.getApproverName() != null ? vo.getApproverName() : "";
        this.result = ApprovalStatusEnum.fromCode(vo.getResult());
        this.comment = vo.getComment() != null ? vo.getComment() : "";
        this.approveTime = vo.getApproveTime();
    }

    // 默认构造函数
    public ApprovalRecordExportVO() {}
}
