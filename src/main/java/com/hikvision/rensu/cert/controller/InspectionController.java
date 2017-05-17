package com.hikvision.rensu.cert.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.hikvision.rensu.cert.domain.TypeInspection;
import com.hikvision.rensu.cert.service.TypeInspectionService;

/**
 * Created by rensu on 17/4/27.
 */
@Controller
@RequestMapping("/inspections")
public class InspectionController {

    @Autowired
    private TypeInspectionService typeInspectionService;

    @RequestMapping(value ="/list.action", method = RequestMethod.GET)
    @ResponseBody
    public Page<TypeInspection> getInspecionListByPage(Integer pageNum, Integer pageSize) {
        return typeInspectionService.getInspectionByPage(pageNum, pageSize);
    }
    
    @RequestMapping(value ="/detail.action", method = RequestMethod.GET)
    @ResponseBody
    public TypeInspection getInspecion(Long id) {
        return typeInspectionService.get(id);
    }

    @RequestMapping(value = "/save.do", method = RequestMethod.POST)
    @ResponseBody
	public String saveInspectionForm(@RequestBody MultipartFile file, HttpServletRequest request) {
    	System.out.println("Start"+request.getParameter("company"));
        String fileName = file.getOriginalFilename(); 
        System.out.println("fileName:  "+file.getOriginalFilename());
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        System.out.println("suffixName: " + suffixName);
        String uploadStatus = "";
		if (null == file || file.isEmpty()) {
			uploadStatus = "Please select the file to upload";
		} else {
			uploadStatus = file.getOriginalFilename() + " file upload success";
			try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(file.getInputStream()));) {
				String line = null;
				while (null != (line = bufferedReader.readLine())){}
					// System.out.println(line);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return uploadStatus;
    }
    
    
    @RequestMapping(method = RequestMethod.POST, value = "/{docSerial}")
    public void saveInspection(@RequestBody TypeInspection typeInspection) {
        typeInspectionService.save(typeInspection);
    }
    
}
