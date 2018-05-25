package com.example.demo.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class User extends Base implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 2919041135304623598L;
	
	private Integer uid;
	private String username;
	private String password;
	private String status;
	private String rname;
	private Integer rid;
	private Set<Role> roles=new HashSet<Role>();
	public Integer getUid() {
		return uid;
	}
	public void setUid(Integer uid) {
		this.uid = uid;
	}
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Set<Role> getRoles() {
		return roles;
	}
	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}
	public String getRname() {
		return rname;
	}
	public void setRname(String rname) {
		this.rname = rname;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Integer getRid() {
		return rid;
	}
	public void setRid(Integer rid) {
		this.rid = rid;
	}
	
	
}
