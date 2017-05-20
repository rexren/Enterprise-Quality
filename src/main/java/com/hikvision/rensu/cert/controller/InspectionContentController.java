package com.hikvision.rensu.cert.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.hikvision.rensu.cert.service.InspectContentService;

@Controller
@RequestMapping("/inspection-content")
public class InspectionContentController {
	@Autowired
	private InspectContentService inspectContentService;
	//TODO: add handlers
}
