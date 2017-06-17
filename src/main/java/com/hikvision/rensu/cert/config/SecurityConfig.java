package com.hikvision.rensu.cert.config;

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
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true) // 允许进入页面方法前检验
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private UserDetailsService userDetailsService;// 自定义用户服务

	@Bean
	@Autowired
	public AuthenticationProvider authenticationProvider(){
	    DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
	    authenticationProvider.setUserDetailsService(userDetailsService);
	    /** 定义密码加密方式 */
	    authenticationProvider.setPasswordEncoder(passwordEncoder());
	    return authenticationProvider;
	}
	
	@Bean
	@Autowired
    public PasswordEncoder passwordEncoder(){  
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder;  
    }
	
	/* 临时方法 运行加密算法 */
	public static void main(String[] args) {
		PasswordEncoder encoder = new BCryptPasswordEncoder();
		System.out.println(encoder.encode("111111")); 
		System.out.println(encoder.encode("123456")); 
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService);
		auth.authenticationProvider(authenticationProvider());
	}

	/** 定义安全策略 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable();   // 禁用csrf
		http
			.authorizeRequests() // 配置安全策略
				/* ANT通配符说明:? 匹配任何单字符; * 匹配0或者任意数量的字符; ** 匹配0或者更多的目录 */
				.antMatchers("/logout").permitAll()
				.antMatchers("/bootstrap/**/*.*").permitAll()
				.antMatchers("/plugins/**/*.*").permitAll()
				.antMatchers("/dist/**/*.*").permitAll()
				.antMatchers("/scripts/global/login-ctrl.js").permitAll()
				.anyRequest().authenticated() //其余的所有请求都需要验证  
				.and()
			.formLogin()
				.loginPage("/login")
				/* boolean alwaysUse = true - the defaultSuccesUrl should be used after authentication 
				 * despite if a protected page had been previously visited*/
				.defaultSuccessUrl("/login-success", true) 
				.permitAll() 
				.and()
			.logout()
				.permitAll();
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		
	}

}
