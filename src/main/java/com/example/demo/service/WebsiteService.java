package com.example.demo.service;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.example.demo.domain.Image;

public interface WebsiteService {

	
	public void addNews(JSONObject parm,HttpSession session);
	
	public void addImage(Image image,MultipartFile multipartFile,HttpSession session)throws Exception;
	
	public void updateNews(JSONObject parm,HttpSession session);
	
	public void updateImage(JSONObject parm,HttpSession session);
	
	public Map<String,Object> PageNews(String params);
	
	public Map<String,Object> PageImage(String params);
	
}
