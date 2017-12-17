package com.hikvision.ga.hephaestus.site.security.config;

import java.util.Properties;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.ldap.core.support.BaseLdapPathContextSource;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.ldap.authentication.LdapAuthenticationProvider;
import org.springframework.security.ldap.search.FilterBasedLdapUserSearch;
import org.springframework.stereotype.Service;

@Service("authenticatorProviderBuilder")
@Scope("prototype")
public class AuthenticatorProviderBuilder {
  @Resource(name = "ldapAuthoritiesPopulator")
  HikLdapAuthoritiesPopulator ldapAuthoritiesPopulator;

  @Resource(name = "profileSetting")
  Properties setting;

  public AuthenticationProvider getAuthenticationProvider() {
    String ladpDomain = setting.getProperty("ladp.domain");
    String ladpuserSearch = setting.getProperty("ladp.userSearch");
    String ladpUrl = setting.getProperty("ladp.url");

    BaseLdapPathContextSource contenxSource = new LoginContextSource(ladpUrl);

    FilterBasedLdapUserSearch userSearch =
        new FilterBasedLdapUserSearch(ladpDomain, ladpuserSearch, contenxSource);

    LoginAuthenticator bindAuth = new LoginAuthenticator(contenxSource, ladpDomain, ladpuserSearch);
    bindAuth.setUserSearch(userSearch);

    LdapAuthenticationProvider ldapAuth =
        new LdapAuthenticationProvider(bindAuth, ldapAuthoritiesPopulator);

    return ldapAuth;
  }
}
