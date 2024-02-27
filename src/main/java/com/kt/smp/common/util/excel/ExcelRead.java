package com.kt.smp.common.util.excel;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ExcelRead {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(ExcelRead.class);

	public static <E> List<E> read(Workbook wb, ExcelReadOption excelReadOption, Class<E> classType) {

		/*
		 * 엑셀의 각 Row를 리스트에 담는다.
		 * 하나의 Row를 하나의 Map으로 표현되며 List에는 모든 Row가 포함.
		 */
		List<E> result = new ArrayList<>();

		// FileType.getWorkbook() <-- 파일의 확장자에 따라서 적절하게 가져온다
		try {
			int sheetNum = 0;
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);
			objectMapper.configure(MapperFeature.ACCEPT_CASE_INSENSITIVE_PROPERTIES, true);

			if (wb != null) {
				Sheet sheet = wb.getSheetAt(sheetNum);

				final Map<String, Type> cells = excelReadOption.getOutputColumns(); // 외부에서 세팅한 컬럼 개수
				final int numOfRows = sheet.getPhysicalNumberOfRows(); // 유효한 데이터가 있는 행의 개수를 가져온다.

				for (int i = excelReadOption.getStartRow(); i < numOfRows; i++) {
					Row row = sheet.getRow(i);
					AtomicInteger index = new AtomicInteger();

					/**
					 * 각 row마다의 값을 저장할 맵 객체 저장되는 형식은 다음과 같다. put("A", "이름"); put("B", "게임명");
					 */
					Map<String, Object> rowData = new LinkedHashMap<>();

					cells.forEach(
							(key, val) -> {

								Object refVal;
								Cell cell = row.getCell(index.getAndIncrement());

								if (val.equals(Integer.class)) {
									if (cell != null && cell.getCellTypeEnum() == CellType.NUMERIC) {
										refVal = (int) cell.getNumericCellValue();
									} else if (cell != null && cell.getCellTypeEnum() == CellType.STRING) {
										refVal = Integer.parseInt(cell.getStringCellValue());
									} else {
										refVal = 0;
									}
								} else if (val.equals(Double.class)) {
									if (cell != null && cell.getCellTypeEnum() == CellType.NUMERIC) {
										refVal = (double) cell.getNumericCellValue();
									} else if (cell != null && cell.getCellTypeEnum() == CellType.STRING) {
										refVal = Double.parseDouble(cell.getStringCellValue());
									} else {
										refVal = 0;
									}
								} else if (val.equals(Long.class)) {
									if (cell != null && cell.getCellTypeEnum() == CellType.NUMERIC) {
										refVal = (long) cell.getNumericCellValue();
									} else if (cell != null && cell.getCellTypeEnum() == CellType.STRING) {
										refVal = Long.parseLong(cell.getStringCellValue());
									} else {
										refVal = 0;
									}
								} else if (val.equals(LocalDateTime.class) || val.equals(Date.class)) {
									refVal = cell.getDateCellValue();
								} else {
									if (cell != null && cell.getCellTypeEnum() == CellType.NUMERIC) {
										String cellVal = ((XSSFCell) cell).getRawValue();

										if (cellVal == null) {
											cellVal = "";
										}

										String[] splited = cellVal.split("\\.");

										if (splited.length == 2 && Integer.parseInt(splited[1]) == 0) {
											refVal = splited[0];
										} else {
											refVal = cellVal;
										}
									} else if (cell != null){
										refVal = cell.getStringCellValue();
									} else {
										refVal = "";
									}
								}

								rowData.put(key, refVal);
							});

					E o = objectMapper.convertValue(rowData, classType);
					result.add(o);
				}

				return result;
			}
		} catch (Exception e) {
			LOGGER.error("(!) poi error : {}", e.getMessage());
		}

		return result;
	}
}
