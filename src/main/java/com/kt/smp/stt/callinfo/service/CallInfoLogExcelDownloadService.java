/**
 * 
 */
package com.kt.smp.stt.callinfo.service;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;
import org.dhatim.fastexcel.Color;
import org.dhatim.fastexcel.Workbook;
import org.dhatim.fastexcel.Worksheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.github.pagehelper.PageHelper;
import com.kt.smp.fileutil.constant.ExcelConstants;
import com.kt.smp.stt.callinfo.dto.CallInfoLogVO;
import com.kt.smp.stt.callinfo.repository.SttCallInfoRepository;

import lombok.RequiredArgsConstructor;

/**
*@FileName : CallInfoExcelList.java
@Project : kt-stt-service_r
@Date : 2023. 10. 27.
*@작성자 : sooyeon.shim
*@변경이력 :
*@프로그램설명 :
*/
@RequiredArgsConstructor
@Component
public class CallInfoLogExcelDownloadService{
	
	@Autowired
	private final SttCallInfoRepository sttCallInfoRepository;
	
    private final static int PAGE_SIZE = 10000;

    private final static int FLUSH_SIZE = 500;

    public final static int MAX_ROWS_PER_SHEET = 1040000;

    public static int getPageSize() {
        return PAGE_SIZE;
    }

    public static int getFlushSize() {
        return FLUSH_SIZE;
    }

    public int count(String applicationId) {
        return sttCallInfoRepository.countCallInfoLogByApplicationId(applicationId);
    }

    public String getSheetName() {
        return "상담 내역 청취 리스트";
    }

    public String getFileName(String applicationId) {
        return applicationId + "_STT변환결과";
    }
    
    public List<String> getHeaders() {
        return Arrays.asList("No", "STT ID", "화자", "Application ID", "STT_SEQ", "시작 시간", "종료 시간", "STT 인식 결과", "신뢰도", "EPD 시작 시간", "EPD 종료 시간");
    }
    
	public void createWorksheet(OutputStream os, String applicationId, List<String> headers) throws IOException {
		Workbook wb = new Workbook(os, "ExcelApplication", "1.0");
		//행 개수
		int count = sttCallInfoRepository.countCallInfoLogByApplicationId(applicationId);
		
		//시트 설정
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

				makePage(ws, pageStartRowIdx, sheetIdx, applicationId);
			}

			ws.flush();
			ws.finish();
		}

		wb.finish();
	}

    public void makePage(Worksheet ws, int pageStartRowIdx, int sheetIndex, String applicationId) throws IOException {
        PageHelper.startPage(
                pageStartRowIdx / getPageSize() + (sheetIndex * ExcelConstants.MAX_ROWS_PER_SHEET / getPageSize()) + 1,
                getPageSize());
        
        List<CallInfoLogVO> results = sttCallInfoRepository.getCallInfoLogListByApplicationId(applicationId);

        int row = pageStartRowIdx + 1;

        for (CallInfoLogVO callInfoDetail : results) {
            ws.value(row, 0, callInfoDetail.getSttResultIdx().toString());
            ws.value(row, 1, callInfoDetail.getSttId());
            ws.value(row, 2, callInfoDetail.getSpeakerType());
            ws.value(row, 3, callInfoDetail.getApplicationId());
            ws.value(row, 4, callInfoDetail.getSttSeq().toString());
            ws.value(row, 5, callInfoDetail.getStartTimeStamp().toString());
            ws.value(row, 6, callInfoDetail.getEndTimeStamp().toString());
            ws.value(row, 7, callInfoDetail.getSttText());
            ws.value(row, 8, callInfoDetail.getConfidence());
            ws.value(row, 9, callInfoDetail.getStartTime());
            ws.value(row, 10, callInfoDetail.getEndTime());
           
            ws.range(row, 0, row, getHeaders().size() - 1).style().horizontalAlignment("center").set();

            if (++row % getFlushSize() == 0) {
                ws.flush();
            }
        }
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
