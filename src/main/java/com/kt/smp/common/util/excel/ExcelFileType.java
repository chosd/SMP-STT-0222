package com.kt.smp.common.util.excel;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

@Slf4j
public class ExcelFileType {

	/**
	 *
	 * 엑셀파일을 읽어서 Workbook 객체에 리턴한다. XLS와 XLSX 확장자를 비교한다.
	 *
	 * @param filePath
	 * @return
	 *
	 */
	public static Workbook getWorkbook(String filePath) {
		/*
		 * FileInputStream은 파일의 경로에 있는 파일을 읽어서 Byte로 가져온다.
		 *
		 * 파일이 존재하지 않는다면은 RuntimeException이 발생된다.
		 */
		FileInputStream fis = null;

		Workbook wb = null;

		try {
			fis = new FileInputStream(filePath);

			/*
			 * 파일의 확장자를 체크해서 .XLS 라면 HSSFWorkbook에 .XLSX라면 XSSFWorkbook에 각각 초기화 한다.
			 */
			if (filePath.toUpperCase().endsWith(".XLS")) {
				wb = new HSSFWorkbook(fis);
			} else if (filePath.toUpperCase().endsWith(".XLSX")) {
				wb = new XSSFWorkbook(fis);
			}
		} catch (FileNotFoundException e) {
			log.error("[ERROR] getWorkbook FileNotFoundException - EXCEL 파일 없음 : {}", e.getMessage());
		} catch (IOException e) {
			log.error("[ERROR] getWorkbook IOException - EXCEL 파일 전송 오류 : {}", e.getMessage());
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					log.error("[ERROR] getWorkbook IOException - EXCEL 파일 Stream Close 오류 : {}", e.getMessage());
				}
				fis = null;
			}
		}

		return wb;
	}

	public static Workbook getWorkbook(MultipartFile file) {
		String extension = "";

		if (file.getOriginalFilename().equals("")) {
			extension = "";
		} else {
			extension = FilenameUtils.getExtension(file.getOriginalFilename());
		}

		try {
			if (extension.equals("xlsx") || extension.equals("XLSX")) {
				return new XSSFWorkbook(file.getInputStream());
			} else if (extension.equals("xls") || extension.equals("XLS")) {
				return new HSSFWorkbook(file.getInputStream());
			} else {
				throw new IOException();
			}
		} catch (IOException e) {
			log.error("[ERROR] getWorkbook IOException : {}", e.getMessage());

			return null;
		}
	}

}

