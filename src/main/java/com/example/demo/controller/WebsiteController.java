package com.example.demo.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.domain.Image;
import com.example.demo.service.WebsiteService;

@Controller 
@RequestMapping(value = "/website", produces = "application/json;charset=UTF-8")
public class WebsiteController {

	private static final Logger log = LoggerFactory.getLogger(SystemController.class);


	@Autowired
    private WebsiteService websiteService;
	
	@RequestMapping(value = "/addNews", method = RequestMethod.POST)
	@ResponseBody
	public Map<String,Object> addNews(@RequestBody JSONObject params,HttpSession session) {
		log.info("params:{}",params);
		HashMap<String,Object> result=new HashMap<String,Object>();
		try {
			websiteService.addNews(params,session);
			result.put("success", true);
		} catch (Exception e) {
			result.put("success", false);
			result.put("msg", "新增失败");
		}
		return result;
	}
	
	@RequestMapping("/updateNews")
	@ResponseBody
	public Map<String,Object>  updateNews(@RequestBody JSONObject parm,HttpSession session) {
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			websiteService.updateNews(parm,session);
			result.put("success", true);
		} catch (Exception e) {
			result.put("success", false);
			result.put("msg", "更新失败");
		}
		return result;
	};
	
	@RequestMapping(value = "/addImage")
	@ResponseBody
	public Map<String,Object> addImage(Image image,@RequestParam("uploadFile") MultipartFile multipartFile,HttpSession session) {
		log.info("params:{}",JSONObject.toJSONString(image));
		HashMap<String,Object> result=new HashMap<String,Object>();
		try {
			if (multipartFile.isEmpty() || StringUtils.isEmpty(multipartFile.getOriginalFilename())) {
		          throw new Exception();
		     }
		    String contentType = multipartFile.getContentType();
		    if (!contentType.contains("")) {
		        throw new Exception();
		    }
			websiteService.addImage(image,multipartFile,session);
			result.put("success", true);
		} catch (Exception e) {
			log.error("上传文件异常：",e);
			result.put("success", false);
			result.put("msg", "新增失败");
		}
		return result;
	}
	
	
	@RequestMapping("/updateImage")
	@ResponseBody
	public Map<String,Object>  updateImage(@RequestBody JSONObject parm,HttpSession session) {
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			websiteService.updateImage(parm,session);
			result.put("success", true);
		} catch (Exception e) {
			result.put("success", false);
			result.put("msg", "更新失败");
		}
		return result;
	};
	
	
	@RequestMapping(value = "/PageNews", method = RequestMethod.GET)
	@ResponseBody
	public Map<String,Object> PageNews(@RequestParam("params") String params) {
		log.info("params:{}",params);
		return websiteService.PageNews(params);
	}
	
	
	@RequestMapping(value = "/PageImage", method = RequestMethod.GET)
	@ResponseBody
	public Map<String,Object> PageImage(@RequestParam("params") String params) {
		log.info("params:{}",params);
		return websiteService.PageImage(params);
	}
	
}
