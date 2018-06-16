package com.example.demo.websocket;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import com.alibaba.fastjson.JSONObject;

@Controller
@RequestMapping(value = "/websocket", produces = "application/json;charset=UTF-8")
public class SocketController {

    @Autowired
    MyHandler handler;

    @RequestMapping("/info")
    public @ResponseBody HashMap<String,Object> info(HttpSession session,@RequestBody JSONObject parm) {
    	String username=(String) session.getAttribute("user");
    	String info=parm.getString("info");
    	HashMap<String,Object> result=new HashMap<String,Object>();
        result.put("info", info);
        result.put("user", username);
        SimpleDateFormat Fmt=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        result.put("time", Fmt.format(new Date()));
        boolean hasSend = handler.sendMessageToAllUsers(new TextMessage(JSONObject.toJSONString(result)));
        result.put("success", hasSend);
        return result;
    }

    @RequestMapping("/message")
    public @ResponseBody String sendMessage(HttpSession session,@RequestParam("user") String user) {
    	String username=(String) session.getAttribute("user");
        boolean hasSend = handler.sendMessageToUser(user, new TextMessage(username+"发送一条消息给"+user));
        System.out.println(hasSend);
        return "message";
    }

    @RequestMapping("/users")
    public @ResponseBody List<Map<String,Object>> users(HttpSession session) {
    	List<Map<String,Object>> result =new ArrayList<Map<String,Object>>();
    	String username=(String) session.getAttribute("user");
    	Map<String, WebSocketSession> users=MyHandler.getUsers();
    	 Set<String> clientIds = users.keySet();
    	 for (String clientId : clientIds) {
    		 if(!username.equals(clientId)) {
    			 HashMap<String,Object> root=new HashMap<String,Object>();
    			 root.put("id", clientId);
    			 root.put("text", clientId);
    			 result.add(root);
    		 }
    	 }
		return result;
    }
}
