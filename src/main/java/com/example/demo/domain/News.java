package com.example.demo.domain;

import java.io.Serializable;
import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;

import com.alibaba.fastjson.annotation.JSONField;

public class News extends Base implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -7229915220141791075L;
	
	private Integer id;
	private String type;
	private String title;
	private String content;
	private String status;
	
	@JSONField(format="yyyyMMdd")
	private Date createtime;
	
	private String createuser;
	private Date updatetime;
	private String updateuser;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Date getCreatetime() {
		return createtime;
	}
	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}
	public String getCreateuser() {
		return createuser;
	}
	public void setCreateuser(String createuser) {
		this.createuser = createuser;
	}
	public Date getUpdatetime() {
		return updatetime;
	}
	public void setUpdatetime(Date updatetime) {
		this.updatetime = updatetime;
	}
	public String getUpdateuser() {
		return updateuser;
	}
	public void setUpdateuser(String updateuser) {
		this.updateuser = updateuser;
	}
	

}
