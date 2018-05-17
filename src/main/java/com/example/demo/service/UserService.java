package com.example.demo.service;


import java.util.List;
import java.util.Map;
import com.example.demo.domain.Module;
import com.example.demo.domain.User;
import com.github.pagehelper.Page;


public interface UserService {

	public User findUserByUserName(String name);
	
	public Map<String,Object> list(String params);
	
	List<Module> getMenu();
	
	public void update(User user);
	
	 /**
     * 分页查询
     * @param pageNo 页号
     * @param pageSize 每页显示记录数
     * @return
     */
    Page<User> findByPage(int pageNo, int pageSize);
	
}
