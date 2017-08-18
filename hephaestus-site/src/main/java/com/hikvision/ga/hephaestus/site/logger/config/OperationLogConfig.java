package com.hikvision.ga.hephaestus.site.logger.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.hikvision.ga.hephaestus.site.logger.interceptor.OperationLogInterceptor;

@Configuration
public class OperationLogConfig extends WebMvcConfigurerAdapter {
  
  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(new OperationLogInterceptor());
    super.addInterceptors(registry);
  }
}
