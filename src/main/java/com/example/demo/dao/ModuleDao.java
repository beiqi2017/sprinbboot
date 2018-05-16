package com.example.demo.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import com.example.demo.domain.Module;

@Mapper
public interface ModuleDao {

	  List<Module> menu(String  type);
	  
	  List<Module> subMenu(Integer id);
}
