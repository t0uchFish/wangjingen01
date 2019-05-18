package com.leyou.upload.service;

import org.springframework.web.multipart.MultipartFile;

public interface UploadService {
    /**
     * 上传图片功能
     * @param file
     * @return
     */
    String uploadImage(MultipartFile file);
}
