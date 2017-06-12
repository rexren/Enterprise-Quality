package com.hikvision.rensu.cert.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hikvision.rensu.cert.domain.SystemUser;
import com.hikvision.rensu.cert.repository.SystemUserRepository;

@Service
@Transactional
public class SystemUserService{
	
	@Autowired
	private SystemUserRepository systemUserRepository;
	
	public List<SystemUser> findByName(String userName){
		return systemUserRepository.findByName(userName);
	}
}
