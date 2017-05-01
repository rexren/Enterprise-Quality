package com.hikvision.rensu.cert.controller;

import com.hikvision.rensu.cert.domain.InspectContent;
import com.hikvision.rensu.cert.domain.TypeInspection;
import com.hikvision.rensu.cert.domain.TypeInspectionFrom;
import com.hikvision.rensu.cert.service.InspectContentService;
import com.hikvision.rensu.cert.service.TypeInspectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by rensu on 17/4/27.
 */
@Controller
@RequestMapping("/inspect")
public class InspectionController {

    @Autowired
    private TypeInspectionService typeInspectionService;

    @Autowired
    private InspectContentService inspectContentService;

    @RequestMapping("/index")
    public String list(Model model) {
        Page<TypeInspection> pages = typeInspectionService.getInspectionByPage(0, 30);
        model.addAttribute("certs", pages.getContent());
        model.addAttribute("content", pages.getContent());
        return "inspection/index";
    }

    @PostMapping(value = "/create")
    public String save(TypeInspectionFrom typeInspection) {
        TypeInspection ty = typeInspection.transfrom();
        typeInspectionService.save(ty);
        return "redirect:/inspect";
    }

    @ResponseBody
    public TypeInspection get(Long id) {
        return typeInspectionService.get(id);
    }


    public void saveInspectionDetail(InspectContent content) {
        inspectContentService.save(content);
        return;
    }

    @ResponseBody
    public InspectContent getInspectionDetail(Long id) {
        return inspectContentService.get(id);
    }

}
