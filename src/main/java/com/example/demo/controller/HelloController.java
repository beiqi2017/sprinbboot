package com.example.demo.controller;

import org.springframework.web.bind.annotation.RestController;

import com.example.demo.domain.User;
import com.example.demo.service.UserService;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@RestController
public class HelloController {

	 @Autowired
	private  UserService userService;
	 
    @RequestMapping("/")
    public String index() {
        return "Greetings from Spring Boot!";
    }

    @ApiOperation(value="获取图书列表", notes="获取图书列表")
    @RequestMapping(value= "/book", method= RequestMethod.GET)
    public List<User> mybatis() {
        return userService.find(); 
    }
    
    @ResponseBody
    @RequestMapping(value = "/session")
    public Map<String, Object> getSession(HttpServletRequest request) {
        request.getSession().setAttribute("username", "admin");
        request.getSession().setAttribute("test", "111");
        request.getSession().setAttribute("12", "a");
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("sessionId", request.getSession().getId());
        return map;
    }

    @ResponseBody
    @RequestMapping(value = "/test")
    public Map<String, Object> get(HttpServletRequest request) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("username", request.getSession().getAttribute("username"));
        map.put("test", request.getSession().getAttribute("test"));
        map.put("12", request.getSession().getAttribute("12"));
        map.put("sessionId", request.getSession().getId());
        return map;
    }
    
    
}
