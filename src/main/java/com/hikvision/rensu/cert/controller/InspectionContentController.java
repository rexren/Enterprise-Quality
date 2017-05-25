package com.hikvision.rensu.cert.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hikvision.rensu.cert.domain.InspectContent;
import com.hikvision.rensu.cert.service.InspectContentService;

@Controller
@RequestMapping("/reports")
public class InspectionContentController {
	@Autowired
	private InspectContentService inspectContentService;

	@RequestMapping(value = "/list.do", method = RequestMethod.GET)
	@ResponseBody
	public List<InspectContent> getContentsList(String inspectionId){
		Long longId = 2475l;
		return inspectContentService.getContentsAll(longId);
	}
    
}
