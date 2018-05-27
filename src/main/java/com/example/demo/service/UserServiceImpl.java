package com.example.demo.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.dao.ModuleDao;
import com.example.demo.dao.RoleDao;
import com.example.demo.dao.UserDao;
import com.example.demo.domain.Module;
import com.example.demo.domain.Right;
import com.example.demo.domain.Role;
import com.example.demo.domain.User;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

@Service
@Transactional
public class UserServiceImpl implements UserService{

	@Autowired
    private UserDao userMapper;
	
	@Autowired
    private RoleDao roleDao;

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
			u.setRid(jsonObject.getInteger("rid"));
			u.setUsername(jsonObject.getString("username"));
			HashMap<String,Object> map=new HashMap<String,Object>();
			map.put("rows",userMapper.list(u));
			map.put("total",userMapper.count(u));
			return map;
		}

	@Override
	public List<Module> getMenu(String uid) {
		// TODO Auto-generated method stub
		List<Module> result= moduleDao.menu("menu");
		User user=userMapper.getUser(uid);
		for(int i=0;i<result.size();i++) {
			Module module=result.get(i);
			List<Module> result1= moduleDao.subMenu(module.getMid(),user.getUid());
		    module.setModules(result1);
		}
		return result;
	}
	
	
	@Override
    public Page<User> findByPage(int pageNo, int pageSize) {
        PageHelper.startPage(pageNo, pageSize);
        return userMapper.findByPage();
    }
	
	@Override
    public Map<String,Object> roleByPage(String params) {
		 JSONObject jsonObject = JSONObject.parseObject(params);
	     // 当前页
	     int currentPage = StringUtils.isEmpty(jsonObject.getString("currentPage")) || Integer.parseInt(jsonObject.getString("currentPage")) < 1 ? 1
	                : Integer.parseInt(jsonObject.getString("currentPage"));
	     // 每页行数
	     int pageSize = StringUtils.isEmpty(jsonObject.getString("pageSize")) ? 10 : Integer.parseInt(jsonObject.getString("pageSize"));

	     Role role=new Role();
	     role.setRname(jsonObject.getString("rname"));
	     PageHelper.startPage(currentPage, pageSize);
	     Page<Role> roles = roleDao.findByPage(role);
	     Map<String,Object> map=new HashMap<String,Object>();
		 map.put("total",roles.getTotal());
		 map.put("rows",roles.getResult());
		 return map;
    }
	
	
	public List<Map<String,Object>> getTree(Integer rid){
		List<Map<String,Object>> result =new ArrayList<Map<String,Object>>();
		
		HashMap<String,Object> root=new HashMap<String,Object>();
		root.put("id", "0");
		root.put("text", "所有");
		
		HashMap<String,Object> state=new HashMap<String,Object>();
		state.put("checked", true);
		
		List<Right> list1 =moduleDao.module(rid,2, null);
		List<Map<String,Object>> node1 =new ArrayList<Map<String,Object>>();
		
		for(int i=0;i<list1.size();i++) {
			Right right=list1.get(i);
			HashMap<String,Object> map=new HashMap<String,Object>();
			map.put("id", right.getMid());
			map.put("text", right.getMenu());
			setState(map,right.getHas());
			
			List<Map<String,Object>> node2 =new ArrayList<Map<String,Object>>();
			List<Right> list2 =moduleDao.module(rid,3, right.getMid());
			for(int j=0;j<list2.size();j++) {
				Right right1=list2.get(j);
				HashMap<String,Object> map1=new HashMap<String,Object>();
				map1.put("id", right1.getMid());
				map1.put("text", right1.getMenu());
				setState(map1,right1.getHas());
				node2.add(map1);
			}
			map.put("nodes", node2);
			node1.add(map);
		}
		root.put("nodes", node1);
		result.add(root);
		return result;
	}
	
	private void setState(HashMap<String,Object> map,String has) {
		if(has!=null) {
			HashMap<String,Object> state=new HashMap<String,Object>();
			state.put("checked", true);
			map.put("state", state);
		}
	}

	@Transactional
	public void updateTree(JSONObject parm) {
		Integer rid=parm.getInteger("rid");
		roleDao.delete(rid);
		if(!"".equals(parm.getString("mid"))) {
			String[] mid=parm.getString("mid").split(",");
			List<Map<String,Object>> list=new ArrayList<Map<String,Object>>();
			for(int i=0;i<mid.length;i++) {
				Map<String,Object> map=new HashMap<String,Object>();
				map.put("mid", new Integer(mid[i]));
				map.put("rid", rid);
				list.add(map);
			}
			roleDao.insert(list);
		}
	}
	
	
	public void addRole(JSONObject parm) {
		Role role=new Role();
		role.setRname(parm.getString("role"));
		roleDao.add(role);
	}
	
	
	public void upStatus(JSONObject parm) {
		User user=new User();
		user.setUid(parm.getInteger("id"));
		String status=parm.getString("status");
		if("恢复".equals(status)) {
			user.setStatus("noraml");
		}else {
			user.setStatus("stop");
		}
		userMapper.update(user);
	}
	
	@Transactional
    public void addUser(JSONObject parm) {
    	User user=JSON.parseObject(parm.toJSONString(), User.class);
    	user.setStatus("normal");
    	userMapper.add(user);
    	userMapper.addRole(user);
    };
    
    @Transactional
    public void updateUser(JSONObject parm) {
    	User user=JSON.parseObject(parm.toJSONString(), User.class);
    	userMapper.upRole(user);
    	userMapper.update(user);
    };   
	 
}
