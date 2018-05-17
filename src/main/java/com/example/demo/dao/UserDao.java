package com.example.demo.dao;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import com.example.demo.domain.User;
import com.github.pagehelper.Page;

@Mapper
public interface UserDao {

	  User getUser(String username);
	  
	  List<User> list(User user);
	  
	  int count(User user);
	  
	  void update(User user);
	  
	  /**
	     * 分页查询数据
	     * @return
	     */
	 Page<User> findByPage();
}
