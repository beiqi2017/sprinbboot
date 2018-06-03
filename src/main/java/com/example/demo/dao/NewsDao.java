package com.example.demo.dao;

import org.apache.ibatis.annotations.Mapper;
import com.example.demo.domain.News;
import com.github.pagehelper.Page;

@Mapper
public interface NewsDao {

	/**
     * 分页查询数据
     * @return
     */
    Page<News> findByPage(News news);
    
    void insert(News news);
    
    void update(News news);
}
