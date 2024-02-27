/**
 * 
 */
package com.kt.smp.common.util.crypto;

/**
* @FileName : TextDecryptor.java
* @Project : STT_SMP_Service
* @Date : 2023. 10. 12.
* @작성자 : homin.lee
* @변경이력 :
* @프로그램설명 :
*/
public interface TextCrypto {

	public String encrypt(String string);
	public String decrypt(String string);
}
