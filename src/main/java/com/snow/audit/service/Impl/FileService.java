package com.snow.audit.service.Impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class FileService {
    // 从配置文件读取上传路径和域名
    @Value("${file.upload.path:/opt/uploads/}")
    private String uploadPath;

    @Value("${file.base.url:http://8.134.63.161:8080}")
    private String baseUrl;

    // 允许的图片格式
    private static final List<String> ALLOWED_EXTENSIONS = Arrays.asList(
            "jpg", "jpeg", "png", "gif", "bmp", "webp"
    );

    // 最大文件大小 5MB
    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024;

    /**
     * 上传图片文件
     */
    public Map<String, Object> uploadImage(MultipartFile file) {
        Map<String, Object> data = new HashMap<>();

        try {
            // 验证文件
            validateFile(file);

            // 生成文件名
            String fileName = generateFileName(file.getOriginalFilename());

            // 创建上传目录
            createUploadDirectory();

            // 保存文件
            String filePath = saveFile(file, fileName);

            // 生成访问URL
            String fileUrl = baseUrl + "/files/" + fileName;

            // 构建返回结果

            data.put("url", fileUrl);
            data.put("fileName", fileName);

        } catch (IllegalArgumentException e) {

        } catch (Exception e) {

        }

        return data;
    }

    /**
     * 验证文件
     */
    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("文件不能为空");
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new IllegalArgumentException("文件大小不能超过5MB");
        }

        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || originalFilename.trim().isEmpty()) {
            throw new IllegalArgumentException("文件名不能为空");
        }

        String extension = getFileExtension(originalFilename).toLowerCase();
        if (!ALLOWED_EXTENSIONS.contains(extension)) {
            throw new IllegalArgumentException("只支持图片格式：" + String.join(", ", ALLOWED_EXTENSIONS));
        }
    }

    /**
     * 生成唯一文件名
     */
    private String generateFileName(String originalFilename) {
        String extension = getFileExtension(originalFilename);
        String dateStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String uuid = UUID.randomUUID().toString().replace("-", "");
        return dateStr + "_" + uuid + "." + extension;
    }

    /**
     * 获取文件扩展名
     */
    private String getFileExtension(String filename) {
        int lastDotIndex = filename.lastIndexOf(".");
        if (lastDotIndex > 0 && lastDotIndex < filename.length() - 1) {
            return filename.substring(lastDotIndex + 1);
        }
        return "";
    }

    /**
     * 创建上传目录
     */
    private void createUploadDirectory() throws IOException {
        Path uploadDir = Paths.get(uploadPath);
        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir);
        }
    }

    /**
     * 保存文件到指定路径
     */
    private String saveFile(MultipartFile file, String fileName) throws IOException {
        Path filePath = Paths.get(uploadPath, fileName);
        Files.copy(file.getInputStream(), filePath);
        return filePath.toString();
    }


}
