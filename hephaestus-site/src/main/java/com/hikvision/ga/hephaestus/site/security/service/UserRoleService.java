package com.hikvision.ga.hephaestus.site.security.service;

import java.util.List;

import com.hikvision.ga.hephaestus.site.cert.repository.UserRoleRepository;
import com.hikvision.ga.hephaestus.site.security.domain.UserRole;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserRoleService{

	@Autowired
	private UserRoleRepository userRoleRepository;
	
	public List<UserRole> getRoleByUserId(Long userId){
		return userRoleRepository.getRoleByUserId(userId);
	}

	
}
