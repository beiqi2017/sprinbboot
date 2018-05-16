package com.example.demo.service;


import java.util.List;
import java.util.Map;
import com.example.demo.domain.Module;
import com.example.demo.domain.User;


public interface UserService {

	public User findUserByUserName(String name);
	
	public Map<String,Object> list(String params);
	
	List<Module> getMenu();
	
	public void update(User user);
	
}
