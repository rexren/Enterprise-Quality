package com.hikvision.ga.hephaestus.site.cert.security.userdetails;

import java.util.Collection;
import java.util.List;

import com.hikvision.ga.hephaestus.site.cert.domain.UserRole;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import com.hikvision.ga.hephaestus.site.cert.domain.SystemUser;

/**
 * 自己声明类实现UserDetails接口
 * map: MyUserDetails 
 * UserDetails是Spring Security的用户认证实体，带有用户名、密码、权限列表、过期特性等性质
 * */
public class HikUserDetails extends SystemUser implements UserDetails{

	private static final long serialVersionUID = 1L;
	
	private List<UserRole> roles;

    public HikUserDetails(SystemUser user, List<UserRole> roles){
        super(user);
        this.setRoles(roles);
    }

    public List<UserRole> getRoles() {
		return roles;
	}

	public void setRoles(List<UserRole> roles) {
		this.roles = roles;
	}

	@Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if(roles == null || roles.size() <1){
            return AuthorityUtils.commaSeparatedStringToAuthorityList("");
        }
        StringBuilder commaBuilder = new StringBuilder();
        for(UserRole role : roles){
            commaBuilder.append(role.getRole()).append(",");
        }
        String authorities = commaBuilder.substring(0,commaBuilder.length()-1);
        return AuthorityUtils.commaSeparatedStringToAuthorityList(authorities);
    }

    @Override
    public String getPassword() {
        return super.getPassword();
    }

    @Override
    public String getName() {
        return super.getName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

	@Override
	public String getUsername() {
		return super.getName();
	}


}
