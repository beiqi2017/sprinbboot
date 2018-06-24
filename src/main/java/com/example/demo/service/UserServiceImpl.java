package com.example.demo.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;

import com.example.demo.dao.UserDao;
import com.example.demo.domain.User;

@Service
@CacheConfig(cacheNames="userCache")
public class UserServiceImpl implements UserService{

	@Autowired
    private UserDao userMapper;
	
	 @CachePut(key="user")
	 public List<User> find() {
	        return userMapper.getUser();
	    }
}
