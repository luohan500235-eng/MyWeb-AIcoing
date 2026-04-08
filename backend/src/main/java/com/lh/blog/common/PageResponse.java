package com.lh.blog.common;

import java.util.List;

import org.springframework.data.domain.Page;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PageResponse<T> {

    private List<T> records;
    private long total;
    private int page;
    private int size;
    private int totalPages;

    public static <T> PageResponse<T> from(Page<T> page) {
        return new PageResponse<T>(page.getContent(), page.getTotalElements(), page.getNumber() + 1,
                page.getSize(), page.getTotalPages());
    }
}