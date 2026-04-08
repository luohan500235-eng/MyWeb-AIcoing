package com.lh.blog.modules.post.dto;

import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostSaveRequest {

    private Long id;

    @NotBlank(message = "标题不能为空")
    @Size(max = 200, message = "标题长度不能超过200")
    private String title;

    @NotBlank(message = "slug不能为空")
    @Size(max = 200, message = "slug长度不能超过200")
    private String slug;

    @Size(max = 500, message = "摘要长度不能超过500")
    private String summary;

    private String coverImage;

    @NotBlank(message = "内容不能为空")
    private String contentMd;

    private Long categoryId;

    @NotNull(message = "状态不能为空")
    private Integer status;

    private Integer isTop = 0;

    private Integer allowComment = 1;

    private String seoTitle;

    private String seoDescription;

    private List<Long> tagIds;
}