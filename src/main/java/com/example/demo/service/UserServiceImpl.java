package com.example.demo.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.mgt.RealmSecurityManager;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
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
import com.example.demo.shiro.AuthRealm;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

@Service
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
	@Cacheable(key="'user_menu_'+#uid", value = "userCache")
	public List<Module> getMenu(String uid) {
		// TODO Auto-generated method stub
		List<Module> result= moduleDao.menu("menu");
		User user=userMapper.getUser(uid);
		for(int i=0;i<result.size()&&user!=null;i++) {
			Module module=result.get(i);
			List<Module> result1= moduleDao.subMenu(module.getMid(),user.getUid());
			if(result1.size()>0) {
				 module.setModules(result1);
			}else {
				 result.remove(i);
				 i--;
			}
		  
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
	
	@Cacheable(key="'user_tree_'+#rid", value = "userCache")
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
	@CacheEvict(allEntries=true, value="userCache")
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
			User user=new User();
			user.setRid(rid);
			user.setPage(1);
			user.setRows(9999);
			List<User> users=userMapper.list(user);
			for(User u:users) {
				clearPerm(u.getUsername());
			}
		}
	}
	
	
	public void addRole(JSONObject parm) {
		Role role=new Role();
		role.setRname(parm.getString("role"));
		roleDao.add(role);
	}
	
	@CacheEvict(allEntries=true, value="userCache")
	public void upStatus(JSONObject parm) {
		User user=new User();
		user.setUid(parm.getInteger("id"));
		String status=parm.getString("status");
		if("恢复".equals(status)) {
			user.setStatus("normal");
		}else {
			user.setStatus("stop");
		}
		userMapper.update(user);
		user=userMapper.findById(user);
		clearPerm(user.getUsername());
	}
	
	@Transactional
    public void addUser(JSONObject parm) {
    	User user=JSON.parseObject(parm.toJSONString(), User.class);
    	user.setStatus("normal");
    	userMapper.add(user);
    	userMapper.addRole(user);
    };
    
    @Transactional
    @CacheEvict(allEntries=true, value="userCache")
    public void updateUser(JSONObject parm) {
    	User user=JSON.parseObject(parm.toJSONString(), User.class);
    	userMapper.upRole(user);
    	userMapper.update(user);
    	user=userMapper.findById(user);
    	clearPerm(user.getUsername());
    };   
    
    
    private void clearPerm(String username) {
        
        Subject subject = SecurityUtils.getSubject();   
        String realmName = subject.getPrincipals().getRealmNames().iterator().next();   
        //第一个参数为用户名,第二个参数为realmName,test想要操作权限的用户   
        SimplePrincipalCollection principals = new SimplePrincipalCollection(username,realmName);   
        subject.runAs(principals);   
        
        RealmSecurityManager rsm = (RealmSecurityManager)SecurityUtils.getSecurityManager();    
		AuthRealm realm = (AuthRealm)rsm.getRealms().iterator().next();  
		realm.getAuthorizationCache().remove(subject.getPrincipals());   
        subject.releaseRunAs();  
    }
	 
}
