package com.hikvision.rensu.cert.controller;

import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

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
import com.hikvision.rensu.cert.domain.Copyright;
import com.hikvision.rensu.cert.domain.TypeInspection;
import com.hikvision.rensu.cert.service.CopyrightService;
import com.hikvision.rensu.cert.support.AjaxResult;
import com.hikvision.rensu.cert.support.ListContent;
import com.hikvision.rensu.cert.support.ListResult;

@Controller
@RequestMapping("/copyright")
public class CopyrightController {
	
	private static final Logger logger = LoggerFactory.getLogger(InspectionController.class);
	private final static String SORT_TYPEINSPECTION_UPDATEDATE = "UpdateDate";

    @Autowired
    private CopyrightService copyrightService;
   
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
    @RequestMapping(value ="/list.do", method = RequestMethod.GET)
    @ResponseBody
    public ListResult getListByPage(Integer pageNum, Integer pageSize, String sortBy, Integer direction) {
		int pn = pageNum == null ? 0 : pageNum.intValue() - 1;
		int ps = pageSize == null ? 20 : pageSize.intValue(); // 默认20条/页
		int dir = direction == null ? 0 : (direction.intValue() <= 0 ? 0 : 1); // 默认为降序
		if (StringUtils.isBlank(sortBy)) {
			sortBy = SORT_TYPEINSPECTION_UPDATEDATE; // 默认按照更新时间倒序
		}
		ListResult res = new ListResult();
		try {
			Page<Copyright> p = copyrightService.getCopyrightByPage(pn, ps, sortBy, dir);
			res.setListContent(new ListContent<Copyright>(p.getSize(), p.getTotalElements(), 
					p.getTotalPages(),p.getContent()));
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
     * 获取双证单条数据
     */
    @RequestMapping(value ="/detail.do", method = RequestMethod.GET)
    @ResponseBody
    public AjaxResult<Copyright> getCopyright(Long id) {
    	AjaxResult<Copyright> res = new AjaxResult<Copyright>();
    	if (null == id) {
			res.setCode(RetStatus.FORM_DATA_MISSING.getCode());
			res.setMsg(RetStatus.FORM_DATA_MISSING.getInfo());
		} else {
			try {
				Copyright c = copyrightService.getCopyrightById(id);
				if(c != null){
					res.setData(c);
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
     * 保存单条数据
     * TODO 自定义返回类型
     */
    @RequestMapping(value = "/save.do", method = RequestMethod.POST)
    @ResponseBody
	public List<String> saveCopyrightForm(HttpServletRequest request) {
    	System.out.println("Start"+request.getParameter("softwareName"));
        //TODO 保存数据
    	List<String> list = new ArrayList<>();
        list.add("aaa");
        list.add("bbb");
        list.add("ccc");
        return list;
    }
    

    
}
