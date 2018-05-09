package com.example.demo.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.example.demo.domain.User;
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
}
