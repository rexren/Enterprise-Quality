package com.hikvision.rensu.cert.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hikvision.rensu.cert.domain.Copyright;
import com.hikvision.rensu.cert.repository.CopyrightRepository;

@Service
@Transactional
public class CopyrightService {
	
	private final static Logger logger = LoggerFactory.getLogger(TypeInspectionService.class);
	
	@Autowired
	private CopyrightRepository copyrightRepository;

	@Autowired
	private SystemUserService systemUserService;
	
	public Page<Copyright> getCopyrightByPage(int pageNum, int pageSize, String sortBy, int dir){
		Direction d = dir > 0 ? Direction.ASC : Direction.DESC;
		Pageable page = new PageRequest(pageNum, pageSize, new Sort(d, sortBy, "id"));
        return copyrightRepository.findAll(page);
	}

	public Copyright getCopyrightById(Long id){
		return copyrightRepository.findOne(id);
	}

	/**
	 * 导入列表数据 到数据库
	 * 软件名称|简称
	 * 软件著作权登记号|签发日期|软件著作权文件链接|认证/测试机构|软件类型|
	 * 软件产品登记证书|登记日期|登记有效期|软件产品登记证书链接|认证/测试机构|
	 * 软件评测报告|签发日期|软件评测报告链接|软件评测报告认证/测试机构|
	 * 类别界定报告|鉴定/界定日期|类别界定报告链接|类别界定报告认证/测试机构|软件型号|
	 * 负责人
	 *
	 * @param sheet:
	 *            【双证】工作薄
	 * @return res: 更新/导入的条目数量
	 * @throws Exception
	 * @author langyicong
	 */
	public int importCopyRightSheet(Sheet sheet) throws Exception {
		int res = 0;
		int rows = sheet.getLastRowNum() + 1;
		List<Copyright> copyrights = new ArrayList<>();
		
		/* 找到表头并且 判断表头是否符合条件 */
		int headRow = -1, softwareNameCol = -1;
		for (int row = 0; row < rows; row++) {
			Row r = sheet.getRow(row);
			int cols = r.getPhysicalNumberOfCells();
			for (int col = 0; col < cols; col++) {
				if (r.getCell(col) != null) {
					if (r.getCell(col).getCellTypeEnum() != CellType.STRING) {
						r.getCell(col).setCellType(CellType.STRING);
					}
					String cellValue = r.getCell(col).getStringCellValue();
					if (StringUtils.contains(cellValue, "软件名称")) {
						headRow = row;
						softwareNameCol = col;
					}
				}
			}
			if (row == headRow) {
				break;
			}
		}
		if (headRow < 0 || softwareNameCol < 0) {
			throw new Exception("缺少关键字");
		}
		for (int row = headRow+1; row < rows; row++) {
			Row r = sheet.getRow(row);
			/* for rows those are not empty */
			if (r.getCell(softwareNameCol) != null && r.getCell(softwareNameCol).getCellTypeEnum() != CellType.BLANK
					&& r.getCell(softwareNameCol+1).getCellTypeEnum() != CellType.BLANK) {
				Copyright c = null;
				
				if (r.getCell(softwareNameCol).getCellTypeEnum() != CellType.STRING) {
					try {
						r.getCell(softwareNameCol).setCellType(CellType.STRING);
					} catch (Exception e) {
						throw e;
					}
				}
				List<Copyright> dupItems = copyrightRepository.findBySoftwareName(StringUtils.trim(r.getCell(softwareNameCol).getStringCellValue()));
				if (dupItems.size() > 0) {
					/* if this softwareName exists in db, find it and update other properties */
					c = dupItems.get(0);
				} else {
					c = new Copyright();
					c.setCreateDate(new Date());
					c.setSoftwareName(StringUtils.trim(r.getCell(softwareNameCol).getStringCellValue()));
				}
				/* Set other properties*/
				c.setUpdateDate(new Date());
				c.setAbbreviation(r.getCell(1).getStringCellValue());
				c.setCrNo(r.getCell(2).getStringCellValue());
				if (r.getCell(3) != null) {
					if (r.getCell(3).getCellTypeEnum() != CellType.NUMERIC) {
						r.getCell(3).setCellType(CellType.NUMERIC);
					}
					c.setCrDate(r.getCell(3).getDateCellValue());
				}
				c.setCrUrl(r.getCell(4).getStringCellValue());
				c.setCrOrganization(r.getCell(5).getStringCellValue());
				c.setCrSoftwareType(r.getCell(6).getStringCellValue());
				c.setRgNo(r.getCell(7).getStringCellValue());
				if(r.getCell(8)!=null){
					if (r.getCell(8).getCellTypeEnum() != CellType.NUMERIC) {
						r.getCell(8).setCellType(CellType.NUMERIC);
					}
					c.setRgDate(r.getCell(8).getDateCellValue());
				}
				if(r.getCell(9)!=null){
					if (r.getCell(9).getCellTypeEnum() != CellType.NUMERIC) {
						r.getCell(9).setCellType(CellType.NUMERIC);
					}
					c.setRgExpiryDate(r.getCell(9).getDateCellValue());
				}
				c.setRgUrl(r.getCell(10).getStringCellValue());
				c.setRgOrganization(r.getCell(11).getStringCellValue());
				c.setEpNo(r.getCell(12).getStringCellValue());
				if(r.getCell(13)!=null){
					if (r.getCell(13).getCellTypeEnum() != CellType.NUMERIC) {
						r.getCell(13).setCellType(CellType.NUMERIC);
					}
					c.setEpDate(r.getCell(13).getDateCellValue());
				}
				c.setEpUrl(r.getCell(14).getStringCellValue());
				c.setEpOrganization(r.getCell(15).getStringCellValue());
				c.setCdNo(r.getCell(16).getStringCellValue());
				if(r.getCell(17)!=null){
					if (r.getCell(17).getCellTypeEnum() != CellType.NUMERIC) {
						r.getCell(17).setCellType(CellType.NUMERIC);
					}
					c.setCdDate(r.getCell(17).getDateCellValue());
				}
				c.setCdUrl(r.getCell(18).getStringCellValue());
				c.setCdOrganization(r.getCell(19).getStringCellValue());
				c.setModel(r.getCell(20).getStringCellValue());
				c.setCharge(r.getCell(21).getStringCellValue());
				c.setOperator(systemUserService.getCurrentUsername());
				copyrights.add(c);
			}
		}
		try {
			res = copyrightRepository.save(copyrights).size();
		} catch (Exception e) {
			logger.error("",e);
			throw e;
		}
		
		return res;
	}

	public List<Copyright> findBySoftwareName(String softwareName) {
		return copyrightRepository.findBySoftwareName(softwareName);
	}
	
	/**
	 * 新建/更新存儲双证条目
	 * 
	 * @param c
	 *            待插入数据库的双证条目
	 * @return 存储成功的Copyright实体
	 */
	public Copyright saveCopyright(Copyright c) {
		return copyrightRepository.save(c);
	}
	
	/**
	 * 模糊搜索关键字
	 */
	public Page<Copyright> searchCopyrightByPage(String fieldName, String[] keywordList, int pn, int ps, String sortBy,
			int dir) {
		Page<Copyright> p = null;
		Direction d = dir > 0 ? Direction.ASC : Direction.DESC;
		Pageable page = new PageRequest(pn, ps, new Sort(d, sortBy, "id"));
		
		if(keywordList.length > 0){
			List<Copyright> crs = copyrightRepository.searchCopyrightByKeyword(fieldName, keywordList);
			p = new PageImpl<Copyright>(crs, page, crs.size());
		} else {
			p = copyrightRepository.findAll(page);
		}
		return p;
	}

	public void deleteCopyrightById(Long id) throws IllegalArgumentException {
		copyrightRepository.delete(id);
		
	}
}
