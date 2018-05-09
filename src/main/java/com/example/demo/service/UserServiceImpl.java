package com.example.demo.service;

import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.dao.UserDao;
import com.example.demo.domain.User;

@Service
public class UserServiceImpl implements UserService{

	@Autowired
    private UserDao userMapper;
	
	 public User findUserByUserName(String name) {
	        return userMapper.getUser(name);
	    }
	 
	 public Map<String,Object>  list(String params) {
		    JSONObject jsonObject = JSONObject.parseObject(params);
	        // 当前页
	        int currentPage = StringUtils.isEmpty(jsonObject.getString("currentPage")) || Integer.parseInt(jsonObject.getString("currentPage")) < 1 ? 1
	                : Integer.parseInt(jsonObject.getString("currentPage"));
	        // 每页行数
	        int pageSize = StringUtils.isEmpty(jsonObject.getString("pageSize")) ? 10 : Integer.parseInt(jsonObject.getString("pageSize"));

	    	User u =new User();
			u.setPage(currentPage);
			u.setRows(pageSize);
			
			HashMap<String,Object> map=new HashMap<String,Object>();
			map.put("rows",userMapper.list(u));
			map.put("total",userMapper.count(u));
			return map;
		}
}
