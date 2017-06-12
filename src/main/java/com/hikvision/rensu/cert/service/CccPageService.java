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

import com.hikvision.rensu.cert.controller.InspectionController;
import com.hikvision.rensu.cert.domain.CccPage;
import com.hikvision.rensu.cert.repository.CccPageRepository;

@Service
@Transactional
public class CccPageService {
	
	private static final Logger logger = LoggerFactory.getLogger(InspectionController.class);

	@Autowired
	private CccPageRepository cccPageRepository;
	
	public Page<CccPage> getCCCListByPage(Integer pageNum, Integer pageSize, String sortBy, int dir) {
		Direction d = dir > 0 ? Direction.ASC : Direction.DESC;
		Pageable page = new PageRequest(pageNum, pageSize, new Sort(d, sortBy));
		return cccPageRepository.findAll(page);
	}

	/**
	 * 导入列表数据 到数据库
	 * 
	 *
	 * @param sheet:
	 *            【3C】工作薄
	 * @return res: 更新/导入的条目数量
	 * @throws Exception
	 * @author langyicong
	 */
	public int importCCCSheet(Sheet sheet) throws Exception {
		int res = 0;
		int rows = sheet.getLastRowNum() + 1;
		List<CccPage> cccList = new ArrayList<>();
		
		/* 找到表头并且 判断表头是否符合条件 */
		int headRow = -1, docNoCol = -1, modelCol = -1, productNameCol = -1;
		for (int row = 0; row < rows; row++) {
			Row r = sheet.getRow(row);
			int cols = r.getPhysicalNumberOfCells();
			for (int col = 0; col < cols; col++) {
				if (r.getCell(col) != null) {
					if (r.getCell(col).getCellTypeEnum() != CellType.STRING) {
						r.getCell(col).setCellType(CellType.STRING);
					}
					String cellValue = r.getCell(col).getStringCellValue();
					if (StringUtils.contains(cellValue, "文件编号")) {
						headRow = row;
						docNoCol = col;
					}
					if (StringUtils.contains(cellValue, "型号")) {
						modelCol = col;
					}
					if (StringUtils.contains(cellValue, "名称")) {
						productNameCol = col;
					}
				}
			}
			if (row == headRow) {
				break;
			}
		}
		if (headRow < 0 || docNoCol < 0 || modelCol < 0 || productNameCol < 0) {
			throw new Exception("缺少关键字");
		}
		for (int row = headRow + 1; row < rows; row++) {
			Row r = sheet.getRow(row);
			/* for rows those are not empty */
			if (r.getCell(0) != null && r.getCell(0).getCellTypeEnum() != CellType.BLANK
					&& r.getCell(1).getCellTypeEnum() != CellType.BLANK) {
				CccPage c = null;
				if (r.getCell(docNoCol) != null) {
					if (r.getCell(docNoCol).getCellTypeEnum() != CellType.STRING) {
						try {
							r.getCell(docNoCol).setCellType(CellType.STRING);
						} catch (Exception e) {
							throw e;
						}
					}
					if (cccPageRepository.findByDocNo(r.getCell(docNoCol).getStringCellValue()).size() > 0) {
						/* if this docNo exists in db, find it and update other properties */
						c = cccPageRepository.findByDocNo(r.getCell(docNoCol).getStringCellValue()).get(0);
					} else {
						c = new CccPage();
						c.setCreateDate(new Date());
						c.setDocNo(StringUtils.trim(r.getCell(docNoCol).getStringCellValue()));
					}
				} else {
					throw new Exception("Missing docNo");
				}
				/* Set other properties*/
				c.setUpdateDate(new Date());
				c.setModel(r.getCell(modelCol).getStringCellValue());
				c.setProductName(StringUtils.trim(r.getCell(productNameCol).getStringCellValue()));
				/* 颁发日期 */
				if(r.getCell(3)!=null){
					if (r.getCell(3).getCellTypeEnum() != CellType.NUMERIC) {
						r.getCell(3).setCellType(CellType.NUMERIC);
					}
					c.setAwardDate(r.getCell(3).getDateCellValue());
				}
				/* 有效日期 */
				if(r.getCell(4)!=null){
					if (r.getCell(4).getCellTypeEnum() != CellType.NUMERIC) {
						r.getCell(4).setCellType(CellType.NUMERIC);
					}
					c.setExpiryDate(r.getCell(4).getDateCellValue());
				}
				c.setUrl(StringUtils.trim(r.getCell(5).getStringCellValue()));
				c.setOrganization(StringUtils.trim(r.getCell(6).getStringCellValue()));
				c.setRemarks(StringUtils.trim(r.getCell(7).getStringCellValue()));
				
				cccList.add(c);
			}
		}
		try {
			cccPageRepository.save(cccList);
			res = cccList.size();
		} catch (Exception e) {
			logger.error("",e);
			throw e;
		}

		return res;
	}

	public CccPage getCccPageById(Long id) {
		return cccPageRepository.findOne(id);
	}

	public List<CccPage> findByDocNo(String docNo) {
		return cccPageRepository.findByDocNo(docNo);
	}

	public CccPage saveCccPage(CccPage c) {
		return cccPageRepository.save(c);
		
	}

	/**
	 * 模糊搜索关键字
	 */
	public Page<CccPage> searchCccByPage(String fieldName, String[] keywordList, int pn, int ps, String sortBy,
			int dir) {
		Page<CccPage> p = null;
		Direction d = dir > 0 ? Direction.ASC : Direction.DESC;
		Pageable page = new PageRequest(pn, ps, new Sort(d, sortBy));
		
		if(keywordList.length > 0){
			List<CccPage> ccc = cccPageRepository.searchCccByKeyword(fieldName,keywordList);
			p = new PageImpl<CccPage>(ccc, page, ccc.size());
		} else {
			p = cccPageRepository.findAll(page);
		}
		return p;
	}

}