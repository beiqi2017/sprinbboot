package com.example.demo.dao;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {

	 @Select("select id, name as name, pwd as pwd from user")
	  List<User> find();
}
