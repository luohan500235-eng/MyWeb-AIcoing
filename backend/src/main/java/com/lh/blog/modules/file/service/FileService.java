package com.lh.blog.modules.file.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.lh.blog.common.BusinessException;
import com.lh.blog.modules.file.entity.SysFile;
import com.lh.blog.modules.file.repository.SysFileRepository;

@Service
public class FileService {

    private final SysFileRepository fileRepository;

    public FileService(SysFileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    public Map<String, Object> upload(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new BusinessException("上传文件不能为空");
        }
        String originalName = file.getOriginalFilename();
        String extension = StringUtils.getFilenameExtension(originalName);
        String dateFolder = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMM"));
        String generatedName = UUID.randomUUID().toString().replace("-", "")
                + (StringUtils.hasText(extension) ? "." + extension : "");
        Path folder = Paths.get("uploads", dateFolder);
        Path target = folder.resolve(generatedName);
        try {
            Files.createDirectories(folder);
            Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
            throw new BusinessException("文件保存失败: " + ex.getMessage());
        }
        SysFile entity = new SysFile();
        entity.setOriginName(originalName);
        entity.setFileName(generatedName);
        entity.setFilePath(target.toString().replace('\\', '/'));
        entity.setFileUrl("/uploads/" + dateFolder + "/" + generatedName);
        entity.setFileSize(file.getSize());
        entity.setContentType(file.getContentType());
        fileRepository.save(entity);

        Map<String, Object> result = new LinkedHashMap<String, Object>();
        result.put("id", entity.getId());
        result.put("name", entity.getOriginName());
        result.put("url", entity.getFileUrl());
        result.put("size", entity.getFileSize());
        return result;
    }
}