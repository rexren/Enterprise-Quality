package com.hikvision.ga.hephaestus.site.cert.support;

import java.util.List;

public class UserResult extends BaseResult{
	private long id;
	private String name;
	private List<String> roles;
	
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
	public List<String> getRoles() {
		return roles;
	}
	public void setRoles(List<String> roles) {
		this.roles = roles;
	}
	public UserResult() {
		super();
	}
	
}
