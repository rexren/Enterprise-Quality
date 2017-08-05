package com.hikvision.ga.hephaestus.site.cert.service;

import java.security.Principal;
import java.util.List;

import com.hikvision.ga.hephaestus.site.cert.domain.SystemUser;
import com.hikvision.ga.hephaestus.site.cert.repository.SystemUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SystemUserService{
	
	@Autowired
	private SystemUserRepository systemUserRepository;
	
	public List<SystemUser> findByName(String userName){
		return systemUserRepository.findByName(userName);
	}
	
	public String getCurrentUsername() {

	      Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	      if (principal instanceof UserDetails) {
	         return ((UserDetails) principal).getUsername();
	      }

	      if (principal instanceof Principal) {
	         return ((Principal) principal).getName();
	      }
	      return String.valueOf(principal);
	   }
	
}
