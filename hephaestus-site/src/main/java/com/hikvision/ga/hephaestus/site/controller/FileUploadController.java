package com.hikvision.ga.hephaestus.site.controller;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.hikvision.ga.hephaestus.cert.service.CccPageService;
import com.hikvision.ga.hephaestus.cert.service.CopyrightService;
import com.hikvision.ga.hephaestus.cert.service.TypeInspectionService;
import com.hikvision.ga.hephaestus.common.constant.RetStatus;
import com.hikvision.ga.hephaestus.common.support.ImportResult;
import com.hikvision.ga.hephaestus.site.cert.constant.BusinessType;
import com.hikvision.ga.hephaestus.site.cert.constant.HikRole;
import com.hikvision.ga.hephaestus.site.cert.constant.OperationAct;
import com.hikvision.ga.hephaestus.site.logger.OperationLogBuilder;
import com.hikvision.ga.hephaestus.site.security.annotation.HikRoleAccess;

/**
 * 文件上传
 */
@PreAuthorize("hasRole('ROLE_ADMIN')")
@Controller
@RequestMapping("/fileupload")
public class FileUploadController {

  static private final Logger logger = LoggerFactory.getLogger(FileUploadController.class);

  @Autowired
  private TypeInspectionService typeInspectionService;

  @Autowired
  private CopyrightService copyrightService;

  @Autowired
  private CccPageService cccPageService;

  /**
   * 上传资质列表（公检、双证、3C等）
   *
   * @param file 上传的文件
   * @return 上传结果
   */
  @RequestMapping(value = "/indexlist.do", method = RequestMethod.POST)
  @ResponseBody
  @HikRoleAccess(roles = HikRole.ADMIN)
  public ImportResult saveIndexList(@RequestBody MultipartFile file) {
    // 操作日志：记录动作和模块
    OperationLogBuilder.build().act(OperationAct.UPLOAD).businessType(BusinessType.FILEUPLOAD);
    if (null == file || file.isEmpty()) {
      OperationLogBuilder.build().operateResult(0).errorCode(RetStatus.FILE_EMPTY.getCode()).log();
      return new ImportResult(RetStatus.FILE_EMPTY.getCode(), RetStatus.FILE_EMPTY.getInfo(), 0, 0,
          0);
    } else {
      InputStream xlsxFile = null;
      int numOfInpections = 0;
      int numOfCopyRight = 0;
      int numOf3C = 0;
      /*
       * Test excel Encryption & handle exceptions
       */
      try {
        Workbook workbook = WorkbookFactory.create(file.getInputStream());
        for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
          if (StringUtils.contains(workbook.getSheetName(i), "公检")) {
            numOfInpections = typeInspectionService.importInspectionSheet(workbook.getSheetAt(i));
          } else if (StringUtils.contains(workbook.getSheetName(i), "双证")) {
            numOfCopyRight = copyrightService.importCopyRightSheet(workbook.getSheetAt(i));
          } else if (StringUtils.contains(workbook.getSheetName(i), "3C")) {
            numOf3C = cccPageService.importCCCSheet(workbook.getSheetAt(i));
          }
        }
        // 操作日志：操作对象和内容
        OperationLogBuilder.build().operateResult(1).content(file.getOriginalFilename())
            .operateObjectKeys("型检,双证,3C")
            .operateObjectValues(numOfInpections + "," + numOfCopyRight + "," + numOf3C).log();
        return new ImportResult(RetStatus.SUCCESS.getCode(), RetStatus.SUCCESS.getInfo(),
            numOfInpections, numOfCopyRight, numOf3C);
      } catch (IOException e) {
        logger.error("", e);
        if (StringUtils.contains(e.getMessage(), "EncryptionInfo")) {
          // 文件被加密
          OperationLogBuilder.build().operateResult(0).errorCode(RetStatus.FILE_ENCYPTED.getCode())
              .log();
          return new ImportResult(RetStatus.FILE_ENCYPTED.getCode(),
              RetStatus.FILE_ENCYPTED.getInfo());
        } else {
          OperationLogBuilder.build().operateResult(0)
              .errorCode(RetStatus.FILE_PARSING_ERROR.getCode()).log();
          return new ImportResult(RetStatus.FILE_PARSING_ERROR.getCode(),
              RetStatus.FILE_PARSING_ERROR.getInfo());
        }
      } catch (EncryptedDocumentException | InvalidFormatException e) {
        logger.error("", e);
        OperationLogBuilder.build().operateResult(0).errorCode(RetStatus.FILE_INVALID.getCode())
            .log();
        return new ImportResult(RetStatus.FILE_INVALID.getCode(), RetStatus.FILE_INVALID.getInfo());
      } catch (Exception e) {
        logger.error("", e);
        if (StringUtils.contains(e.getCause().getCause().toString(), "duplicate")) {
          String cause = e.getCause().getCause().toString().toString();
          OperationLogBuilder.build().operateResult(0)
              .errorCode(RetStatus.DOCNO_DUPLICATED.getCode()).log();
          return new ImportResult(RetStatus.DOCNO_DUPLICATED.getCode(),
              cause.substring(cause.indexOf("详细") + 3));
        }
        OperationLogBuilder.build().operateResult(0)
            .errorCode(RetStatus.FILE_PARSING_ERROR.getCode()).log();
        return new ImportResult(RetStatus.FILE_PARSING_ERROR.getCode(),
            RetStatus.FILE_PARSING_ERROR.getInfo());
      } finally {
        IOUtils.closeQuietly(xlsxFile);
      }
    }
  }


}
