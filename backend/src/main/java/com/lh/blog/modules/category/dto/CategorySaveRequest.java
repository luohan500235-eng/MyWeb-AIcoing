package com.lh.blog.modules.category.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CategorySaveRequest {

    private Long id;

    @NotBlank(message = "分类名称不能为空")
    @Size(max = 50, message = "分类名称长度不能超过50")
    private String name;

    @NotBlank(message = "slug不能为空")
    @Size(max = 100, message = "slug长度不能超过100")
    private String slug;

    @Size(max = 255, message = "描述长度不能超过255")
    private String description;

    @NotNull(message = "排序不能为空")
    private Integer sort;

    @NotNull(message = "状态不能为空")
    private Integer status;
}