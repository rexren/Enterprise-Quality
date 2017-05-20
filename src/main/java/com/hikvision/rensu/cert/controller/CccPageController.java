package com.hikvision.rensu.cert.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hikvision.rensu.cert.domain.CccPage;
import com.hikvision.rensu.cert.service.CccPageService;

/**
 * Created by rensu on 17/5/1.
 */
@Controller
@RequestMapping("/ccc")
public class CccPageController {
	
	@Autowired
    private CccPageService cccPageService;
	
	@RequestMapping(value ="/list.action", method = RequestMethod.GET)
    @ResponseBody
    public Page<CccPage> getCCCListByPage(Integer pageNum, Integer pageSize) {
        return cccPageService.getCCCListByPage(pageNum, pageSize);
    }
}
