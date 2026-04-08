package com.lh.blog.modules.file.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.lh.blog.common.ApiResponse;
import com.lh.blog.modules.file.service.FileService;

@RestController
@RequestMapping("/api/admin/files")
public class FileController {

    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("/upload")
    public ApiResponse<Object> upload(@RequestParam("file") MultipartFile file) {
        return ApiResponse.success(fileService.upload(file));
    }
}