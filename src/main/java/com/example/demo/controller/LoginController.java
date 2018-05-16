package com.example.demo.controller;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpSession;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.alibaba.fastjson.JSONObject;

@Controller
public class LoginController {
	
   
	@RequestMapping("/login")
    public String login(){
        return "index";
    }
	
    @RequestMapping("/loginUser")
    @ResponseBody
    public Map<String,Object> loginUser(@RequestBody JSONObject login,HttpSession session) {
    	String username=login.getString("username");
        String password=login.getString("password");
        String code=login.getString("code");
    	HashMap<String,Object> map=new HashMap<String,Object>();
		map.put("success",false);
		if(!code.equalsIgnoreCase((String) session.getAttribute("code"))) {
			map.put("msg","验证码错误");
			return map;
		}
    	Subject subject = SecurityUtils.getSubject();
 	    UsernamePasswordToken token = new UsernamePasswordToken(username,password);
 	    token.setRememberMe(true);
        try {
        	subject.login(token);
            /*String user=(String) subject.getPrincipal();
            session.setAttribute("user", user);*/
        	map.put("success",true);
            return map;
        }catch (AuthenticationException e) {
  	      token.clear();
  	      String msg = "";
             if (e instanceof org.apache.shiro.authc.ConcurrentAccessException) {
                  msg = "用户已登录！";
              } else if (e instanceof org.apache.shiro.authc.AccountException) {
                  msg = "用户名错误！";
              } else if (e instanceof org.apache.shiro.authc.IncorrectCredentialsException) {
                  msg = "密码错误！";
              } else if (e instanceof org.apache.shiro.authc.AuthenticationException) {
                  msg = "认证失败！";
              }
             map.put("msg",msg);
  	         return map;
  	    } 
        
    }
    @RequestMapping("/logOut")
    public String logOut(HttpSession session) {
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
//        session.removeAttribute("user");
        return "login";
    }
    
  
}
