package com.hikvision.rensu.cert.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;

/** 
 * spring security配置
 * 修改配置后需要重启程序才能生效
 * 
 * @author langyicong
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true) // 允许进入页面方法前检验
public class SecurityConfig extends WebSecurityConfigurerAdapter {


	@Autowired
	private UserDetailsService userDetailsService;// 自定义用户服务

	@Autowired
	public void configAuthentication(AuthenticationManagerBuilder auth) throws Exception {
	}

	/** 定义认证用户信息获取来源，密码校验规则等 */
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService);
		//auth.inMemoryAuthentication().withUser("user").password("123456").roles("USER")
		//  .and().withUser("admin").password("111111").roles("ADMIN"); 
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
				.defaultSuccessUrl("/#/") //TODO 用户被强制登出后再次登入访问什么页面
				.permitAll() 
				//.failureUrl("/login-error").permitAll()
				.and()
			.logout()
				.permitAll();
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		
	}

}
