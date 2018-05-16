package com.example.demo.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Module {

	private Integer mid;
	private String mname;
	private String url;
	private String type;
	private String menu;
	private Integer pid;
	private Set<Role> roles;
	private List<Module> modules=new ArrayList<Module>();
	
	public Integer getMid() {
		return mid;
	}
	public void setMid(Integer mid) {
		this.mid = mid;
	}
	public String getMname() {
		return mname;
	}
	public void setMname(String mname) {
		this.mname = mname;
	}
	public Set<Role> getRoles() {
		return roles;
	}
	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getMenu() {
		return menu;
	}
	public void setMenu(String menu) {
		this.menu = menu;
	}
	public Integer getPid() {
		return pid;
	}
	public void setPid(Integer pid) {
		this.pid = pid;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public List<Module> getModules() {
		return modules;
	}
	public void setModules(List<Module> modules) {
		this.modules = modules;
	}
	
	
}
