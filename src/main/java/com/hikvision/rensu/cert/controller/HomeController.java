package com.hikvision.rensu.cert.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.hikvision.rensu.cert.constant.StaticParams;
import com.hikvision.rensu.cert.domain.SystemUser;
import com.hikvision.rensu.cert.service.SystemUserService;
import com.hikvision.rensu.cert.service.UserRoleService;

/**
 * Created by rensu on 17/4/1.
 */
@Controller
public class HomeController {
	
	@Autowired
	private SystemUserService systemUserService;
	
	/**
	 * 默认登陆页面
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login(){
		return "login";
	}
	/*public ModelAndView login(HttpServletRequest request, HttpServletResponse response){
		ModelAndView view = new ModelAndView();
		view.setViewName("login");
		return view;
	}*/
	
	/**
	 * 登陆接口，不能被拦截
	 * @param request
	 * @param response
	 * @param name
	 * @param password
	 * @return
	 */
	//TODO debug
	@RequestMapping(value ="/doLogin", method = RequestMethod.POST)
	public String doLogin(String username, String password){
		/*验证user*/
		System.out.println("Logging in --> " + username + " : " + password);
		SystemUser user;
		try {
			user = systemUserService.findByName(username).get(0);
		} catch (Exception e) {
			return "system error";
		}
		if(user == null){
			return "No User";
		}
		return "redirect:/index.html";
	}
	
	/*@RequestMapping(value = StaticParams.PATH.API+"doLogin/{name}/{password}")
	public String doLogin(HttpServletRequest request, HttpServletResponse response,
			@PathVariable("name") String name,
			@PathVariable("password") String password){
		System.out.println("Logging in --> " + name + " : " + password);
		SystemUser user;
		try {
			user = systemUserService.findByName(name).get(0);
		} catch (Exception e) {
			return "system error";
		}
		if(user == null){
			return "No User";
		}
		return "redirect:/index.html";
	}*/
	
	@RequestMapping(value = "/logout")
	public String doLogout(HttpServletRequest request, HttpServletResponse response){
		return "logout";
	}
	
	@RequestMapping(value = "/logoutsuccess")
	public String logoutSuccess(){
		return "logout success";
	}
	
	@RequestMapping("/")
	public String indexPage() {
		return "redirect:/index.html";
	}
}
