package com.snow.audit.common;

import com.alibaba.excel.write.metadata.style.WriteCellStyle;
import com.alibaba.excel.write.metadata.style.WriteFont;
import com.alibaba.excel.write.style.HorizontalCellStyleStrategy;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;

/**
 * @Author lish
 * @Date 2025/6/24 9:51
 * @DESCRIBE
 */
public class CustomStyleHandler extends HorizontalCellStyleStrategy {

    public CustomStyleHandler() {
        super(createHeadWriteCellStyle(), createContentWriteCellStyle());
    }

    /**
     * 表头样式
     */
    private static WriteCellStyle createHeadWriteCellStyle() {
        WriteCellStyle headWriteCellStyle = new WriteCellStyle();

        // 背景颜色
        headWriteCellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        headWriteCellStyle.setFillPatternType(FillPatternType.SOLID_FOREGROUND);

        // 字体
        WriteFont headWriteFont = new WriteFont();
        headWriteFont.setFontName("宋体");
        headWriteFont.setFontHeightInPoints((short) 12);
        headWriteFont.setBold(true);
        headWriteCellStyle.setWriteFont(headWriteFont);

        // 边框
        headWriteCellStyle.setBorderBottom(BorderStyle.THIN);
        headWriteCellStyle.setBorderLeft(BorderStyle.THIN);
        headWriteCellStyle.setBorderRight(BorderStyle.THIN);
        headWriteCellStyle.setBorderTop(BorderStyle.THIN);

        // 居中对齐
        headWriteCellStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);

        return headWriteCellStyle;
    }

    /**
     * 内容样式
     */
    private static WriteCellStyle createContentWriteCellStyle() {
        WriteCellStyle contentWriteCellStyle = new WriteCellStyle();

        // 字体
        WriteFont contentWriteFont = new WriteFont();
        contentWriteFont.setFontName("宋体");
        contentWriteFont.setFontHeightInPoints((short) 11);
        contentWriteCellStyle.setWriteFont(contentWriteFont);

        // 边框
        contentWriteCellStyle.setBorderBottom(BorderStyle.THIN);
        contentWriteCellStyle.setBorderLeft(BorderStyle.THIN);
        contentWriteCellStyle.setBorderRight(BorderStyle.THIN);
        contentWriteCellStyle.setBorderTop(BorderStyle.THIN);

        // 自动换行
        contentWriteCellStyle.setWrapped(true);

        return contentWriteCellStyle;
    }
}
