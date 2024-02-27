package com.kt.smp.fileutil.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dhatim.fastexcel.Worksheet;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TrainDataSampleDownloadService extends FastExcelService {

	@Override
	public int count(Object searchCondition) {
		return 1;
	}

	@Override
	public String getSheetName() {
		return "LM 학습데이터 등록 샘플";
	}

	@Override
	public String getFileName(Long id) {
		return "LM 학습데이터 대량등록 양식";
	}

	public List<String> getHeaders() {
		return Arrays.asList("데이터 구분(*)", "서비스 모델(*)", "학습데이터(*)", "가중치(*)", "설명");
	}

	@Override
	public void makePage(Worksheet ws, int pageStartRowIdx, int sheetIndex, Object searchCondition) throws IOException {

		int row = pageStartRowIdx + 1;
		int c = 0;

		ws.value(row, c++, "일반");
		ws.value(row, c++, "콜봇");
		ws.value(row, c++, "반갑습니다");
		ws.value(row, c++, "100");
		ws.value(row, c, "");

		ws.range(row, 0, row, getHeaders().size() - 1).style().horizontalAlignment("center").set();

		ws.flush();
	}

	@Override
	public String getAmFileName(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void amMakePage(Worksheet ws, int pageIndex, int sheetIndex, Object searchCondition) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getAmSheetName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int amCount(Object searchCondition) {
		// TODO Auto-generated method stub
		return 0;
	}
}
