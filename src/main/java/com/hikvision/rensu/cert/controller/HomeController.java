package com.hikvision.rensu.cert.controller;

import java.security.Principal;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hikvision.rensu.cert.constant.RetStatus;
import com.hikvision.rensu.cert.domain.SystemUser;
import com.hikvision.rensu.cert.service.SystemUserService;
import com.hikvision.rensu.cert.support.AjaxResult;

/**
 * Created by rensu on 17/4/1.
 */
@Controller
public class HomeController {
	
	private static final Logger logger = LoggerFactory.getLogger(InspectionController.class);

	@Autowired
	private SystemUserService systemUserService;
	/**
	 * 默认登陆页面
	 * @param
	 * @return 登录页
	 */
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login(){
		return "login";
	}
	
	/**
	 * 默认登陆成功页面
	 * @param
	 * @return 登录页
	 */
	@RequestMapping("/login-success")
	public String successLogin(){
		return "redirect:/index.html";
	}
	
	/**
	 * 获取当前用户
	 * @param request
	 * @return user
	 */
	//TODO 另建class，只返回用户名、id和role
	@ResponseBody
	@RequestMapping("/user")
	public AjaxResult<SystemUser> user(Principal user) {
		AjaxResult<SystemUser> res = new AjaxResult<SystemUser>();
		try {
			res.setData(systemUserService.findByName(user.getName()).get(0));
			res.setCode(RetStatus.SUCCESS.getCode());
			res.setMsg(RetStatus.SUCCESS.getInfo());
		} catch (Exception e) {
			logger.error("", e);
			res.setCode(RetStatus.SYSTEM_ERROR.getCode());
			res.setMsg(RetStatus.SYSTEM_ERROR.getInfo());
		}
		return res;
	}

	/**
	 * 默认登出页面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/logout")
	public String doLogout(HttpServletRequest request, HttpServletResponse response){
		return "logout";
	}
	
}
