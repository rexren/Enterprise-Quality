package com.hikvision.rensu.cert.security;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import com.hikvision.rensu.cert.security.userdetails.HikUserDetails;
import com.hikvision.rensu.cert.security.userdetails.HikUserDetailsService;

/** 
 * HikAuthenticationProvider
 * 提供用户UserDetails的具体验证方式，在这里可以自定义用户密码的加密、验证方式等等
 */
@Component
public class HikAuthenticationProvider implements AuthenticationProvider {

	@Autowired
    private HikUserDetailsService hikUserService;
	
	 /**
     * 自定义验证方式
     */

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		String username = authentication.getName();
        String password = (String) authentication.getCredentials();
        HikUserDetails user = (HikUserDetails) hikUserService.loadUserByUsername(username);
        if(user == null){
            throw new BadCredentialsException("Username not found.");
        }

        //加密过程在这里体现
        if (!password.equals(user.getPassword())) {
            throw new BadCredentialsException("Wrong password.");
        }

        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
        return new UsernamePasswordAuthenticationToken(user, password, authorities);
	}

	@Override
	public boolean supports(Class<?> arg0) {
		return true;
	}

}
