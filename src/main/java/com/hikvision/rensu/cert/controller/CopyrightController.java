package com.hikvision.rensu.cert.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.hikvision.rensu.cert.domain.Copyright;
import com.hikvision.rensu.cert.service.CopyrightService;

@Controller
@RequestMapping("/copyright")
public class CopyrightController {

    @Autowired
    private CopyrightService copyrightService;
   
    /**
     * 获取双证列表页
     */
    @RequestMapping(value ="/list.do", method = RequestMethod.GET)
    @ResponseBody
    public Page<Copyright> getListByPage(Integer pageNum, Integer pageSize) {
        return copyrightService.getCopyrightByPage(pageNum, pageSize);
    }
    
    /**
     * 获取双证单条数据
     */
    @RequestMapping(value ="/detail.do", method = RequestMethod.GET)
    @ResponseBody
    public Copyright getCopyright(Long id) {
        return copyrightService.getCopyrightById(id);
    }
    
    /**
     * 保存单条数据
     * TODO 自定义返回类型
     */
    @RequestMapping(value = "/save.do", method = RequestMethod.POST)
    @ResponseBody
	public List<String> saveCopyrightForm(HttpServletRequest request) {
    	System.out.println("Start"+request.getParameter("softwareName"));
        //TODO 保存数据
    	List<String> list = new ArrayList<>();
        list.add("aaa");
        list.add("bbb");
        list.add("ccc");
        return list;
    }
    
    
}
