package com.hikvision.ga.hephaestus.cert.service;

import com.hikvision.ga.hephaestus.cert.InspectContent;
import com.hikvision.ga.hephaestus.cert.repository.InspectContentRepository;

import java.util.ArrayList;
import java.util.List;
import javax.transaction.Transactional;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



/**
 * Created by rensu on 17/4/28.
 */
@Service
@Transactional
public class InspectContentService {

    @Autowired
    private InspectContentRepository inspectContentRepository;


    public InspectContent getInspectContent(Long id) {

        return inspectContentRepository.findOne(id);
    }

    /**
     * 用外键inspectionId查询contents
     *
     * @param inspectionId 外键id
     * @return List<InspectContent> 查到的内容条目列表
     */
    public List<InspectContent> getContentsByInspectionId(Long inspectionId) {
        return inspectContentRepository.findByInspectionIdOrderById(inspectionId);
    }

    /**
     * 用外键删除contents
     *
     * @param inspectionId 外键id
     * @return List<InspectContent> 查到的内容条目列表
     */
    public void deleteByFK(Long inspectionId) {
        List<InspectContent> list = getContentsByInspectionId(inspectionId);
        inspectContentRepository.deleteInBatch(list);
    }

    /**
     * 与数据库中的contentList比较，如果已经存在，则删除后插入新数据
     *
     * @param contentList  待插入数据库的检测报告内容表（子表）
     * @param inspectionId 检测条目（主表条目）的id
     * @return void
     */
    @Transactional
    public void importContentList(List<InspectContent> contentList, Long inspectionId) throws Exception {

        // if there exists entries of this inspectionId in InspectContent table, delete these entries
        if (getContentsByInspectionId(inspectionId) != null) {
            deleteByFK(inspectionId);
        }
        inspectContentRepository.save(contentList);
    }

	public void save(List<InspectContent> contentList) {
		inspectContentRepository.save(contentList);
	}
	
	/**
	 * 导入检测报告工作表到list中
	 * 
	 * @param contentSheet:
	 *            <检测项索引表>工作表
	 * @param inspectionId:
	 *            对应的型检id
	 * @return list 检测项列表
	 * @author langyicong
	 * @throws Exception
	 */
	public  List<InspectContent> importContentSheet(Sheet contentSheet, Long inspectionId) throws InvalidFormatException {
		int rows = contentSheet.getLastRowNum() + 1;
		List<InspectContent> contentList = new ArrayList<>(); // 此处不能将row的值作为List的长度，因为工作表中可能会有无内容的行

		/* 寻找表头所在行&每个字段所在列 */
		int headRow = -1, caseIdCol = -1, caseNameCol = -1, caseDescrCol = -1, testResultCol = -1, remarkCol = -1;
		for (int row = 0; row < rows; row++) {
			Row r = contentSheet.getRow(row);
			int cols = r.getPhysicalNumberOfCells();
			for (int col = 0; col < cols; col++) {
				if (r.getCell(col) != null) {
					if (r.getCell(col).getCellTypeEnum() != CellType.STRING) {
						r.getCell(col).setCellType(CellType.STRING);
					}
					String cellValue = r.getCell(col).getStringCellValue();
					if (StringUtils.contains(cellValue, "序号")) {
						headRow = row;
						caseIdCol = col;
					}
					if (StringUtils.containsAny(cellValue, "项目", "用例名称", "功能列表")) {
						caseNameCol = col;
					}
					if (StringUtils.containsAny(cellValue, "要求", "用例说明", "功能描述")) {
						caseDescrCol = col;
					}
					if (StringUtils.contains(cellValue, "测试结果")) {
						testResultCol = col;
					}
					if (StringUtils.contains(cellValue, "备注")) {
						remarkCol = col;
					}
				}
			}
			if (row == headRow) {
				break;
			}
		}

		if (headRow < 0 || caseIdCol < 0 || caseNameCol < 0 || caseDescrCol < 0) {
			throw new InvalidFormatException("缺少关键字");
		}

		String cachedCaseId = null;
		String cachedCaseName = null;
		for (int row = headRow; row < rows; row++) {
			Row r = contentSheet.getRow(row);
			/* for rows those are not empty */
			if (r.getCell(caseDescrCol) != null && r.getCell(caseDescrCol).getCellTypeEnum() != CellType.BLANK) {
				InspectContent c = new InspectContent();
				if (r.getCell(caseIdCol) != null && r.getCell(caseIdCol).getCellTypeEnum() != CellType.BLANK) {
					if (r.getCell(caseIdCol).getCellTypeEnum() != CellType.STRING) {
						r.getCell(caseIdCol).setCellType(CellType.STRING);
					}
					cachedCaseId = r.getCell(caseIdCol).getStringCellValue();
				}
				c.setCaseId(cachedCaseId);

				if (r.getCell(caseNameCol) != null && r.getCell(caseNameCol).getCellTypeEnum() != CellType.BLANK) {
					cachedCaseName = r.getCell(caseNameCol).getStringCellValue();
				}
				c.setCaseName(cachedCaseName);

				if (caseDescrCol >= 0) {
					c.setCaseDescription(r.getCell(caseDescrCol).getStringCellValue());
				}
				if (testResultCol >= 0) {
					c.setTestResult(r.getCell(testResultCol).getStringCellValue());
				}
				if (remarkCol >= 0) {
					c.setRemarks(r.getCell(remarkCol).getStringCellValue());
				}
				c.setInspectionId(inspectionId);
				contentList.add(c);
			}
		}
		return contentList;
	}
	
}
