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
	private String name;
	private String password;
	private Set<Role> roles=new HashSet<Role>();
	public Integer getUid() {
		return uid;
	}
	public void setUid(Integer uid) {
		this.uid = uid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
	
	
}
