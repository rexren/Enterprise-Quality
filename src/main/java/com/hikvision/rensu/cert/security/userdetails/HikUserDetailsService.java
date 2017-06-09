package com.hikvision.rensu.cert.security.userdetails;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.hikvision.rensu.cert.domain.SystemUser;
import com.hikvision.rensu.cert.domain.UserRole;
import com.hikvision.rensu.cert.service.SystemUserService;
import com.hikvision.rensu.cert.service.UserRoleService;

/**
 * 实现UserDetailsService接口
 * 
 * map: MyUserDetailsService 
 * UserDetailsService提供了获取UserDetails的方式， 最终生成用户和权限共同组成的UserDetails，
 * 在这里就可以实现从自定义的数据源中获取用户信息了
 * */
@Service
public class HikUserDetailsService implements UserDetailsService{

	private SystemUserService systemUserService;
	
	private UserRoleService userRoleService;
	
	@Override
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		SystemUser user;
		try {
			user = systemUserService.findByName(userName).get(0);
		} catch (Exception e) {
			throw new UsernameNotFoundException("user select fail");
		}
		if(user == null){
			throw new UsernameNotFoundException("no user found");
		} else {
			try {
				List<UserRole> roles = userRoleService.getRoleByUserId(user.getId());
				return new HikUserDetails(user, roles);
			} catch (Exception e) {
				throw new UsernameNotFoundException("user role select fail");
			}
		}
	}

}
