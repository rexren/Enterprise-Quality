package com.hikvision.ga.hephaestus.site.security.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.hikvision.ga.hephaestus.site.security.interceptor.HikRoleAccessInterceptor;

/**
 * 配置用户权限拦截器，忽略url同cas
 * 
 * @author langyicong
 *
 */
@Configuration
@PropertySource(value = {"classpath:cas-client.properties"})
@AutoConfigureAfter(WebMvcAutoConfiguration.class)
public class HikRoleAccessConfig extends WebMvcConfigurerAdapter {

  @Value("${hik.ga.cas.ignoreAuthUrls:/*}")
  private String ignoreUrls;

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(new HikRoleAccessInterceptor()).addPathPatterns("/**")
        .excludePathPatterns(ignoreUrls.split(","));
    super.addInterceptors(registry);
    super.addInterceptors(registry);
  }
}
