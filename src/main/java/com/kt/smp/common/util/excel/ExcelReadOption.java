package com.kt.smp.common.util.excel;

import lombok.Builder;
import lombok.Data;

import java.lang.reflect.Type;
import java.util.Map;

@Data
@Builder
public class ExcelReadOption {
	/**
	 * 엑셀파일의 경로
	 */
	private String filePath;

	/**
	 * 추출할 컬럼 명
	 */
	private Map<String, Type> outputColumns;

	/**
	 * 추출을 시작할 행 번호
	 */
	private int startRow;

	public void addColumn(String name, Type type) {
		outputColumns.put(name, type);
	}

}


