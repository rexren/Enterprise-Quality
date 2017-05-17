package com.hikvision.rensu.cert.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by rensu on 17/4/1.
 */
@Controller
public class FrontPageController {

    @RequestMapping("/")
    public String indexPage() {
    	return "redirect:/index.html";
    }

}
