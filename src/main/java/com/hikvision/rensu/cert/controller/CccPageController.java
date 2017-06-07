package com.hikvision.rensu.cert.controller;

import java.util.Date;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hikvision.rensu.cert.constant.RetStatus;
import com.hikvision.rensu.cert.domain.CccPage;
import com.hikvision.rensu.cert.service.CccPageService;
import com.hikvision.rensu.cert.support.AjaxResult;
import com.hikvision.rensu.cert.support.BaseResult;
import com.hikvision.rensu.cert.support.ListContent;
import com.hikvision.rensu.cert.support.ListResult;

/**
 * Created by rensu on 17/5/1.
 */
@Controller
@RequestMapping("/ccc")
public class CccPageController {
	private static final Logger logger = LoggerFactory.getLogger(InspectionController.class);
	private final static String SORT_TYPEINSPECTION_UPDATEDATE = "updateDate";

	@Autowired
    private CccPageService cccPageService;
	
	/**
	 * CCC列表页
	 * 
	 * @param pageNum
	 *            页码 默认为第一页
	 * @param pageSize
	 *            页大小 默认20条/页
	 * @param sortBy
	 *            需要倒序排序的字段，默认为更新时间UpdateAt字段
	 * @param direction
	 *            <=0表示降序，>0为升序，默认为降序
	 * @return 包含型检数据对象的返回对象
	 */
	@RequestMapping(value ="/list.do", method = RequestMethod.GET)
    @ResponseBody
    public ListResult getCCCListByPage(Integer pageNum, Integer pageSize, String sortBy, Integer direction) {
		int pn = pageNum == null ? 0 : pageNum.intValue() - 1;
		int ps = pageSize == null ? 20 : pageSize.intValue(); // 默认20条/页
		int dir = direction == null ? 0 : (direction.intValue() <= 0 ? 0 : 1); // 默认为降序
		if (StringUtils.isBlank(sortBy)) {
			sortBy = SORT_TYPEINSPECTION_UPDATEDATE; // 默认按照更新时间倒序
		}
		ListResult res = new ListResult();
		try {
			Page<CccPage> p = cccPageService.getCCCListByPage(pn, ps, sortBy, dir);
			res.setListContent(new ListContent<CccPage>(p.getSize(), p.getTotalElements(), p.getTotalPages(),
					p.getContent()));
			res.setCode(RetStatus.SUCCESS.getCode());
			res.setMsg(RetStatus.SUCCESS.getInfo());
		} catch (Exception e) {
			logger.error("", e);
			res.setCode(RetStatus.SYSTEM_ERROR.getCode());
			res.setMsg(RetStatus.SYSTEM_ERROR.getInfo());
		}
		return res;
    }
	
	 /**
     * 获取CCC单条数据
     */
	@RequestMapping(value ="/detail.do", method = RequestMethod.GET)
    @ResponseBody
    public AjaxResult<CccPage> getCccPage(Long id) {
		AjaxResult<CccPage> res = new AjaxResult<CccPage>();
		if (null == id) {
			res.setCode(RetStatus.FORM_DATA_MISSING.getCode());
			res.setMsg(RetStatus.FORM_DATA_MISSING.getInfo());
		} else {
			try {
				CccPage ccc = cccPageService.getCccPageById(id);
				if(ccc != null){
					res.setData(ccc);
					res.setCode(RetStatus.SUCCESS.getCode());
					res.setMsg(RetStatus.SUCCESS.getInfo());
				} else {
					res.setCode(RetStatus.ITEM_NOT_FOUND.getCode());
					res.setMsg(RetStatus.ITEM_NOT_FOUND.getInfo());
				}
			}catch (Exception e) {
				logger.error("", e.getMessage());
				res.setCode(RetStatus.SYSTEM_ERROR.getCode());
				res.setMsg(RetStatus.SYSTEM_ERROR.getInfo());
			}
		}
		return res;
	}
	
    /**
	 * 新建保存单条数据
	 * 
	 * @param request
	 *            3C编辑页表单内容
	 * @return res 返回状态
	 * @author langyicong
	 */
	@RequestMapping(value = "/save.do", method = RequestMethod.POST)
    @ResponseBody
	public BaseResult saveCccPage(HttpServletRequest request) {
		BaseResult res = new BaseResult();
		/* 表单验证:关键信息docNo不为空 */
		if (StringUtils.isBlank(request.getParameter("docNo"))) {
			res.setCode(RetStatus.FORM_DATA_MISSING.getCode());
			res.setMsg(RetStatus.FORM_DATA_MISSING.getInfo());
			return res;
		}
		/* 表单验证:docNo不能重复 */
		String docNoStripped = StringUtils.trim(request.getParameter("docNo"));
		if (cccPageService.findByDocNo(docNoStripped).size() > 0) {
			res.setCode(RetStatus.DOCNO_DUPLICATED.getCode());
			res.setMsg(RetStatus.DOCNO_DUPLICATED.getInfo());
			return res;
		}
		CccPage c = new CccPage();
		try {
			/* 页面参数组装成CccPage实体 */
			c = setCccProperties(request, c);
			cccPageService.saveCccPage(c);
			res.setCode(RetStatus.SUCCESS.getCode());
			res.setMsg(RetStatus.SUCCESS.getInfo());
		} catch (Exception e) {
			logger.error("", e);
			res.setCode(RetStatus.SYSTEM_ERROR.getCode());
			res.setMsg(RetStatus.SYSTEM_ERROR.getInfo());
			return res;
		}
		
    	return res;
	}
	
	/**
	 * 更新单条数据
	 * 
	 * @param request
	 *            CCC编辑页表单内容
	 * @return res 返回状态
	 * @author langyicong
	 */
    @RequestMapping(value = "/update.do", method = RequestMethod.POST)
    @ResponseBody
    public BaseResult updateCccPage(HttpServletRequest request) {
    	BaseResult res = new BaseResult();
    	/* if null id or null entity */
		if (null == request || StringUtils.isBlank(request.getParameter("id"))) {
			res.setCode(RetStatus.FORM_DATA_MISSING.getCode());
			res.setMsg(RetStatus.FORM_DATA_MISSING.getInfo());
			return res;
		}
		/* 查询对应的实体，并且将页面数据更新实体内容 */
		Long requestId = NumberUtils.toLong(request.getParameter("id"));
		CccPage c = null;
		try {
			c = cccPageService.getCccPageById(requestId);
			if(null == c){
				res.setCode(RetStatus.ITEM_NOT_FOUND.getCode());
				res.setMsg(RetStatus.ITEM_NOT_FOUND.getInfo());
				return res;
			}
			// 编辑后docNo无重复
			String docNoStripped = StringUtils.trim(request.getParameter("docNo"));
			if (cccPageService.findByDocNo(docNoStripped).size() > 0
					&& !StringUtils.equals(c.getDocNo(), docNoStripped)) {
				res.setCode(RetStatus.DOCNO_DUPLICATED.getCode());
				res.setMsg(RetStatus.DOCNO_DUPLICATED.getInfo());
				return res;
			};
			c = setCccProperties(request, c);
			cccPageService.saveCccPage(c);
			res.setCode(RetStatus.SUCCESS.getCode());
			res.setMsg(RetStatus.SUCCESS.getInfo());
		} catch (Exception e) {
			logger.error("", e);
			res.setCode(RetStatus.SYSTEM_ERROR.getCode());
			res.setMsg(RetStatus.SYSTEM_ERROR.getInfo());
			return res;
		}
    	return res;
    }
    
	/**
	 * 将HttpServletRequest请求体（页面参数）组装成CccPage实体
	 * 
	 * @param request
	 *            页面表单数据
	 * @param c  初始的CccPage实体，可为空（新建）或者含id（更新）
	 * @return c 转换后的CccPage实体，不包括创建时间
	 * @throws Exception
	 */
	private CccPage setCccProperties(HttpServletRequest request, CccPage c) throws Exception{
		if(null == c){
			c = new CccPage();
		}
		/* 新建 */
		if(null == c.getId() || c.getId()<0){
			c.setCreateDate(new Date());
		}
		/* setXxxxDate */
		c.setExpiryDate(NumberUtils.toLong(request.getParameter("expiryDate"))>0?new Date(NumberUtils.toLong(request.getParameter("expiryDate"))):null);
		c.setAwardDate(NumberUtils.toLong(request.getParameter("awardDate"))>0?new Date(NumberUtils.toLong(request.getParameter("awardDate"))):null);
		c.setUpdateDate(new Date());
		/* set other properties */
		c.setDocNo(StringUtils.trim(request.getParameter("docNo")));
		c.setProductName(StringUtils.trim(request.getParameter("productName")));
		c.setModel(StringUtils.trim(request.getParameter("model")));
		c.setOrganization(StringUtils.trim(request.getParameter("organization")));
		c.setRemarks(StringUtils.trim(request.getParameter("remarks")));
		c.setUrl(StringUtils.trim(request.getParameter("url")));
		c.setOperator("TESTER");
		return c;
	}
	
	/**
	 * 关键词搜索CCC列表
	 * 
	 * @param pageNum
	 *            页码 默认为第一页
	 * @param pageSize
	 *            页大小 默认20条/页
	 * @param sortBy
	 *            需要倒序排序的字段，默认为更新时间UpdateAt字段
	 * @param direction
	 *            <=0表示降序，>0为升序，默认为降序
	 * @return 包含型检数据对象的返回对象
	 */
	@RequestMapping(value ="/search.do", method = RequestMethod.GET)
    @ResponseBody
    public ListResult searchCCCList(Integer field, String keyword, Integer pageNum, Integer pageSize, String sortBy, Integer direction) {
		int pn = pageNum == null ? 0 : pageNum.intValue() - 1;
		int ps = pageSize == null ? 20 : pageSize.intValue(); // 默认20条/页
		int dir = direction == null ? 0 : (direction.intValue() <= 0 ? 0 : 1); // 默认为降序
		if (StringUtils.isBlank(sortBy)) {
			sortBy = SORT_TYPEINSPECTION_UPDATEDATE; // 默认按照更新时间倒序
		}
		String fieldName;
		if(field == null){ 
			fieldName = "";
		}else{
			switch (field) {
				case 1: fieldName="docNo";break;
				case 2: fieldName="model";break;
				case 3: fieldName="productName";break;
				case 4: fieldName="remarks";break;
				default: fieldName="";break;
			}
			
		}		
		if (StringUtils.isBlank(keyword)) {
			keyword = ""; // 默认按照更新时间倒序
		}
		//TODO 空格分词
		String[] keywordList = StringUtils.split(keyword);
		ListResult res = new ListResult();
		try {
			//TODO 搜索函数 Integer pageNum, Integer pageSize, String sortBy, Integer direction
			Page<CccPage> p = cccPageService.searchCccListByPage(fieldName, keywordList, pn, ps, sortBy, dir);
			res.setListContent(new ListContent<CccPage>(p.getSize(), p.getTotalElements(), p.getTotalPages(),
					p.getContent()));
			res.setCode(RetStatus.SUCCESS.getCode());
			res.setMsg(RetStatus.SUCCESS.getInfo());
		} catch (Exception e) {
			logger.error("", e.getMessage());
			res.setCode(RetStatus.SYSTEM_ERROR.getCode());
			res.setMsg(RetStatus.SYSTEM_ERROR.getInfo());
		}
		return res;
    }
	
}
