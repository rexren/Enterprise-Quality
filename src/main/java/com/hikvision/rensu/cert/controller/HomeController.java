package com.hikvision.rensu.cert.controller;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hikvision.rensu.cert.constant.RetStatus;
import com.hikvision.rensu.cert.support.BaseResult;

/**
 * Created by rensu on 17/4/1.
 */
@Controller
public class HomeController {
	
	/**
	 * 默认登陆页面
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login(@ModelAttribute(value = "error") String error, Model model){
		return "login";
	}
	
	/**
	 * 获取当前用户
	 * @param request
	 * @return user
	 */
	@ResponseBody
	@RequestMapping("/user")
	public Principal user(Principal user) {
		return user;
	}
	  
	/**
	 * 默认登陆页面
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/doLogin", method = RequestMethod.POST)
	public BaseResult doLogin(){
		BaseResult res = new BaseResult();
		res.setCode(RetStatus.USER_AUTH_ERROR.getCode());
		res.setMsg("登录失败");
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
	
	@RequestMapping(value = "/logoutsuccess")
	public String logoutSuccess(){
		return "logout success";
	}
	
}
