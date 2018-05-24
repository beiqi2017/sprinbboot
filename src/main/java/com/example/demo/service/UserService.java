package com.example.demo.service;


import java.util.List;
import java.util.Map;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.domain.Module;
import com.example.demo.domain.User;
import com.github.pagehelper.Page;


public interface UserService {

	public User findUserByUserName(String name);
	
	public Map<String,Object> list(String params);
	
	public List<Module> getMenu(String uid);
	
	public void update(User user);
	
	 /**
     * 分页查询
     * @param pageNo 页号
     * @param pageSize 每页显示记录数
     * @return
     */
	public Page<User> findByPage(int pageNo, int pageSize);
    
    /**
     * 分页查询
     * @param pageNo 页号
     * @param pageSize 每页显示记录数
     * @return
     */
    public Map<String,Object> roleByPage(String params);
    
    public List<Map<String,Object>> getTree(Integer rid);
    
    public void  updateTree(JSONObject parm);
    
    public void  addRole(JSONObject parm);
    
    
    public void upStatus(JSONObject parm);
	
}
