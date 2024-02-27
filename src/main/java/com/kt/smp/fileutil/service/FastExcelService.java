package com.kt.smp.fileutil.service;

import org.dhatim.fastexcel.Color;
import org.dhatim.fastexcel.Workbook;
import org.dhatim.fastexcel.Worksheet;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import static com.kt.smp.fileutil.constant.ExcelConstants.MAX_ROWS_PER_SHEET;

public abstract class FastExcelService {

	private final static int PAGE_SIZE = 10000;

	private final static int FLUSH_SIZE = 500;

	public static int getPageSize() {
		return PAGE_SIZE;
	}

	public static int getFlushSize() {
		return FLUSH_SIZE;
	}

	public abstract int count(Object searchCondition);
	
	public abstract int amCount(Object searchCondition);

	public abstract String getSheetName();
	
	public abstract String getAmSheetName();

	public abstract String getFileName(Long id);
	
	public abstract String getAmFileName(Long id);

	public abstract void makePage(Worksheet ws, int pageIndex, int sheetIndex, Object searchCondition) throws IOException;
	
	public abstract void amMakePage(Worksheet ws, int pageIndex, int sheetIndex, Object searchCondition) throws IOException;

	public void createWorksheet(OutputStream os, Object searchCondition, List<String> headers) throws IOException {
		Workbook wb = new Workbook(os, "ExcelApplication", "1.0");

		int count = count(searchCondition);

		int numSheets = Math.max(count / MAX_ROWS_PER_SHEET + (count % MAX_ROWS_PER_SHEET > 0 ? 1 : 0), 1);

		for (int sheetIdx = 0; sheetIdx < numSheets; sheetIdx++) {
			String sheetName = getSheetName();
			if (numSheets > 1) {
				StringBuffer stringBuffer = new StringBuffer(sheetName);
				stringBuffer.append("_");
				stringBuffer.append(sheetIdx + 1);
//				sheetName += "_" + (sheetIdx + 1);
			}
			Worksheet ws = wb.newWorksheet(sheetName);

			// 헤더 생성
			makeHeaderRow(ws, headers);

			// row 생성
			for (int pageStartRowIdx = 0;
				 pageStartRowIdx < Math.min(MAX_ROWS_PER_SHEET, count - sheetIdx * MAX_ROWS_PER_SHEET);
				 pageStartRowIdx += PAGE_SIZE) {

				makePage(ws, pageStartRowIdx, sheetIdx, searchCondition);
			}

			ws.flush();
			ws.finish();
		}

		wb.finish();
	}

	public void amCreateWorksheet(OutputStream os, Object searchCondition, List<String> headers) throws IOException {
		Workbook wb = new Workbook(os, "ExcelApplication", "1.0");

		int count = amCount(searchCondition);

		int numSheets = Math.max(count / MAX_ROWS_PER_SHEET + (count % MAX_ROWS_PER_SHEET > 0 ? 1 : 0), 1);

		for (int sheetIdx = 0; sheetIdx < numSheets; sheetIdx++) {
			String sheetName = getSheetName();

			if (numSheets > 1) {
//				sheetName += "_" + (sheetIdx + 1);
				StringBuffer sb = new StringBuffer(sheetName);
				sb.append("_");
				sb.append(sheetIdx + 1);
			}
			Worksheet ws = wb.newWorksheet(sheetName);

			// 헤더 생성
			makeHeaderRow(ws, headers);

			// row 생성
			for (int pageStartRowIdx = 0;
				 pageStartRowIdx < Math.min(MAX_ROWS_PER_SHEET, count - sheetIdx * MAX_ROWS_PER_SHEET);
				 pageStartRowIdx += PAGE_SIZE) {

				amMakePage(ws, pageStartRowIdx, sheetIdx, searchCondition);
			}

			ws.flush();
			ws.finish();
		}

		wb.finish();
	}
	
	private void makeHeaderRow(Worksheet ws, List<String> headers) {
		for (int i = 0; i < headers.size(); i++) {
			ws.width(i, 50);
		}

		for (int headerIdx = 0; headerIdx < headers.size(); headerIdx++) {
			ws.value(0, headerIdx, headers.get(headerIdx));
		}

		ws.range(0, 0, 0, headers.size() - 1).style().horizontalAlignment("center").set();
		ws.range(0, 0, 0, headers.size() - 1).style().fillColor(Color.LIGHT_GREEN).set();

	}
}

