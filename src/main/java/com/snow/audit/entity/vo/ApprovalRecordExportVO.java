package com.snow.audit.entity.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
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
    @ColumnWidth(12)
    private String applyTime;

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
    //@DateTimeFormat("yyyy-MM-dd HH:mm:ss")
    @ColumnWidth(20)
    private String approveTime;


    // 构造函数 - 从原VO转换
    public ApprovalRecordExportVO(ApprovalRecordVO vo) {
        this.applicantName = vo.getApplicantName();
        this.applyTime = vo.getApplyTime();
        this.days = vo.getDays();
        this.approverName = vo.getApproverName();
        this.result = vo.getResult();
        this.comment = vo.getComment();
        this.approveTime = vo.getApproveTime();
    }

    // 默认构造函数
    public ApprovalRecordExportVO() {}
}
