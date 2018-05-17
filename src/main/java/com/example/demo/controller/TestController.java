package com.example.demo.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.domain.Module;
import com.example.demo.domain.User;
import com.example.demo.service.UserService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;

@Controller 
@RequestMapping(value = "/user", produces = "application/json;charset=UTF-8")
public class TestController {

	@Autowired
    private UserService userService;
	
	@RequestMapping(value = "/page", method = RequestMethod.GET)
	@ResponseBody
	public Map<String,Object> menuList(@RequestParam("params") String params) {
		return userService.list(params);
	}
	
	@RequestMapping(value = "/page1", method = RequestMethod.GET)
	@ResponseBody
	public PageInfo<User> testFindByPage() {
		Page<User> persons = userService.findByPage(1, 2);
		// 需要把Page包装成PageInfo对象才能序列化。该插件也默认实现了一个PageInfo
		PageInfo<User> pageInfo = new PageInfo<>(persons);
		return pageInfo;
	}

	
	@RequestMapping(value = "/getTree", method = RequestMethod.GET)
	@ResponseBody
	public List<Map<String,Object>> getTree() {
		List<Map<String,Object>> result =new ArrayList<Map<String,Object>>();
		HashMap<String,Object> root=new HashMap<String,Object>();
		root.put("id", "0");
		root.put("text", "所有");
		List<Map<String,Object>> list =new ArrayList<Map<String,Object>>();
		for(int i=0;i<10;i++) {
			HashMap<String,Object> map=new HashMap<String,Object>();
			map.put("id", "2"+i);
			map.put("text", "2"+i);
			List<Map<String,Object>> nodes =new ArrayList<Map<String,Object>>();
			if(i==1||i==3||i==5||i==6) {
				HashMap<String,Object> map1=new HashMap<String,Object>();
				map1.put("id", "21"+i);
				map1.put("text", "21"+i);
				nodes.add(map1);
			}
			map.put("nodes", nodes);
			list.add(map);
		}
		HashMap<String,Object> state=new HashMap<String,Object>();
		state.put("checked", true);
		root.put("state", state);
		root.put("nodes", list);
		result.add(root);
		return result;
	}
	
	
	
	@RequestMapping(value = "/menu", method = RequestMethod.GET)
	@ResponseBody
	public List<Module> menu() {
		List<Module> result= userService.getMenu();
		return result;
	}
	
	
	
}
