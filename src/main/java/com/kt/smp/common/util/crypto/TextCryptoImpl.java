package com.kt.smp.common.util.crypto;

import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class TextCryptoImpl implements TextCrypto {
	
	@Value("${calltext.secretKey}")
    private String SECRET_KEY;
	
	private static final String ALGORITHM = "AES";
	private static final String TRANSFORMATION = "AES/ECB/PKCS5Padding";

	public byte[] hexStringToByteArray(final String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }

	@Override
    public String encrypt(String string) {
    	if(SECRET_KEY == null || string == null) {
    		log.error("> SECRET_KEY or string is null ");
    		return string;
    	}
        String retValue = string;

//        log.debug("암호화 전 Data: " + string);
        try {
        	byte[] KEY = hexStringToByteArray(SECRET_KEY);
        	Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(KEY, ALGORITHM));
            byte[] encData = cipher.doFinal(string.getBytes());
            retValue = Base64.getEncoder().encodeToString(encData);
        } catch (Exception e) {
        	log.error("<<< ERROR UnExpected Exception - " + string);
        }
        
        log.debug(">>> after encrypt : " + retValue);
        return retValue;
    }

	@Override
	public String decrypt(String base64Str) {
		if(SECRET_KEY == null || base64Str == null) {
    		log.error("> SECRET_KEY or base64Str is null ");
    		return base64Str;
    	}
        String retValue = base64Str;

        try {
        	byte[] KEY = hexStringToByteArray(SECRET_KEY);
            byte[] encData = Base64.getDecoder().decode(base64Str);
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(KEY, ALGORITHM));
            byte[] decData = cipher.doFinal(encData);
            retValue = new String(decData);

        } catch (Exception e) {
            log.error("<<< ERROR UnExpected Exception - " + base64Str);
//            e.printStackTrace();
        }
        
        log.debug(">>> after decrypt : " + retValue);
        return retValue;
    }
	
}
