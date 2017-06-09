package com.hikvision.rensu.cert.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)//允许进入页面方法前检验
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	
	/**定义认证用户信息获取来源，密码校验规则等*/  
    @Override  
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {  
        //inMemoryAuthentication 从内存中获取  
        auth.inMemoryAuthentication().withUser("user").password("123456").roles("USER")
        	.and().withUser("admin").password("111111").roles("ADMIN");  
          
        //jdbcAuthentication从数据库中获取，但是默认是以security提供的表结构  
        //usersByUsernameQuery 指定查询用户SQL  
        //authoritiesByUsernameQuery 指定查询权限SQL  
        //auth.jdbcAuthentication().dataSource(dataSource).usersByUsernameQuery(query).authoritiesByUsernameQuery(query);  
          
        //注入userDetailsService，需要实现userDetailsService接口  
        //auth.userDetailsService(userDetailsService);  
    }  
      
    /**定义安全策略*/  
    @Override  
    protected void configure(HttpSecurity http) throws Exception {  
    	http.csrf().disable(); //禁用csrf
        http.authorizeRequests()  //配置安全策略  
            .antMatchers("/","/hello").permitAll() //定义"/","/hello"请求不需要验证  
            .anyRequest().authenticated() //其余的所有请求都需要验证  
            .and()  
        .logout()  
            .permitAll()//定义logout不需要验证  
            .and()  
        .formLogin();//使用form表单登录  
    }  
      
}
