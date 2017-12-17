package com.hikvision.ga.hephaestus.site.security.config;

import java.util.Collection;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.ldap.core.DirContextOperations;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.ldap.userdetails.LdapAuthoritiesPopulator;
import org.springframework.stereotype.Service;

import com.hikvision.ga.hephaestus.site.security.domain.UserRole;
import com.hikvision.ga.hephaestus.site.security.repository.UserRoleRepository;


/**
 * Spring Secutity 登陆时，获取权限的实现
 */
@Service("ldapAuthoritiesPopulator")
@Scope("prototype")
public class HikLdapAuthoritiesPopulator implements LdapAuthoritiesPopulator {

  @Resource(name = "userInfoDAO")
  private UserRoleRepository userInfoDAO;

  private List<UserRole> roles;

  @Override
  public Collection<? extends GrantedAuthority> getGrantedAuthorities(DirContextOperations arg0,
      String arg1) {
    if (roles == null || roles.size() < 1) {
      return AuthorityUtils.commaSeparatedStringToAuthorityList("");
    }
    StringBuilder commaBuilder = new StringBuilder();
    for (UserRole role : roles) {
      commaBuilder.append(role.getRole()).append(",");
    }
    String authorities = commaBuilder.substring(0, commaBuilder.length() - 1);
    return AuthorityUtils.commaSeparatedStringToAuthorityList(authorities);
  }

}
