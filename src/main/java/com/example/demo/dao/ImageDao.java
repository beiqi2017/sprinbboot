package com.example.demo.dao;


import org.apache.ibatis.annotations.Mapper;
import com.example.demo.domain.Image;
import com.github.pagehelper.Page;

@Mapper
public interface ImageDao {

	/**
     * 分页查询数据
     * @return
     */
    Page<Image> findByPage(Image image);
    
    void insert(Image image);
    
    void update(Image image);
}
