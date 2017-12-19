package com.hikvision.ga.hephaestus.site.controller;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hikvision.ga.hephaestus.common.constant.RetStatus;
import com.hikvision.ga.hephaestus.common.support.AjaxResult;
import com.hikvision.ga.hephaestus.common.support.UserResult;
import com.hikvision.ga.hephaestus.site.logger.OperationLogIgnore;
import com.hikvision.ga.hephaestus.site.security.domain.HikUser;
import com.hikvision.ga.hephaestus.site.security.service.HikUserService;


/**
 * Created by rensu on 17/4/1.
 */
@Controller
public class HomeController {

  private static final Logger logger = LoggerFactory.getLogger(InspectionController.class);

  @Autowired
  private HikUserService hikUserService;

  /**
   * 默认登陆成功页面
   *
   * @param
   * @return 登录页
   */
  @OperationLogIgnore
  @RequestMapping("/")
  public String successLogin() {
    return "redirect:/index.html";
  }

  /**
   * Ajax获取当前用户信息，密码已隐藏
   *
   * @return principal
   */
  @SuppressWarnings("unchecked")
  @RequestMapping(value = "/user", method = {RequestMethod.GET})
  @ResponseBody
  public AjaxResult<HikUser> getUserInfo(HttpServletRequest request) {
    try{
    Authentication principal = (Authentication) request.getUserPrincipal();
    System.out.println("当前登录用户："+principal.getName());
    HikUser user = hikUserService.getCurrentHikUser();
    return AjaxResult.success(user);
   } catch(Exception e){
     e.printStackTrace();
     logger.error("", e);
     return (AjaxResult<HikUser>) AjaxResult.fail(RetStatus.SYSTEM_ERROR.getCode(), e.getMessage());
    }
  }

}
