package com.hikvision.rensu.cert.controller;

import com.hikvision.rensu.cert.domain.InspectContent;
import com.hikvision.rensu.cert.service.InspectContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by rensu on 17/5/6.
 */
@RestController
@RequestMapping("/contents")
public class InspectionContentController {

    @Autowired
    private InspectContentService inspectContentService;


    @RequestMapping(method = RequestMethod.POST, value = "/{docSerial}")
    public void saveInspectionDetail(@RequestBody InspectContent content) {
        inspectContentService.save(content);
    }

    @RequestMapping("/{id}")
    public InspectContent getInspectionDetail(@PathVariable Long id) {
        return inspectContentService.get(id);
    }

}
