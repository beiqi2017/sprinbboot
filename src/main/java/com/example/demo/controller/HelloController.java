package com.example.demo.controller;

import org.springframework.web.bind.annotation.RestController;

import com.example.demo.domain.User;
import com.example.demo.service.UserService;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

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
    
   
    
    
    
}
