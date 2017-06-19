package com.hikvision.rensu.cert.support;


import java.util.List;

import com.hikvision.rensu.cert.domain.UserRole;

public class UserResult extends BaseResult{
	private long id;
	private String name;
	private List<UserRole> authorities;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public List<UserRole> getAuthorities() {
		return authorities;
	}
	public void setAuthorities(List<UserRole> authorities) {
		this.authorities = authorities;
	}
	public UserResult() {
		super();
	}
}
