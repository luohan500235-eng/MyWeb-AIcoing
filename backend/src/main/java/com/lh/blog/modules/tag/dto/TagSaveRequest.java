package com.lh.blog.modules.tag.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TagSaveRequest {

    private Long id;

    @NotBlank(message = "标签名称不能为空")
    @Size(max = 50, message = "标签名称长度不能超过50")
    private String name;

    @NotBlank(message = "slug不能为空")
    @Size(max = 100, message = "slug长度不能超过100")
    private String slug;

    @Size(max = 20, message = "颜色长度不能超过20")
    private String color;
}