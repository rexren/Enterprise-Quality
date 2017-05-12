package com.hikvision.rensu.cert.controller;

import com.hikvision.rensu.cert.domain.TypeInspection;
import com.hikvision.rensu.cert.service.TypeInspectionService;

import freemarker.template.utility.NumberUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.NumberUtils;
import org.springframework.web.bind.annotation.*;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.HEAD;

import java.util.List;

/**
 * Created by rensu on 17/4/27.
 */
@Controller
@RequestMapping("/inspections")
public class InspectionController {

    @Autowired
    private TypeInspectionService typeInspectionService;
    
    @RequestMapping(value = "/index", method = {GET, HEAD})
    public String viewIndex(Model model) {
        Page<TypeInspection> pages = typeInspectionService.getInspectionByPage(0, 30);
        model.addAttribute("certs", pages.getContent());
        model.addAttribute("content", pages.getContent());
        model.addAttribute("total", pages.getTotalElements());
        return "inspection/index";
    }

    @RequestMapping("/{id}")
    @ResponseBody
    public TypeInspection getInspecion(@PathVariable Long id) {
        return typeInspectionService.get(id);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/{docSerial}")
    public void saveInspection(@RequestBody TypeInspection typeInspection) {
        typeInspectionService.save(typeInspection);
    }

    @RequestMapping(value ="/list.action", method = RequestMethod.GET)
    @ResponseBody
    public Page<TypeInspection> getInspecionListByPage(Integer pageNum, Integer pageSize) {
        return typeInspectionService.getInspectionByPage(pageNum, pageSize);
    }
}
