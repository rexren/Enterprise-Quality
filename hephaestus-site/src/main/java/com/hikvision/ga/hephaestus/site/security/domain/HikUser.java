package com.hikvision.ga.hephaestus.site.security.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * 用户权限表
*/
@Entity
public class HikUser implements Serializable {
	
	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 自增ID
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	/**
	 * 用户角色，只有一个
	 */
	@Column
	private String role;
	
	@Column
	private String userName;
	
	public Long getId() {
		return id;
	}
	
	public String getRole() {
		return role;
	}
	
	public void setRole(String role) {
		this.role = role;
	}

	public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public HikUser() {
    super();
  }

  public HikUser(String role, String userName) {
    super();
    this.role = role;
    this.userName = userName;
  }
}
