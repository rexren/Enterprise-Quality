package com.hikvision.ga.hephaestus.site.security;

import java.util.Hashtable;

import javax.naming.AuthenticationException;
import javax.naming.Context;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;

/**
 * @Description: AD域认证
 * @author langyicong
 * @date 2017年12月9日
 */
public class AdDomain {

  public static void main(String[] args) {
    // Test: add your OA user name and password
    int a = ADconnect("xxxxx", "xxxx");
    System.out.println(a);
  }

  /**
   * AD域认证
   * @param username
   * @param password
   * @return 认证结果，0为认证成功
   */
  public static int ADconnect(String username, String password) {
    DirContext ctx = null;
    Hashtable<String, String> HashEnv = new Hashtable<String, String>();
    HashEnv.put(Context.SECURITY_AUTHENTICATION, "none");
    HashEnv.put(Context.SECURITY_PRINCIPAL, username);
    HashEnv.put(Context.SECURITY_CREDENTIALS, password);
    HashEnv.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
    HashEnv.put("com.sun.jndi.ldap.connect.timeout", "3000");
    HashEnv.put(Context.PROVIDER_URL, "ldap://10.1.7.88:389");
    try {
      ctx = new InitialDirContext(HashEnv);
      return 0;
    } catch (AuthenticationException e) {
      e.printStackTrace();
      return 1;
    } catch (javax.naming.CommunicationException e) {
      e.printStackTrace();
      return -1;
    } catch (Exception e) {
      e.printStackTrace();
      return 2;
    } finally {
      if (null != ctx) {
        try {
          ctx.close();
          ctx = null;
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }
  }
}
