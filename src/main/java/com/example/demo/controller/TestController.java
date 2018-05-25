package com.example.demo.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.domain.Module;
import com.example.demo.service.UserService;

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
	
	@RequestMapping("/upStatus")
	@ResponseBody
	public Map<String,Object> upStatus(@RequestBody JSONObject parm) {
		HashMap<String,Object> result=new HashMap<String,Object>();
		try {
			userService.upStatus(parm);
			result.put("success", true);
		} catch (Exception e) {
			result.put("success", false);
			result.put("msg", "更新失败");
		}
		return result;
	}
	
	@RequestMapping(value = "/rolePage", method = RequestMethod.GET)
	@ResponseBody
	public  Map<String,Object> testFindByPage(@RequestParam("params") String params) {
		return userService.roleByPage(params);
	}

	
	@RequestMapping(value = "/getTree", method = RequestMethod.GET)
	@ResponseBody
	public List<Map<String,Object>> getTree(Integer rid) {
		return userService.getTree(rid);
	}
	
	@RequestMapping("/updateTree")
	@ResponseBody
	public Map<String,Object> updateTree(@RequestBody JSONObject parm) {
		HashMap<String,Object> result=new HashMap<String,Object>();
		try {
			userService.updateTree(parm);
			result.put("success", true);
		} catch (Exception e) {
			result.put("success", false);
			result.put("msg", "更新失败");
		}
		return result;
	}
	
	
	@RequestMapping("/addRole")
	@ResponseBody
	public Map<String,Object> addRole(@RequestBody JSONObject parm) {
		HashMap<String,Object> result=new HashMap<String,Object>();
		try {
			userService.addRole(parm);
			result.put("success", true);
		} catch (Exception e) {
			result.put("success", false);
			result.put("msg", "新增失败");
		}
		return result;
	}
	
	
	
	@RequestMapping(value = "/menu", method = RequestMethod.GET)
	@ResponseBody
	public List<Module> menu(HttpSession session) {
		String username=(String) session.getAttribute("user");
		List<Module> result= userService.getMenu(username);
		return result;
	}
	
	@RequestMapping("/addUser")
	@ResponseBody
	public Map<String,Object>  addUser(@RequestBody JSONObject parm) {
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			userService.addUser(parm);
			result.put("success", true);
		} catch (Exception e) {
			result.put("success", false);
			result.put("msg", "新增失败");
		}
		return result;
	};

	@RequestMapping("/updateUser")
	@ResponseBody
	public Map<String,Object>  updateUser(@RequestBody JSONObject parm) {
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			userService.updateUser(parm);
			result.put("success", true);
		} catch (Exception e) {
			result.put("success", false);
			result.put("msg", "更新失败");
		}
		return result;
	};
	
}
