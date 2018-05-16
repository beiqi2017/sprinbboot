package com.example.demo.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.dao.ModuleDao;
import com.example.demo.dao.UserDao;
import com.example.demo.domain.Module;
import com.example.demo.domain.User;

@Service
public class UserServiceImpl implements UserService{

	@Autowired
    private UserDao userMapper;
	

	@Autowired
    private ModuleDao  moduleDao;
	
	 public User findUserByUserName(String name) {
	        return userMapper.getUser(name);
	 }
	 
	 public void update(User user) {
	     userMapper.update(user);
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

	@Override
	public List<Module> getMenu() {
		// TODO Auto-generated method stub
		List<Module> result= moduleDao.menu("menu");
		for(int i=0;i<result.size();i++) {
			Module module=result.get(i);
			List<Module> result1= moduleDao.subMenu(module.getMid());
		    module.setModules(result1);
		}
		return result;
	}
	 
}
