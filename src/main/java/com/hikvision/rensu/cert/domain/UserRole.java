package com.hikvision.rensu.cert.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * 权限表
 * 用户和角色可以有一对多的关系，因此可以将用户和角色分成两张表
*/
@Entity
public class UserRole implements Serializable{
	
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	@Column
	private String role;
	
	@Column
	private Long userId;
	
	public Long getId() {
		return id;
	}
	
	public String getRole() {
		return role;
	}
	
	public void setRole(String role) {
		this.role = role;
	}
	
	public Long getUserId() {
		return userId;
	}
	
	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public UserRole() {
	}
	
	public UserRole(UserRole userRole) {
		this.id=userRole.getId();
		this.role = userRole.getRole();
		this.userId = userRole.getUserId();
	}
	
}
