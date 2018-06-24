package com.example.demo.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import com.example.demo.dao.UserDao;
import com.example.demo.domain.User;

@Service
public class UserServiceImpl implements UserService{

	@Autowired
    private UserDao userMapper;
	
	@Cacheable(value = "usercache",keyGenerator = "wiselyKeyGenerator")  
	 public List<User> find() {
	        return userMapper.getUser();
	    }
}
