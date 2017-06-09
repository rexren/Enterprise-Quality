package com.hikvision.rensu.cert.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.hikvision.rensu.cert.domain.UserRole;
import com.hikvision.rensu.cert.repository.UserRoleRepository;

public class UserRoleService{

	@Autowired
	private UserRoleRepository userRoleRepository;
	
	public List<UserRole> getRoleByUserId(Long userId){
		return userRoleRepository.getRoleByUserId(userId);
	}

	
}
