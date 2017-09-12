package com.hikvision.ga.hephaestus.site.controller;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import com.hikvision.ga.hephaestus.cert.Copyright;
import com.hikvision.ga.hephaestus.common.RetStatus;
import com.hikvision.ga.hephaestus.cert.service.CopyrightService;
import com.hikvision.ga.hephaestus.site.cert.support.AjaxResult;
import com.hikvision.ga.hephaestus.site.cert.support.BaseResult;
import com.hikvision.ga.hephaestus.site.cert.support.ListContent;
import com.hikvision.ga.hephaestus.site.cert.support.ListResult;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hikvision.ga.hephaestus.site.user.SystemUserService;

@Controller
@RequestMapping("/copyright")
public class CopyrightController {

	private static final Logger logger = LoggerFactory.getLogger(InspectionController.class);
	
	private final static String SORTBY_CRDATE = "crDate";

	@Autowired
	private CopyrightService copyrightService;
	
	@Autowired
	private SystemUserService systemUserService;

	/**
	 * 获取双证列表页
	 * 
	 * @param pageNum
	 *            页码 默认为第一页
	 * @param pageSize
	 *            页大小 默认20条/页
	 * @param sortBy
	 *            需要倒序排序的字段，默认为更新时间UpdateAt字段
	 * @param direction
	 *            <=0表示降序，>0为升序，默认为降序
	 * @return 包含双证数据对象的返回对象
	 */
	@RequestMapping(value = "/list.do", method = RequestMethod.GET)
	@ResponseBody
	public ListResult getListByPage(Integer pageNum, Integer pageSize, String sortBy, Integer direction) {
		int pn = pageNum == null ? 0 : pageNum.intValue() - 1;
		int ps = pageSize == null ? 20 : pageSize.intValue(); // 默认20条/页
		int dir = direction == null ? 0 : (direction.intValue() <= 0 ? 0 : 1); // 默认为降序
		if (StringUtils.isBlank(sortBy)) {
			sortBy = SORTBY_CRDATE; // 默认按照软著签发日期和id倒序
		}
		ListResult res = new ListResult();
		try {
			Page<Copyright> p = copyrightService.getCopyrightByPage(pn, ps, sortBy, dir);
			if(null!=p){
				res.setListContent(new ListContent(p.getSize(), p.getTotalElements(), p.getTotalPages(), p.getContent()));
			}
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
	 * 删除单条型检数据（不包括列表）
	 * 
	 * @param id
	 *            型检id
	 * @return 操作状态
	 */
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "/delete.do", method = RequestMethod.GET)
	@ResponseBody
	public BaseResult deleteCopyrightById(Long id) {
		BaseResult res = new BaseResult();

		if (null == id) {
			res.setCode(RetStatus.PARAM_ILLEGAL.getCode());
			res.setMsg(RetStatus.PARAM_ILLEGAL.getInfo());
			return res;
		}
		try {
			copyrightService.deleteCopyrightById(id);
			res.setCode(RetStatus.SUCCESS.getCode());
			res.setMsg(RetStatus.SUCCESS.getInfo());
		} catch (IllegalArgumentException e) {
			logger.error("", e);
			res.setCode(RetStatus.USER_NOT_FOUND.getCode());
			res.setMsg(RetStatus.USER_NOT_FOUND.getInfo());
		} catch (Exception e) {
			logger.error("", e);
			res.setCode(RetStatus.SYSTEM_ERROR.getCode());
			res.setMsg(RetStatus.SYSTEM_ERROR.getInfo());
		}

		return res;
	}

	/**
	 * 获取双证单条数据
	 * 
	 * @param id
	 *            双证id
	 * @return 包含双证数据对象的返回对象
	 */
	@RequestMapping(value = "/detail.do", method = RequestMethod.GET)
	@ResponseBody
	public AjaxResult<Copyright> getCopyright(Long id) {
		AjaxResult<Copyright> res = new AjaxResult<Copyright>();
		if (null == id) {
			res.setCode(RetStatus.FORM_DATA_MISSING.getCode());
			res.setMsg(RetStatus.FORM_DATA_MISSING.getInfo());
		} else {
			try {
				Copyright c = copyrightService.getCopyrightById(id);
				if (c != null) {
					res.setData(c);
					res.setCode(RetStatus.SUCCESS.getCode());
					res.setMsg(RetStatus.SUCCESS.getInfo());
				} else {
					res.setCode(RetStatus.ITEM_NOT_FOUND.getCode());
					res.setMsg(RetStatus.ITEM_NOT_FOUND.getInfo());
				}
			} catch (Exception e) {
				logger.error("", e);
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
	 *            双证编辑页表单内容
	 * @return res 返回状态
	 * @author langyicong
	 */
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "/save.do", method = RequestMethod.POST)
	@ResponseBody
	public BaseResult saveCopyright(HttpServletRequest request) {
		BaseResult res = new BaseResult();
		/* 表单验证:关键信息softwareName不为空 */
		if (StringUtils.isBlank(request.getParameter("softwareName"))) {
			res.setCode(RetStatus.FORM_DATA_MISSING.getCode());
			res.setMsg(RetStatus.FORM_DATA_MISSING.getInfo());
			return res;
		}
		/* 表单验证:softwareName不能重复 */
		String nameStripped = StringUtils.trim(request.getParameter("softwareName"));
		if (copyrightService.findBySoftwareName(nameStripped).size() > 0) {
			res.setCode(RetStatus.NAME_DUPLICATED.getCode());
			res.setMsg(RetStatus.NAME_DUPLICATED.getInfo());
			return res;
		}
		Copyright c = new Copyright();
		try {
			/* 页面参数组装成Copyright实体 */
			c = setCopyrightProperties(request, c);
			copyrightService.saveCopyright(c);
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
	 *            双证编辑页表单内容
	 * @return res 返回状态
	 * @author langyicong
	 */
	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "/update.do", method = RequestMethod.POST)
	@ResponseBody
	public BaseResult updateCopyright(HttpServletRequest request) {
		BaseResult res = new BaseResult();
		/* if null id or null entity */
		if (null == request || StringUtils.isBlank(request.getParameter("id"))) {
			res.setCode(RetStatus.FORM_DATA_MISSING.getCode());
			res.setMsg(RetStatus.FORM_DATA_MISSING.getInfo());
			return res;
		}

		/* 查询对应的实体，并且将页面数据更新实体内容 */
		Long requestId = NumberUtils.toLong(request.getParameter("id"));
		Copyright c = null;
		try {
			c = copyrightService.getCopyrightById(requestId);
			if (null == c) {
				res.setCode(RetStatus.ITEM_NOT_FOUND.getCode());
				res.setMsg(RetStatus.ITEM_NOT_FOUND.getInfo());
				return res;
			}
			// 编辑后softwareName无重复
			String nameStripped = StringUtils.trim(request.getParameter("softwareName"));
			if (copyrightService.findBySoftwareName(nameStripped).size() > 0
					&& !StringUtils.equals(c.getSoftwareName(), nameStripped)) {
				res.setCode(RetStatus.NAME_DUPLICATED.getCode());
				res.setMsg(RetStatus.NAME_DUPLICATED.getInfo());
				return res;
			}
			c = setCopyrightProperties(request, c);
			copyrightService.saveCopyright(c);
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
	 * 将HttpServletRequest请求体（页面参数）组装成Copyright实体
	 * 
	 * @param request
	 *            页面表单数据
	 * @param c
	 *            初始的Copyright实体，可为空（新建）或者含id（更新）
	 * @return c 转换后的Copyright实体，不包括创建时间
	 * @throws Exception
	 */
	private Copyright setCopyrightProperties(HttpServletRequest request, Copyright c) throws Exception {
		if (null == c) {
			c = new Copyright();
		}
		/* 新建 */
		if (null == c.getId() || c.getId() < 0) {
			c.setCreateDate(new Date());
		}
		/* setXxxxDate */
		c.setCrDate(NumberUtils.toLong(request.getParameter("crDate")) > 0
				? new Date(NumberUtils.toLong(request.getParameter("crDate"))) : null);
		c.setRgDate(NumberUtils.toLong(request.getParameter("rgDate")) > 0
				? new Date(NumberUtils.toLong(request.getParameter("rgDate"))) : null);
		c.setRgExpiryDate(NumberUtils.toLong(request.getParameter("rgExpiryDate")) > 0
				? new Date(NumberUtils.toLong(request.getParameter("rgExpiryDate"))) : null);
		c.setEpDate(NumberUtils.toLong(request.getParameter("epDate")) > 0
				? new Date(NumberUtils.toLong(request.getParameter("epDate"))) : null);
		c.setCdDate(NumberUtils.toLong(request.getParameter("cdDate")) > 0
				? new Date(NumberUtils.toLong(request.getParameter("cdDate"))) : null);
		c.setUpdateDate(new Date());
		/* set other properties */
		c.setSoftwareName(StringUtils.trim(request.getParameter("softwareName")));
		c.setAbbreviation(StringUtils.trim(request.getParameter("abbreviation")));
		c.setCrNo(StringUtils.trim(request.getParameter("crNo")));
		c.setCrUrl(StringUtils.trim(request.getParameter("crUrl")));
		c.setCrOrganization(StringUtils.trim(request.getParameter("crOrganization")));
		c.setCrSoftwareType(StringUtils.trim(request.getParameter("crSoftwareType")));
		c.setRgNo(StringUtils.trim(request.getParameter("rgNo")));
		c.setRgUrl(StringUtils.trim(request.getParameter("rgUrl")));
		c.setRgOrganization(StringUtils.trim(request.getParameter("rgOrganization")));
		c.setEpNo(StringUtils.trim(request.getParameter("epNo")));
		c.setEpUrl(StringUtils.trim(request.getParameter("epUrl")));
		c.setEpOrganization(StringUtils.trim(request.getParameter("epOrganization")));
		c.setCdNo(StringUtils.trim(request.getParameter("cdNo")));
		c.setCdUrl(StringUtils.trim(request.getParameter("cdUrl")));
		c.setCdOrganization(StringUtils.trim(request.getParameter("cdOrganization")));
		c.setModel(StringUtils.trim(request.getParameter("model")));
		c.setCharge(StringUtils.trim(request.getParameter("charge")));
		c.setOperator(systemUserService.getCurrentUsername());

		return c;
	}

	/**
	 * 关键词搜索Copyright列表
	 * 
	 * @param pageNum
	 *            页码 默认为第一页
	 * @param pageSize
	 *            页大小 默认20条/页
	 * @param sortBy
	 *            需要倒序排序的字段，默认为更新时间UpdateAt字段
	 * @param direction
	 *            <=0表示降序，>0为升序，默认为降序
	 * @return 包含搜索结果的的返回对象
	 */
	@RequestMapping(value = "/search.do", method = RequestMethod.GET)
	@ResponseBody
	public ListResult searchCopyright(Integer field, String keyword, Integer pageNum, Integer pageSize, String sortBy,
			Integer direction) {
		int pn = pageNum == null ? 0 : pageNum.intValue() - 1;
		int ps = pageSize == null ? 20 : pageSize.intValue(); // 默认20条/页
		int dir = direction == null ? 0 : (direction.intValue() <= 0 ? 0 : 1); // 默认为降序
		if (StringUtils.isBlank(sortBy)) {
			sortBy = SORTBY_CRDATE; // 默认按照软著签发日期和id倒序
		}
		String fieldName;
		if (field == null) {
			fieldName = "";
		} else {
			switch (field) {
			case 1:
				fieldName = "softwareName";
				break;
			case 2:
				fieldName = "abbreviation";
				break;
			case 3:
				fieldName = "model";
				break;
			case 4:
				fieldName = "crNo";
				break;
			case 5:
				fieldName = "rgNo";
				break;
			case 6:
				fieldName = "epNo";
				break;
			case 7:
				fieldName = "cdNo";
				break;
			default:
				fieldName = "";
				break;
			}

		}
		if (StringUtils.isBlank(keyword)) {
			keyword = "";
		}

		String[] keywordList = StringUtils.split(keyword);
		ListResult res = new ListResult();
		try {
			Page<Copyright> p = copyrightService.searchCopyrightByPage(fieldName, keywordList, pn, ps, sortBy, dir);
			if(null != p){
			res.setListContent(
				new ListContent(p.getSize(), p.getTotalElements(), p.getTotalPages(), p.getContent()));
			}
			res.setCode(RetStatus.SUCCESS.getCode());
			res.setMsg(RetStatus.SUCCESS.getInfo());
		} catch (Exception e) {
			logger.error("", e);
			res.setCode(RetStatus.SYSTEM_ERROR.getCode());
			res.setMsg(RetStatus.SYSTEM_ERROR.getInfo());
		}
		return res;
	}

}
