package com.hikvision.rensu.cert.controller;

import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.HEAD;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

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
    
    @RequestMapping(value = "/index", method = {GET, HEAD})
    public String viewIndex(Model model) {
        Page<TypeInspection> pages = typeInspectionService.getInspectionByPage(0, 30);
        model.addAttribute("certs", pages.getContent());
        model.addAttribute("content", pages.getContent());
        model.addAttribute("total", pages.getTotalElements());
        return "inspection/index";
    }

    @RequestMapping(value ="/detail.action", method = RequestMethod.GET)
    @ResponseBody
    public TypeInspection getInspecion(Long id) {
        return typeInspectionService.get(id);
    }

    @RequestMapping(value = "/save.do")  
//    public String upload(@RequestParam(value = "file", required = false) MultipartFile file, HttpServletRequest request) {
    public String upload(HttpServletRequest request) {
        System.out.println("开始"+request.getParameter("company"));
        if(request instanceof MultipartHttpServletRequest){
        	MultiValueMap<String, MultipartFile> fileMap = ((MultipartHttpServletRequest)request).getMultiFileMap();
        	if(null != fileMap){
        		for(String key : fileMap.keySet()){
					List<MultipartFile> files = fileMap.get(key);
					System.out.println(files.size());
        		}
        	}
        	
        }
        return "上传失败"; 
//        String path = request.getSession().getServletContext().getRealPath("upload");  
//        System.out.println("存储路径： "+path);
        /*
        String fileName = file.getOriginalFilename(); 
        System.out.println("文件名为： "+fileName);
        // 获取文件的后缀名
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        System.out.println("上传的后缀名为：" + suffixName);
        // 文件上传后的路径
        String filePath = "E://test//";
        // 解决中文问题，liunx下中文路径，图片显示问题
        // fileName = UUID.randomUUID() + suffixName;
        File targetFile = new File(filePath + fileName);
        if(!targetFile.exists()){  
            targetFile.mkdirs();  
        }  
        try {  
            file.transferTo(targetFile);
            return "上传成功";
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "上传失败";  
        */
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
    
    /*    
    test
    */	
    @RequestMapping(value = "/uploadtest11.do", method = RequestMethod.POST)
    public String upload11(@RequestParam(value="uploadedfile") MultipartFile uploadedfile) {
        System.out.println("开始");
        if (null == uploadedfile || uploadedfile.isEmpty()) {
			return "Please select the file to upload";
		}
        String fileName = uploadedfile.getOriginalFilename(); 
        System.out.println("文件名为： "+fileName);
        // 获取文件的后缀名
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        System.out.println("上传的后缀名为：" + suffixName);
        // 文件上传后的路径
        String filePath = "C://test//";
        File targetFile = new File(filePath + fileName);
        if(!targetFile.exists()){  
            targetFile.mkdirs();  
        }  
        try {  
        	uploadedfile.transferTo(targetFile);
            return "上传成功";
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "上传失败";
    }
}
