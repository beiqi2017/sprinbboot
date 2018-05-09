package com.example.demo.service;


import java.util.Map;
import com.example.demo.domain.User;


public interface UserService {

	public User findUserByUserName(String name);
	
	public Map<String,Object> list(String params);
}
