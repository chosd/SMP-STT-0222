/**
 * 
 */
package com.kt.smp.stt.comm.preference.dto;

import lombok.Data;

@Data
public class EncryptConfigDto {
	private String textEncrypt;				// 텍스트 데이터 암호화 유무 ("Y" or "N")
	private String wavEncrypt;				// 음원 데이터 암호화 유무 ("Y" or "N")
}
