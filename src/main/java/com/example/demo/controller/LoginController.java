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
    @ResponseBody
    public Map<String,Boolean> login(@RequestBody JSONObject login) {
    	HashMap<String,Boolean> map=new HashMap<String,Boolean>();
		map.put("success",true);
		return map;
    }
    @RequestMapping("/loginUser")
    public String loginUser(String username,String password,HttpSession session) {
    	 Subject subject = SecurityUtils.getSubject();
 	    UsernamePasswordToken token = new UsernamePasswordToken(username,password);
 	    token.setRememberMe(true);
        try {
        	subject.login(token);
            /*String user=(String) subject.getPrincipal();
            session.setAttribute("user", user);*/
            return "index";
        }catch (AuthenticationException e) {
  	      token.clear();
  	      e.printStackTrace();
  	      String msg = "";
             if (e instanceof org.apache.shiro.authc.ConcurrentAccessException) {
                  msg = "用户已登录！";
               } else if (e instanceof org.apache.shiro.authc.AccountException) {
                  msg = "未知帐号错误或用户状态异常！";
              } else if (e instanceof org.apache.shiro.authc.IncorrectCredentialsException) {
                  msg = "用户名密码错误！";
               } else if (e instanceof org.apache.shiro.authc.AuthenticationException) {
                  msg = "认证失败！";
               }
              System.out.println(msg);
  	          return "login";
  	    } 
        
    }
    @RequestMapping("/logOut")
    public String logOut(HttpSession session) {
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
//        session.removeAttribute("user");
        return "login";
    }
    
    @RequestMapping("/test")
    public String test() {
        return "test";
    }
}
