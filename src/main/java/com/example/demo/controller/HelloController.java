package com.example.demo.controller;

import org.springframework.web.bind.annotation.RestController;
import com.example.demo.dao.User;
import com.example.demo.service.UserService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
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

    @RequestMapping(value= "/book", method= RequestMethod.GET)
    public List<User> mybatis() {
        return userService.find(); 
    }
    
   
    
    
    
}
