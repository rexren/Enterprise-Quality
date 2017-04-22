package com.hikvision.rensu.cert.controller;

import com.hikvision.rensu.cert.service.TypeInspectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by rensu on 17/4/1.
 */
@Controller
public class FrontPageController {

    @Autowired
    private TypeInspectionService typeInspectionService;

    @RequestMapping("/")
    public String indexPage(Model model) {
        model.addAttribute("certs", typeInspectionService.getInspections());
        model.addAttribute("name", "陈晓琳");
        return "index";
    }

    @RequestMapping("/admin")
    public String adminPage() {
        return "admin";
    }
}
