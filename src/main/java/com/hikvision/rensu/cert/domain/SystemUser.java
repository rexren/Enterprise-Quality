package com.hikvision.rensu.cert.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * 用户表
 * 用户和角色可以有一对多的关系，因此可以将用户和角色分成两张表
*/
@Entity
public class SystemUser implements Serializable{
	 
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column
	private String name;
	
	@Column
	private String password;

	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
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
	
	public SystemUser(){
		
	}

	public SystemUser(SystemUser user){
		this.name = user.getName();
		this.password = user.getPassword();
		this.id = user.getId();
	}
	
}
