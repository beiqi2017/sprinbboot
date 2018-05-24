package com.example.demo.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import com.example.demo.domain.Role;
import com.github.pagehelper.Page;

@Mapper
public interface RoleDao {

	/**
     * 分页查询数据
     * @return
     */
    Page<Role> findByPage(Role role);
    
    void delete(int rid);
    
    void insert(List list);
    
    void add(Role role);
}
