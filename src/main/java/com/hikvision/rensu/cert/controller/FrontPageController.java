package com.hikvision.rensu.cert.controller;

import com.hikvision.rensu.cert.domain.TypeInspection;
import com.hikvision.rensu.cert.domain.TypeInspectionFrom;
import com.hikvision.rensu.cert.service.TypeInspectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Date;

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

    @PostMapping(value = "/cert/create")
    public String createCert(TypeInspectionFrom typeInspection) {
        TypeInspection ty = typeInspection.transfrom();
        typeInspectionService.save(ty);
        return "redirect:/cert";
    }
}
