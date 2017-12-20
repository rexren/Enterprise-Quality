package com.hikvision.ga.hephaestus.site.security.service;

import java.security.Principal;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hikvision.ga.hephaestus.site.security.domain.HikUser;
import com.hikvision.ga.hephaestus.site.security.repository.HikUserRepository;

/**
 * @Description: 用户管理
 * @author langyicong
 * @date 2017年12月18日
 */
@Service
@Transactional
public class HikUserService {

  @Autowired
  private HikUserRepository hikUserRepository;

  public List<HikUser> getUserByName(String userName) {
    return hikUserRepository.findByUserName(userName);
  }

  public HikUser addUser(HikUser user) {
    return hikUserRepository.save(user);
  }

  /**
   * 通过Spring Security获取当前用户名称
   * @return 用户名称
   */
  private String getCurrentUsername() {
    SecurityContext context = SecurityContextHolder.getContext();
    if(context==null)
      return null;
    Object principal = context.getAuthentication().getPrincipal();
    if (principal instanceof UserDetails) {
      return ((UserDetails) principal).getUsername();
    }
    if (principal instanceof Principal) {
      return ((Principal) principal).getName();
    }
    return String.valueOf(principal);
  }
  
  /**
   * 通过HttpServletRequest获取当前用户名称
   * @param request http请求
   * @return 用户名称
   */
  public String getCurrentUsername(HttpServletRequest request) {
    return getCurrentHikUser(request).getUserName();
  }
  
  /**
   * 通过Spring Security获取当前用户实体
   * @return HikUser
   */
  public HikUser getCurrentHikUser() {
    List<HikUser> users = hikUserRepository.findByUserName(getCurrentUsername());
    HikUser user = users.size() == 0 ? null : users.get(0);
    return user;
  }

  /**
   * 通过HttpServletRequest获取当前用户实体
   * @param request http请求
   * @return HikUser
   */
  public HikUser getCurrentHikUser(HttpServletRequest request) {
    if (null == request.getSession().getAttribute("SPRING_SECURITY_CONTEXT")) {
      return null;
    }
    SecurityContextImpl securityContextImpl =
        (SecurityContextImpl) request.getSession().getAttribute("SPRING_SECURITY_CONTEXT");
    Authentication authentication = securityContextImpl.getAuthentication();
    String userName = authentication.getName();
    List<HikUser> users = hikUserRepository.findByUserName(userName);
    HikUser user = users.size() == 0 ? null : users.get(0);
    return user;
  }

}
