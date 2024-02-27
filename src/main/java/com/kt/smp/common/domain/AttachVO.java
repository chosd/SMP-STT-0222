package com.kt.smp.common.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @title  첨부VO
 * @author Brian
 * @since  2022. 01. 29.
 * @see
 * <pre>
 * </pre>
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttachVO {

	private String atSeq;										// 첨부시퀀스
	private String atNo;										// 첨부순번
	private String atPath;										// 첨부경로
	private String atOrigNm;									// 원본파일명
	private String atSaveNm;									// 저장파일명
	private String atSz;										// 첨부크기
	private String regDt;										// 등록일시
	private String regId;										// 등록아이디
	private String regIp;										// 등록아이피
	private String updDt;										// 수정일시
	private String updId;										// 수정아이디
	private String updIp;										// 수정아이피
	private String useYn;										// 사용여부
	
}