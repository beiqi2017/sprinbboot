package com.example.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ViewController {

	
	@RequestMapping("/page")
    public String index() {
        return "test";
    }
	
	@ExceptionHandler(Exception.class)   
    public void handleException(Exception ex) {              
        ex.printStackTrace();
    }  
}
