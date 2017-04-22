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
    public String indexPage() {
        return "redirect:/cert";
    }

    @RequestMapping("/cert")
    public String certPage(Model model) {
        model.addAttribute("certs", typeInspectionService.getInspections());
        model.addAttribute("name", "陈晓琳");
        return "cert";
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
