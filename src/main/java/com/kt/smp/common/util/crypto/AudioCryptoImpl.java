package com.kt.smp.common.util.crypto;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
* @FileName : AudioDecryptorImpl.java
* @Project : STT_SMP_Service
* @Date : 2023. 10. 12.
* @작성자 : homin.lee
* @변경이력 :
* @프로그램설명 : 음원파일 암복호화 객체
*/
@Component
public class AudioCryptoImpl implements AudioCrypto{

	@Value("${wavenc.secretKey}")
    private String SECRET_KEY;
	
	@Value("${wavenc.iv}")
    private String IV;

	private static final String ALGORITHM = "AES";
	private static final String TRANSFORMATION = "AES/CBC/PKCS5Padding";
	private static final String ENC_EXT = "enc";
	
	@Override
	public String encrypt(String filePath) {
		byte[] key = hexStringToByteArray(SECRET_KEY);
        byte[] iv = hexStringToByteArray(IV);

        SecretKeySpec keySpec = new SecretKeySpec(key, ALGORITHM);
        IvParameterSpec ivSpec = new IvParameterSpec(iv);
        try {
            File inputFile = new File(filePath);
            File outputFile = new File(inputFile.getAbsolutePath() + "." +ENC_EXT);
            
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);

            FileInputStream inputStream = new FileInputStream(inputFile);
            FileOutputStream outputStream = new FileOutputStream(outputFile);

            byte[] buffer = new byte[64];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                byte[] output = cipher.update(buffer, 0, bytesRead);
                if (output != null) {
                    outputStream.write(output);
                }
            }
            byte[] outputBytes = cipher.doFinal();
            if (outputBytes != null) {
                outputStream.write(outputBytes);
            }
            inputStream.close();
            outputStream.close();

            inputFile.delete();
            
            return filePath + "." + ENC_EXT;
        } catch (Exception e) {
			throw new RuntimeException("음원 암호화에 실패하였습니다.", e);
        }
		
	}
	
	@Override
	public byte[] getDecryptedByteArray(String filePath) {
		return decrypt(filePath);
	}
	
	private byte[] decrypt(String filePath) {
		byte[] key = hexStringToByteArray(SECRET_KEY);
		byte[] iv = hexStringToByteArray(IV);

		SecretKeySpec keySpec = new SecretKeySpec(key, ALGORITHM);
		IvParameterSpec ivSpec = new IvParameterSpec(iv);

		try {
			File inputFile = new File(filePath);

			Cipher cipher = Cipher.getInstance(TRANSFORMATION);
			cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);

			FileInputStream inputStream = new FileInputStream(inputFile);
			ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
			byte[] buffer = new byte[64];
			int bytesRead;
			while ((bytesRead = inputStream.read(buffer)) != -1) {
				byte[] output = cipher.update(buffer, 0, bytesRead);
				if (output != null) {
					byteOutputStream.write(output);
				}
			}
			
			byte[] outputBytes = cipher.doFinal();
			if (outputBytes != null) {
				byteOutputStream.write(outputBytes);
			}
			inputStream.close();
			byteOutputStream.close();
			return byteOutputStream.toByteArray();
		} catch (Exception e) {
			throw new RuntimeException("음원 복호화에 실패하였습니다.", e);
		}
	}
	
	private byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }

	@Override
	public boolean isEncrypted(String filePath) {
		String ext = filePath.substring(filePath.lastIndexOf(".") + 1);
		return !(ext.equals("mp3") || ext.equals("wav"));
	}

}
