package com.hikvision.rensu.cert.controller;

import com.hikvision.rensu.cert.domain.SearchContent;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by rensu on 17/4/1.
 */
@Controller
public class FrontPageController {


    @RequestMapping("/")
    public String indexPage() {
    	return "redirect:/inspections/index";
    }


    @RequestMapping("/copyright")
    public String copyrightPage(Model model) {
        model.addAttribute("name", "陈晓琳");
        return "copyright";
    }

    @RequestMapping("/ccc")
    public String CCCPage(Model model) {
        model.addAttribute("name", "陈晓琳");
        return "ccc";
    }

}
