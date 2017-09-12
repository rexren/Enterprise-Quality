package com.hikvision.ga.hephaestus.site.logger.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.hikvision.ga.hephaestus.site.logger.interceptor.OperationLogInterceptor;

/**
 * 配置日志拦截器
 * @author langyicong
 *
 */
@Configuration
@AutoConfigureAfter(WebMvcAutoConfiguration.class)
public class OperationLogConfig extends WebMvcConfigurerAdapter {
  
  @Value("${hik.ga.operationlog.ignoreUrls:/*}")
  private String ignoreUrls;
  
  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(new OperationLogInterceptor())
    .addPathPatterns("/**")
    .excludePathPatterns(ignoreUrls.split(","));  //设置忽略的url
    super.addInterceptors(registry);
  }
}
