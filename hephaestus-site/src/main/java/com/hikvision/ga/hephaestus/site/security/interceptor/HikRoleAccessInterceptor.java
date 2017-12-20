package com.hikvision.ga.hephaestus.site.security.interceptor;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.hikvision.ga.hephaestus.common.constant.RetStatus;
import com.hikvision.ga.hephaestus.site.security.annotation.HikRoleAccess;
import com.hikvision.ga.hephaestus.site.security.domain.HikUser;
import com.hikvision.ga.hephaestus.site.security.service.HikUserService;

/**
 * @Description: 自定义权限拦截器
 * @author langyicong
 * @date 2017年12月18日
 */
@Service
public class HikRoleAccessInterceptor extends HandlerInterceptorAdapter {

  @Autowired
  private HikUserService hikUserService;

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
      throws Exception {
    System.out.println(request.getRequestURL());
    HandlerMethod handlerMethod = (HandlerMethod) handler;
    Method method = handlerMethod.getMethod();
    HikRoleAccess access = method.getAnnotation(HikRoleAccess.class);
    // 如果没有写注解,则不控权限
    if (access == null) {
      return true;
    }
    // 如果注解的roles值不为空
    if (access.roles().length > 0) {
      String[] roles = access.roles();
      Set<String> roleSet = new HashSet<>();
      for (String role : roles) {
        roleSet.add(role);
      }
      // 解决service为null无法注入问题
      if (hikUserService == null) {
        WebApplicationContext context =
            WebApplicationContextUtils.getWebApplicationContext(request.getServletContext());
        if (context != null) {
          hikUserService = context.getBean(HikUserService.class);
        }
      }
      HikUser user = hikUserService.getCurrentHikUser(request);
      if (user != null && roleSet.contains(user.getRole())) {
        return true;
      }
    }
    response.setHeader("code", RetStatus.USER_AUTH_ERROR.getCode());
    response.setHeader("msg", RetStatus.USER_AUTH_ERROR.getInfo());
    response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
    return false;
  }

}
