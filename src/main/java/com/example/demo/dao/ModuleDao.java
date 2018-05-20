package com.example.demo.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.example.demo.domain.Module;
import com.example.demo.domain.Right;

@Mapper
public interface ModuleDao {

	  List<Module> menu(String  type);
	  
	  List<Module> subMenu(@Param("pid")Integer pid,@Param("uid")Integer uid);
	  
	  List<Right> module(@Param("rid")Integer rid,@Param("level")Integer level,@Param("pid")Integer pid);
}
