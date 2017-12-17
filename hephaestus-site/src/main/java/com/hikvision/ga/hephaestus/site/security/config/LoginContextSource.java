package com.hikvision.ga.hephaestus.site.security.config;

import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;

import org.springframework.security.ldap.ppolicy.PasswordPolicyAwareContextSource;

/**
 * @Description: 登录环境变量的设置
 * @author langyicong
 * @date 2017年12月17日
 */
public class LoginContextSource extends PasswordPolicyAwareContextSource {
  
    private static final String LDAP_FACTORY = "com.sun.jndi.ldap.LdapCtxFactory";

    public LoginContextSource(String providerUrl) {
        super(providerUrl);

        this.afterPropertiesSet();
    }

    @Override
    protected DirContext getDirContextInstance(Hashtable<String, Object> environment) throws NamingException {
        environment.put(Context.INITIAL_CONTEXT_FACTORY, LDAP_FACTORY);
        // LDAP server
        //environment.put(Context.PROVIDER_URL, ladpUrl);
        environment.put(Context.SECURITY_AUTHENTICATION, "none");
        // 这里只是做一个演示，后面其实并不需要公用的帐号登录
        environment.put(Context.SECURITY_PRINCIPAL, "username");
        environment.put(Context.SECURITY_CREDENTIALS, "password");
        environment.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
        environment.put("java.naming.referral", "follow");

        return super.getDirContextInstance(environment);
    }
}