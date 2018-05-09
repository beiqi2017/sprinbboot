package com.example.demo.dao;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import com.example.demo.domain.User;

@Mapper
public interface UserDao {

	  User getUser(String username);
	  
	  List<User> list(User user);
	  
	  int count(User user);
}
