package com.lh.blog.modules.comment.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommentSubmitRequest {

    @NotNull(message = "文章不能为空")
    private Long postId;

    private Long parentId = 0L;

    private Long replyToId;

    @NotBlank(message = "昵称不能为空")
    @Size(max = 50, message = "昵称长度不能超过50")
    private String nickname;

    @Size(max = 100, message = "邮箱长度不能超过100")
    private String email;

    @Size(max = 255, message = "网站长度不能超过255")
    private String website;

    @NotBlank(message = "评论内容不能为空")
    @Size(max = 1000, message = "评论长度不能超过1000")
    private String content;
}