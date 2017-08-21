package com.hikvision.ga.hephaestus.site.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hikvision.ga.hephaestus.site.cert.constant.RetStatus;
import com.hikvision.ga.hephaestus.site.controller.vo.UserResult;
import com.hikvision.ga.hephaestus.site.logger.OperationLogIgnore;
import com.hikvision.ga.hephaestus.site.security.domain.SystemUser;
import com.hikvision.ga.hephaestus.site.security.domain.UserRole;
import com.hikvision.ga.hephaestus.site.security.service.SystemUserService;
import com.hikvision.ga.hephaestus.site.security.service.UserRoleService;

/**
 * Created by rensu on 17/4/1.
 */
@Controller
public class HomeController {

  private static final Logger logger = LoggerFactory.getLogger(InspectionController.class);

  @Autowired
  private SystemUserService systemUserService;

  @Autowired
  private UserRoleService userRoleService;

  /**
   * 默认登陆页面
   * 
   * @param
   * @return 登录页
   */
  @OperationLogIgnore
  @RequestMapping(value = "/login", method = RequestMethod.GET)
  public String login() {
    return "login";
  }

  /**
   * 默认登陆成功页面
   * 
   * @param
   * @return 登录页
   */
  @OperationLogIgnore
  @RequestMapping("/login-success")
  public String successLogin() {
    // TODO 写日志
    return "redirect:/index.html";
  }

  /**
   * 获取当前用户
   * 
   * @param request
   * @return user
   */
  @OperationLogIgnore
  @ResponseBody
  @RequestMapping("/user")
  public UserResult user(Principal user) {
    UserResult res = new UserResult();
    res.setName(user.getName());
    SystemUser sysUser;
    try {
      sysUser = systemUserService.findByName(user.getName()).get(0);
      if (sysUser == null) {
        res.setCode(RetStatus.USER_NOT_FOUND.getCode());
        res.setMsg(RetStatus.USER_NOT_FOUND.getInfo());
      } else {
        List<UserRole> roleList = userRoleService.getRoleByUserId(sysUser.getId());
        List<String> roles = new ArrayList<String>();
        for (int i = 0; i < roleList.size(); i++) {
          roles.add(roleList.get(i).getRole());
        }
        res.setRoles(roles);
        res.setId(sysUser.getId());
        res.setCode(RetStatus.SUCCESS.getCode());
        res.setMsg(RetStatus.SUCCESS.getInfo());
      }
    } catch (Exception e) {
      logger.error("", e);
      res.setCode(RetStatus.SYSTEM_ERROR.getCode());
      res.setMsg(e.getMessage());
    }
    return res;
  }

  /**
   * 默认登出页面
   * 
   * @param request
   * @param response
   * @return
   */
  @OperationLogIgnore
  @RequestMapping(value = "/logout")
  public String doLogout(HttpServletRequest request, HttpServletResponse response) {
    //TODO 写日志
    return "logout";
  }

}
