package com.hikvision.ga.hephaestus.site.security.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Description: 权限校验的注解
 * @author langyicong
 * @date 2017年12月18日
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface HikRoleAccess {
  
  String[] roles() default {};
  
}