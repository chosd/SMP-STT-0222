package com.kt.smp.common.util.excel;

import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.util.List;

public class ExcelMakeForm {

	public static Workbook makeUploadFormExcel(String sheetTitle, List<String> columns) {

		int cellCnt = 0;

		Workbook workbook = new XSSFWorkbook();

		//cell 스타일 설정
		CellStyle styleTitle = getCellStyle(workbook, "title");
		CellStyle styleCommon = getCellStyle(workbook, "common");

		// 1번째 Sheet 생성
		Sheet sheet = workbook.createSheet(sheetTitle);
		sheet.setDefaultColumnWidth(25);
		//sheet.setColumnWidth(9, 7000);

		Row row = null;
		Cell cell = null;
		row = sheet.createRow(0);

		//첫 행(title)
		for (int i = 0; i < columns.size(); i++) {
			cell = row.createCell(cellCnt++);
			cell.setCellValue(columns.get(i));
			cell.setCellStyle(styleTitle);
		}
		//2번째 행(테두리, data validation, comment)
		row = sheet.createRow(1);
		for (int i = 0; i < columns.size(); i++) {
			cell = row.createCell(i);
			cell.setCellStyle(styleCommon);
		}
		//cell dataValdation

		return workbook;
	}

	public static Workbook makeResultExcel(String sheetTitle, List<String> columns, List<List<String>> columnValues) {
		int rowCnt = 1;

		Workbook workbook = new XSSFWorkbook();

		//cell 스타일 설정
		CellStyle styleTitle = getCellStyle(workbook, "title");
		CellStyle styleCommon = getCellStyle(workbook, "common");

		// 1번째 Sheet 생성
		Sheet sheet = workbook.createSheet(sheetTitle);
		sheet.setDefaultColumnWidth(25);
		//sheet.setColumnWidth(9, 7000);

		Row row = null;
		Cell cell = null;
		row = sheet.createRow(0);

		//첫 행(title)
		for (int i = 0; i < columns.size(); i++) {
			cell = row.createCell(i);
			cell.setCellValue(columns.get(i));
			cell.setCellStyle(styleTitle);
		}

		//2번째 이후 행(실제 데이터)
		for (List<String> values : columnValues) {
			row = sheet.createRow(rowCnt++);
			for (int i = 0; i < values.size(); i++) {
				cell = row.createCell(i);
				cell.setCellStyle(styleCommon);
				cell.setCellValue(values.get(i));
			}
		}

		return workbook;
	}

	public static CellStyle getCellStyle(Workbook workbook, String param) {

		CellStyle style = workbook.createCellStyle();
		if (param.equals("title")) {
			Font font = workbook.createFont();
			font.setColor(HSSFColor.HSSFColorPredefined.WHITE.getIndex());
			style.setAlignment(HorizontalAlignment.CENTER);
			style.setBorderRight(BorderStyle.MEDIUM);
			style.setBorderLeft(BorderStyle.MEDIUM);
			style.setBorderTop(BorderStyle.MEDIUM);
			style.setBorderBottom(BorderStyle.MEDIUM);
			style.setFillForegroundColor(HSSFColor.HSSFColorPredefined.BLACK.getIndex());
			style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			style.setFont(font);
		} else {
			style.setAlignment(HorizontalAlignment.CENTER);
			style.setBorderRight(BorderStyle.THIN);
			style.setBorderLeft(BorderStyle.THIN);
			style.setBorderTop(BorderStyle.THIN);
			style.setBorderBottom(BorderStyle.THIN);
			style.setFillForegroundColor(HSSFColor.HSSFColorPredefined.WHITE.getIndex());
			style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		}
		return style;
	}
}
