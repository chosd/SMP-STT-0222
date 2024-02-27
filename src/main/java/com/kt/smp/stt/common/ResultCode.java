package com.kt.smp.stt.common;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

/**
 * @title ResultCode
 * @since 2022. 04. 01.
 * @author KT AICC 기술개발팀 김민우
 * @see <pre><pre>
 */
@Getter
public enum ResultCode {
	SUCCESS("0000", "성공"),

	//요청 오류
	MISSING_REQUIRED_PARAMETER("4000", "필수 파라미터가 누락되었습니다"),
	SERVICE_CODE_NOT_FOUND("4100", "제공되지 않는 서비스모델입니다"),
	INVALID_DATE_FORMAT("4110", "유효하지 않은 날짜 형식입니다"),
	FILE_NOT_FOUND("4200", "전달받은 경로에 파일 없습니다"),
	FILE_READ_PERMISSION_NOT_ALLOWED("4250", "NAS에 위치한 파일 읽기 권한 없습니다"),
	FILE_INTEGRITY_VIOLATION("4300", "파일 무결성이 위배되었습니다(md5 hash checksum 값 다름)"),
	LANGUAGE_MODEL_TYPE_NOT_FOUND("4400", "지원하지 않는 LM Type으로 학습요청 되었습니다"),
	UPLOAD_FILE_SIZE_EXCEED("4500", "요청파일의 크기가 MAX 파일 사이즈보다 큽니다"),
	CONTENT_TYPE_NOT_SUPPORTED("4510", "지원하지 않는 Content-Type 입니다"),
	CHECK_TRAIN_STATUS("4600", "학습이 완료되지 않았습니다"),

	//리소스 관련 오류
	ALREADY_IN_USE_TRAINING("4900", "해당 서비스모델 학습중이므로 요청이 불가능합니다"),
	ALREADY_IN_USE_DEPLOYING("4910", "해당 서비스모델 배포중이므로 요청이 불가능합니다"),
	ALREADY_IN_USE_VERIFYING("4920", "해당 서비스모델로 검증중이므로 요청이 불가능합니다"),
	ALREADY_IN_USE_TESTING("4930", "해당 서비스모델로 단건 테스트중이므로 요청이 불가능합니다"),
	ALL_TRAINING_SERVER_IN_USE("4999", "모든 학습서버가 학습중이므로 학습요청이 불가능합니다"),

	//서버 에러
	INTERNAL_SERVER_ERROR("5000", "서버 오류"),
	MULTIPART_CONVERTING_ERROR("5001", "Multipart --> File 변환 오류가 발생했습니다"),
	FILE_DOWNLOAD_ERROR("5002", "파일 다운로드 오류가 발생했습니다"),

	//학습 오류
	INSUFFICIENT_TRAINING_SERVER_STORAGE("6100", "Train Server 저장공간이 부족합니다"),
	NAS_TRAIN_DATA_FILE_COPY_ERROR("6110", "NAS에 위치한 학습 데이터 복사 오류가 발생했습니다"),
	NAS_MODEL_FILE_UPLOAD_ERROR("6120", "SRU에서 NAS로 모델 업로드 실패했습니다"),
	TRAINING_ERROR("6200", "학습 실패했습니다"),
	INSUFFICIENT_NAS_STORAGE("6300", "NAS 저장공간이 부족합니다"),

	//배포 오류
	INSUFFICIENT_SRU_SERVER_STORAGE("7100", "SRU 저장공간이 부족합니다"),
	NAS_MODEL_COPY_ERROR("7110", "NAS에 위치한 모델 복사 오류가 발생했습니다"),
	MODEL_SETTING_ERROR("7200", "배포 설정파일 오류가 발생했습니다(KEP.ini 설정파일 작성오류)"),
	MODEL_LOAD_FAIL("7250", "모델 배포시 timeout 발생했습니다"),
	DEPLOYING_TESTING_ERROR("7300", "배포시 내부 인식시험 실행 실패했습니다"),
	SCU_SRU_CONNECTION_FAIL("7400", "배포 후 SCU, SRU 연결 실패했습니다"),

	//검증 오류
	VERIFY_INPUT_DATA_ERROR("8100", "검증 Input 파일에 오류가 있습니다(검증 데이터 or 정답지)"),
	NAS_WAV_DIR_COPY_ERROR("8110", "NAS에 위치한 검증 데이터 복사 오류가 발생했습니다"),
	NAS_ANSWER_FILE_COPY_ERROR("8120", "NAS에 위치한 정답지 복사 오류가 발생했습니다"),
	VERIFY_SFP_PROCESS_DEAD("8200", "검증 내부 프로세스 오류가 발생했습니다(SFP 프로세스 다운)"),
	STT_RESULT_ERROR("8210", "STT 결과 파일 오류가 발생했습니다"),

	//단건테스트 오류
	TEST_INPUT_DATA_ERROR("8300", "단건 테스트 input 파일 오류가 발생했습니다"),
	NAS_WAV_FILE_COPY_ERROR("8310", "NAS에 위치한 단건 테스트 WAV파일 복사 오류가 발생했습니다"),
	VBRGW_PROCESS_DEAD("8400", "단건 테스트 내부 프로세스 오류가 발생했습니다(vbrgw 오류)"),
	SRU_CONNECTION_FAIL("8410", "SCU-SRU간 Connection 연결 실패했습니다"),
	
	//신뢰도 전달
	STT_SVCRTE_ERROR("8501", "STT 엔진 내부 오류"),
	STT_RTP_DISCONN("8502", "Client-STT 엔진간 통신 오류"),
	
	//HW 리소스
	HW_RESOURCE_ERROR("8601", "HW 리소스 정보 조회시 내부 오류 발생"),
	
	//신뢰도 음원 저장
	CONFIDENCE_START_ERROR("8700", "신뢰도 음원 파일 저장 시작 오류"),
	CONFIDENCE_END_ERROR("8701", "신뢰도 음원 파일 저장 종료 오류"),
	CONFIDENCE_UPDATE_ERROR("8702", "신뢰도 설정 값 수정 오류"),
	CONFIDENCE_READ_ERROR("8703", "신뢰도 설정 값 조회 오류"),
	
	//통계정보 오류
	STATISTICS_DATA_NOT_FOUND("8800", "해당 SVC의 통계정보를 찾을 수 없습니다"),
	STATISTICS_DATA_PARSING_ERROR("8810", "통계정보 파싱 오류가 발생했습니다"),

	//채널정보 오류
	CHANNEL_DATA_NOT_FOUND("8900", "해당 SVC의 채널정보를 찾을 수 없습니다"),
	CHANNEL_DATA_PARSING_ERROR("8910", "채널정보 파싱 오류가 발생했습니다"),
	CHANNEL_INFO_ASYNC_REQUEST_ERROR("8920", "채널정보 조회 관련 비동치 처리 오류가 발생했습니다"),

	//기타
	UNKNOWN_DEPLOY_AGENT_SERVER_ERROR("9996", "배포 Agent 알 수 없는 서버 오류가 발생했습니다"),
	UNKNOWN_TRAIN_AGENT_SERVER_ERROR("9997", "학습 Agent 알 수 없는 서버 오류"),
	STT_AGENT_SERVER_ERROR("9998", "Agent Server 통신오류"),
	UNKNOWN_SERVER_ERROR("9999", "기타 알 수 없는 서버 오류");

	private final String code;
	
	@JsonValue
	private final String description;

	ResultCode(String code, String description) {
		this.code = code;
		this.description = description;
	}

	public static ResultCode findByCode(String code) {
		return Arrays.stream(values())
			.filter(resultCode -> resultCode.getCode().equals(code))
			.findFirst()
			.orElse(UNKNOWN_SERVER_ERROR);
	}
}
