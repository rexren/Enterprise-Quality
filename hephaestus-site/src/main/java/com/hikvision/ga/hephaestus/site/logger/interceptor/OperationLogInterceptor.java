package com.hikvision.ga.hephaestus.site.logger.interceptor;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.util.StringUtils;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.hikvision.ga.hephaestus.site.logger.OperationLog;
import com.hikvision.ga.hephaestus.site.logger.OperationLogHolder;
import com.hikvision.ga.hephaestus.site.logger.OperationLogIgnore;
import com.hikvision.ga.hephaestus.site.logger.service.OperationLogService;


public class OperationLogInterceptor extends HandlerInterceptorAdapter {

  @Autowired
  private OperationLogService operationLogService;
  
  public OperationLogInterceptor() {
    
  }

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
      throws Exception {
    OperationLog OperationLog = new OperationLog();
    OperationLog.setUserId("0"); // 获取当前用户id
    OperationLog.setUserName(getUserName(request)); // 获取当前用户名
    OperationLog.setIp(getRemoteIpAddress(request)); //获取用户ip
    OperationLog.setUserAgent(getUserAgent(request)); //记录当前用户的客户端类型
    OperationLog.setCreateTime(new Date());  //记录时间
    OperationLogHolder.setOperationLog(OperationLog); //存入ThreadLocal
    return super.preHandle(request, response, handler);
  }

  @Override
  public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
      Object handler, Exception ex) throws Exception {
    if (!(handler instanceof HandlerMethod)) {
      return;
    }
    HandlerMethod method = (HandlerMethod) handler;
    // 获取类上的注解
    OperationLogIgnore classAnnotation =
        method.getMethod().getDeclaringClass().getAnnotation(OperationLogIgnore.class);
    // 获取方法上的注解
    OperationLogIgnore methodAnnotation = method.getMethodAnnotation(OperationLogIgnore.class);

    OperationLog OperationLog = OperationLogHolder.getOperationLog();
    // 类和方法上都没有配置ignore的注解，则记录日志；
    if (null == classAnnotation && null == methodAnnotation && null != OperationLog) {
      // 开始记录日志
      // 如果必填信息没有填写，则拦截的URL以便定位日志来源模块
      if (StringUtils.isEmpty(OperationLog.getAct())
          && StringUtils.isEmpty(OperationLog.getContent())) {
        OperationLog.setContent(request.getRequestURL().toString());
      }

      if (operationLogService == null) {
        ApplicationContext context =
            WebApplicationContextUtils.getWebApplicationContext(request.getServletContext());
        if (context != null) {
          operationLogService = context.getBean(OperationLogService.class);
        }
      }
      if (operationLogService != null) {
        operationLogService.save(OperationLog);
      }

    }
    super.afterCompletion(request, response, handler, ex);
  }
  
  private static String getUserName(HttpServletRequest request){
    System.out.println(request.getRequestURL());
    if(null == request.getSession().getAttribute("SPRING_SECURITY_CONTEXT")){
      return "";
    }
    SecurityContextImpl securityContextImpl = (SecurityContextImpl) request.getSession().getAttribute("SPRING_SECURITY_CONTEXT");
    String userName = securityContextImpl.getAuthentication().getName();
    System.out.println("Username: " + userName);
    return userName;
  }

  private static String getUserAgent(HttpServletRequest request){
    String ua = request.getHeader("User-Agent");
    return ua;
  }

  /**
   * 获取远程的IP地址，需要找到代理的地址
   * 
   * @param request
   * @return
   */
  private static String getRemoteIpAddress(HttpServletRequest request) {
    if (request == null) {
      return null;
    }
    /* 过滤代理服务器的ip地址 */
    String s = request.getHeader("X-Forwarded-For");
    if (s == null || s.length() == 0 || "unknown".equalsIgnoreCase(s)) {
      s = request.getHeader("Proxy-Client-IP");
    }
    if (s == null || s.length() == 0 || "unknown".equalsIgnoreCase(s)) {
      s = request.getHeader("WL-Proxy-Client-IP");
    }
    if (s == null || s.length() == 0 || "unknown".equalsIgnoreCase(s)) {
      s = request.getHeader("HTTP_CLIENT_IP");
    }
    if (s == null || s.length() == 0 || "unknown".equalsIgnoreCase(s)) {
      s = request.getHeader("HTTP_X_FORWARDED_FOR");
    }
    if (s == null || s.length() == 0 || "unknown".equalsIgnoreCase(s)) {
      s = request.getRemoteAddr();
    }
    /* 过滤本服务的ip地址 */
    if ("127.0.0.1".equals(s) || "0:0:0:0:0:0:0:1".equals(s)) {
      try {
        s = InetAddress.getLocalHost().getHostAddress();
      } catch (UnknownHostException unknownhostexception) {
      }
    }
    return s;
  }
  
}
