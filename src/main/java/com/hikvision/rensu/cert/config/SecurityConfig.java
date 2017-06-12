package com.hikvision.rensu.cert.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.hikvision.rensu.cert.constant.StaticParams;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true) // 允许进入页面方法前检验
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	//@Autowired
	//private HikAuthenticationProvider provider;// 自定义验证

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
		http.csrf().disable(); // 禁用csrf
		http.authorizeRequests() // 配置安全策略
				.antMatchers(StaticParams.PATHREGX.CSS, StaticParams.PATHREGX.JS,StaticParams.PATHREGX.IMG)
				.permitAll()// 无需访问权限
				.anyRequest().authenticated() //其余的所有请求都需要验证  
				//.antMatchers(StaticParams.PATHREGX.ADMIN).hasAuthority(StaticParams.USERROLE.ROLE_ADMIN)// admin角色访问权限
				//.antMatchers(StaticParams.PATHREGX.VIEW).hasAuthority(StaticParams.USERROLE.ROLE_USER)// user角色访问权限
				//.anyRequest().authenticated()// all others request authentication
				.and().formLogin().loginPage("/login").failureUrl("/login?error=true").permitAll()
				.and().logout().permitAll();
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		// 将验证过程交给自定义验证工具
		// auth.authenticationProvider(provider);
	}

}
