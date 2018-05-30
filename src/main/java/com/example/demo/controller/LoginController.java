package com.example.demo.controller;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpSession;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.SessionException;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.domain.User;
import com.example.demo.service.UserService;

@Controller
public class LoginController {
	
	private static final Logger log = LoggerFactory.getLogger(LoginController.class);

	@Autowired
    private UserService userService;
   
	@RequestMapping("/login")
    public String login(){
        return "index";
    }
	
	
	@RequestMapping("/forbidden")
	@ResponseBody
    public Map<String,Object> forbidden(){
		HashMap<String,Object> map=new HashMap<String,Object>();
		map.put("success",false);
		map.put("msg","没有权限");
		return map;
    }
	
	
    @RequestMapping("/loginUser")
    @ResponseBody
    public Map<String,Object> loginUser(@RequestBody JSONObject login,HttpSession session) {
    	log.info("login:{}",login);
    	HashMap<String,Object> map=new HashMap<String,Object>();
    	String username=login.getString("username");
        String password=login.getString("password");
        String code=login.getString("code");
		if(!code.equalsIgnoreCase((String) session.getAttribute("code"))) {
			map.put("msg","验证码错误");
			return map;
		}
    	Subject subject = SecurityUtils.getSubject();
 	    UsernamePasswordToken token = new UsernamePasswordToken(username,password);
 	    token.setRememberMe(true);
        try {
        	subject.login(token);
        	map.put("success",true);
            return map;
        }catch (AuthenticationException e) {
    	  map.put("success",false);
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
        if (subject.isAuthenticated()) {  
        	try {
        		session.removeAttribute("user");
                subject.logout();
            } catch (SessionException ise) {
               ise.printStackTrace();
            }catch (Exception e) {
                e.printStackTrace();
            }
        }  
        return "index";
    }
    
    
    @RequestMapping("/upPassword")
    @ResponseBody
    public Map<String,Object> upPassword(@RequestBody JSONObject login,HttpSession session) {
    	HashMap<String,Object> map=new HashMap<String,Object>();
		try {
			String password = login.getString("password");
			String password1 = login.getString("password1");
			String username=(String) session.getAttribute("user");
			User user=userService.findUserByUserName(username);
			if(!password.equals(user.getPassword())) {
				map.put("msg", "原密码不正确");
				map.put("success",false);
			}else {
				user.setPassword(password1);
				userService.update(user);
				map.put("success", true);
			}
		} catch (Exception e) {
			map.put("success",false);
		}
		return map;
    }
    
  
}
