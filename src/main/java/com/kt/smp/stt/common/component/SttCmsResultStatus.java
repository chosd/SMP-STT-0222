package com.kt.smp.stt.common.component;

/**
 * @title STT CMS 공통 응답코드
 * @since 2022.02.21
 * @author soohyun
 * @see <pre></pre>
 */
public enum SttCmsResultStatus {
	// 성공
	SUCCESS("0000", "Success", "성공"),
	PARTIAL_SUCCESS("0001", "Partial Success", "일부 성공"),
	IN_PROGRESS("0002", "In Progress", "진행중"),

	// 클라이언트 에러 대역
	BAD_REQUEST("1000", "Bad Request", "Bad Request"),
	INVALID_PARAMETER("1001", "Invalid Parameter", "요청값이 유효하지 않음"),
	DATA_DUPLICATED("1002", "Data Duplicated", "이미 등록된 데이터"),
	NO_DATA("1003", "No Such Data", "해당 정보가 DB에 없음"),
	NO_SERVICE("1004", "No Such Service", "요청한 서비스가 DB에 없음"),
	NO_SERVER("1005", "No Such Server", "요청한 서버가 DB에 없음"),
	NO_SPEAKERS_AVAILABLE("1006", "No Speakers Avaliable", "사용 가능한 스피커가 없음"),
	INVALID_WAV_FILE("1007", "Invalid Wav File", "잘못된 WAV 파일 요청"),
	NO_FILE("1008", "No Such File", "요청한 파일이 경로에 없음"),
	NO_PROCESS_IN_PROGRESS("1009", "No Process In Progress", "진행 중인 작업 없음"),
	WAV_FILE_HEADER_MISMATCH("1010", "Wav File Header Mismatch", "WAV 헤더 불일치"),

	// 서버 내부 에러 대역
	INTERNAL_SERVER_ERROR("3000", "Internal Server Error", "Internal Server Error"),
	DB_SAVE_ERROR("3001", "DB Save Error", "DB 저장 오류"),
	FILE_SAVE_ERROR("3002", "File Save Error", "파일 저장 오류"),
	DEPENDENT_DATA_FOUND("3003", "Dependent Data Found", "연관된 데이터 존재하여 수정/삭제 불가"),
	EXCEL_CREATE_ERROR("3004", "Excel Create Error", "엑셀 파일 생성 오류"),

	// 외부 시스템 연동 에러 대역
	EXTERNAL_CONNECTION_ERROR("4000", "External Connection Error", "외부 연동 오류"),
	NO_DEPLOY_SERVER("4001", "No Tcs Servers", "배포 가능한 TCS 서버 없음"),
	REQUEST_GENERATION_ERROR("4002", "Request Generation Error", "요청 생성 중 오류"),
	RESPONSE_PARSE_ERROR("4003", "Response Parse Error", "응답 생성 중 오류"),
	NULL_RESPONSE("4004", "Null Response", "API 응답 null"),
	INVALID_RESPONSE("4005", "Invalid Response", "비정상 응답"),
	CONNECTION_FAILED("4006", "Connection Failed", "연결 실패"),

	FAIL("9999", "Fail", ""),

	;

	private String resultCode;
	private String resultMessage;
	private String description; // 한국어 에러 설명

	SttCmsResultStatus(String resultCode, String resultMessage, String description) {
		this.resultCode = resultCode;
		this.resultMessage = resultMessage;
		this.description = description;
	}

	public String getResultCode() {
		return resultCode;
	}

	public String getResultMessage() {
		return resultMessage;
	}

	public String getDescription() {
		return description;
	}
}

