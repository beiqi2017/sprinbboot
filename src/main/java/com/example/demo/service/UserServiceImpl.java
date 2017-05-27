package com.example.demo.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.dao.User;
import com.example.demo.dao.UserMapper;

@Service
public class UserServiceImpl implements UserService{

	@Autowired
    private UserMapper userMapper;
	
	 public List<User> find() {
	        return userMapper.findAll();
	    }
}
