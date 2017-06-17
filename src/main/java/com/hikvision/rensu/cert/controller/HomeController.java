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
	//TODO 只返回用户名、id和role
	@ResponseBody
	@RequestMapping("/user")
	public Principal user(Principal user) {
		return user;
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
