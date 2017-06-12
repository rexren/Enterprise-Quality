package com.hikvision.rensu.cert.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by rensu on 17/4/1.
 */
@Controller
public class HomeController {
	
	/**
	 * 默认登陆页面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login(@ModelAttribute(value = "error") String error, Model model){
		return "login";
	}
	
	//TODO 登录失败页面 @ResponseBody
	
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
