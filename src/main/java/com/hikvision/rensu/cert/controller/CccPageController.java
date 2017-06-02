package com.hikvision.rensu.cert.controller;

import org.apache.commons.lang3.StringUtils;
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
import com.hikvision.rensu.cert.domain.Copyright;
import com.hikvision.rensu.cert.domain.TypeInspection;
import com.hikvision.rensu.cert.service.CccPageService;
import com.hikvision.rensu.cert.support.AjaxResult;
import com.hikvision.rensu.cert.support.ListContent;
import com.hikvision.rensu.cert.support.ListResult;

/**
 * Created by rensu on 17/5/1.
 */
@Controller
@RequestMapping("/ccc")
public class CccPageController {
	private static final Logger logger = LoggerFactory.getLogger(InspectionController.class);
	private final static String SORT_TYPEINSPECTION_UPDATEDATE = "UpdateDate";

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
			logger.error("", e.getMessage());
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
}
