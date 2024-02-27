package com.kt.smp.stt.train.trainData.cache;

import com.kt.smp.stt.train.trainData.dto.SttTrainDataBulkSaveReqDto;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

/**
 * @title 학습데이터 대량 등록 캐시
 * @since 2022.05.08
 * @author jieun.chang
 * @see  <pre><pre>
 */
@Getter
public class SttTrainDataBulkCache {

	private boolean isFinished = false;
	private ResultCode resultCode = ResultCode.SUCCESS;
	private int totalCount;
	private int currCount;
	private List<SttTrainDataBulkSaveReqDto> failList = new ArrayList<>();
	private List<SttTrainDataBulkSaveReqDto> successList = new ArrayList<>();

	private List<SttTrainDataBulkSaveReqDto> inputList = new ArrayList<>();

	public boolean isSuccess() {
		if (totalCount != 0 && this.failList.size() == 0) {
			return true;
		}
		return false;
	}

	public boolean isPartialSuccess() {
		return !isSuccess() && successList.size() > 0;
	}

	public enum ResultCode {
		SUCCESS(""),
		NO_FILE_REQUESTED("파일을 업로드해주세요."),
		EMPTY_FILE("파일이 비어있습니다."),
		TOO_LARGE_FILE("입력 데이터 갯수가 500개를 초과하여 처리가 불가능합니다."),
		FILE_SAVE_FAIL("파일 저장에 실패하였습니다"),
		FILE_OPEN_FAIL("파일을 열 수 없습니다."),
		INVALID_REQUEST("유효하지 않은 요청입니다"),
		DELETE_FAIL("삭제에 실패하였습니다.");

		private String message;

		ResultCode(String message) {
			this.message = message;
		}

		public String getMessage() {
			return message;
		}
	}

	public void addCurrentCount() {
		this.currCount++;
	}

	public void setFinished(ResultCode resultCode) {
		isFinished = true;
		this.resultCode = resultCode;
	}

	private void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public void setCurrCount(int currCount) {
		this.currCount = currCount;
	}

	public void setInputList(List<SttTrainDataBulkSaveReqDto> inputList) {
		this.inputList = inputList;
		setTotalCount(inputList.size());
	}
}
