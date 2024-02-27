/**
 * 
 */
package com.kt.smp.stt.reprocess.dto;


import lombok.Data;
import lombok.NoArgsConstructor;

/**
* @FileName : SttSemiRealTimeResponseDto.java
* @Project : STT_SMP_Service
* @Date : 2023. 10. 24.
* @작성자 : homin.lee
* @변경이력 :
* @프로그램설명 :
*/
@Data
@NoArgsConstructor
public class SttSemiRealTimeResponseDto {
    private String resultCode; 			// 결과 코드
    private String resultMsg; 			// STT 요청 결과
    private String serviceCode; 		// serviceCode 이름
    
    /*
     * < 준실시간 처리 id >
     * 요청 값으로 넘긴 callKey, txRxType, serviceCode, serverId의 조합
     * callkey, txRxType, serviceCode, serverId는 “@”로 구분
     * Ex) 550….4-A716-446655440000@1@2@cfabd212
     * serverId는 8글자로 구성, 문자열에 포함될 수 있는 값 : 0~9(숫자), a~f(영문자)
     * */
    private String srtId; 				
}
