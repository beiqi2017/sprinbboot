package com.example.demo.service;

import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.demo.dao.ImageDao;
import com.example.demo.dao.NewsDao;
import com.example.demo.domain.Image;
import com.example.demo.domain.News;
import com.example.demo.util.ImageUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;

@Service
public class WebsiteServiceImpl implements WebsiteService{
	
	private static final Logger log = LoggerFactory.getLogger(WebsiteServiceImpl.class);

	/**
     * 在配置文件中配置的文件保存路径
     */
    @Value("${img.location}")
    private String location;
    
	@Autowired
    private NewsDao newsDao;
	
	@Autowired
    private ImageDao imageDao;

	@CacheEvict(allEntries=true,value="userCache")
	public void addNews(JSONObject parm,HttpSession session) {
		News news=JSON.parseObject(parm.toJSONString(), News.class);
		String username=(String) session.getAttribute("user");
		news.setStatus("normal");
		news.setCreateuser(username);
		newsDao.insert(news);
	}
	@CacheEvict(allEntries=true, value="userCache")
	public void addImage(Image image, MultipartFile multipartFile, HttpSession session) throws Exception {

		String root_fileName = multipartFile.getOriginalFilename();
		String contentType = multipartFile.getContentType();
		log.info("上传图片:name={},type={}", root_fileName, contentType);
		log.info("图片保存路径={}", location);
		String file_name = ImageUtil.saveImg(multipartFile, location);
		
		String username = (String) session.getAttribute("user");
		image.setPath(file_name);
		image.setStatus("normal");
		image.setCreateuser(username);
		imageDao.insert(image);

	}
	
	@CacheEvict(allEntries=true, value="userCache")
	public void updateNews(JSONObject parm,HttpSession session) {
		News news=JSON.parseObject(parm.toJSONString(), News.class);
		String username=(String) session.getAttribute("user");
		news.setUpdateuser(username);
		news.setStatus("stop");
		newsDao.update(news);
	}
	
	@CacheEvict(allEntries=true, value="userCache")
	public void updateImage(JSONObject parm,HttpSession session) {
		Image image=JSON.parseObject(parm.toJSONString(), Image.class);
		String username=(String) session.getAttribute("user");
		image.setUpdateuser(username);
		image.setStatus("stop");
		imageDao.update(image);
	}

	@Cacheable(key="'user_news_'+#params", value = "userCache")
    public Map<String,Object> PageNews(String params) {
		 JSONObject jsonObject = JSONObject.parseObject(params);
	     // 当前页
	     int pageNo = StringUtils.isEmpty(jsonObject.getString("currentPage")) || Integer.parseInt(jsonObject.getString("currentPage")) < 1 ? 1
	                : Integer.parseInt(jsonObject.getString("currentPage"));
	     // 每页行数
	     int pageSize = StringUtils.isEmpty(jsonObject.getString("pageSize")) ? 10 : Integer.parseInt(jsonObject.getString("pageSize"));

	    News news =JSON.parseObject(jsonObject.toJSONString(), News.class);
	    
        PageHelper.startPage(pageNo, pageSize);
        Page<News> page =  newsDao.findByPage(news);
        Map<String,Object> map=new HashMap<String,Object>();
		map.put("total",page.getTotal());
		map.put("rows",page.getResult());
		return map;
    }
	
	@Cacheable(key="'user_image_'+#params", value = "userCache")
    public Map<String,Object> PageImage(String params) {
		JSONObject jsonObject = JSONObject.parseObject(params);
	     // 当前页
	    int pageNo = StringUtils.isEmpty(jsonObject.getString("currentPage")) || Integer.parseInt(jsonObject.getString("currentPage")) < 1 ? 1
	                : Integer.parseInt(jsonObject.getString("currentPage"));
	     // 每页行数
	    int pageSize = StringUtils.isEmpty(jsonObject.getString("pageSize")) ? 10 : Integer.parseInt(jsonObject.getString("pageSize"));

	    Image image =JSON.parseObject(jsonObject.toJSONString(), Image.class);
	    
        PageHelper.startPage(pageNo, pageSize);
        Page<Image> page =  imageDao.findByPage(image);
        Map<String,Object> map=new HashMap<String,Object>();
		map.put("total",page.getTotal());
		map.put("rows",page.getResult());
		return map;
    }
	
}
