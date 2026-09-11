package com.blog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.blog.entity.Category;

import java.util.List;

/**
 * 分类业务接口。
 */
public interface CategoryService extends IService<Category> {

    // 按排序值升序获取全部分类
    List<Category> listOrdered();
}
