package com.hikvision.ga.hephaestus.site.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * spring security配置
 *
 * @author langyicong
 */
@Configuration
@EnableWebSecurity // 启用web安全
@EnableGlobalMethodSecurity(prePostEnabled = true) // 允许进入页面方法前检验
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  @Autowired
  private UserDetailsService userDetailsService;// 自定义用户服务

  @Bean
  public AuthenticationProvider authenticationProvider() {
    DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
    authenticationProvider.setUserDetailsService(userDetailsService);
    /** 定义密码加密方式 */
    authenticationProvider.setPasswordEncoder(passwordEncoder());
    return authenticationProvider;
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    PasswordEncoder encoder = new BCryptPasswordEncoder();
    return encoder;
  }

  /** 定义安全策略 */
  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.csrf().disable(); // 禁用csrf
    http.authorizeRequests() // 配置安全策略
        /* ANT通配符说明:? 匹配任何单字符; * 匹配0或者任意数量的字符; ** 匹配0或者更多的目录 */
        .antMatchers("/logout").permitAll().antMatchers("/bootstrap/**/*.*").permitAll()
        .antMatchers("/plugins/**/*.*").permitAll().antMatchers("/dist/**/*.*").permitAll()
        .antMatchers("/scripts/global/login-ctrl.js").permitAll().anyRequest().authenticated() // 其余的所有请求都需要验证
        .and().formLogin().loginPage("/login")
        /*
         * boolean alwaysUse = true - the defaultSuccesUrl should be used after authentication
         * despite if a protected page had been previously visited
         */
        .defaultSuccessUrl("/login-success", true).permitAll().and().logout().permitAll();
  }

  // Whatever the Function name is
  @Autowired
  public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
    // auth.userDetailsService(userDetailsService);
    // auth.authenticationProvider(authenticationProvider());
    // ldap认证
    auth
    .ldapAuthentication()  //FIXME http://blog.csdn.net/t894690230/article/details/52928369
    .userSearchBase("ou=people")
    .userSearchFilter("(uid={0})")
    .groupSearchBase("ou=groups")
    .groupSearchFilter("member={0}")
    .contextSource().url("ldap://10.1.7.88:389");
  }

}
