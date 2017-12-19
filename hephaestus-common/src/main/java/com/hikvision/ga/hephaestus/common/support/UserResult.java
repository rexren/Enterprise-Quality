package com.hikvision.ga.hephaestus.common.support;

import java.util.List;

public class UserResult extends BaseResult{
  
	private String name;
	
	private List<String> roles;
	
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
  public UserResult(String name, List<String> roles) {
    super();
    this.name = name;
    this.roles = roles;
  }
}
