package com.snow.audit.controller;

import com.snow.audit.common.Result;
import com.snow.audit.service.Impl.FileService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.Map;

/**
 * @Author lish
 * @Date 2025/6/23 19:57
 * @DESCRIBE
 */
@RestController
@RequestMapping("/api/common")
@Api(tags = "审批管理接口")
public class CommonController {

    @Autowired
    private FileService fileService;

    /**
     * 图片上传接口
     */
    @PostMapping("/upload/image")
    public Result<Map<String, Object>> uploadImage(@RequestParam("file") MultipartFile file) {
        return Result.success(fileService.uploadImage(file));
    }
}
