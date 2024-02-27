package com.kt.smp.common.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigInteger;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * @title  암호화 유틸
 * @author Brian
 * @since  2022. 02. 05.
 */
@Slf4j
public class EncUtil {
	
	public static String encAES128(String input, String passprase) {
		String encrypted = "";
		
		try {
			SecretKeySpec ks = (SecretKeySpec) generateKey(passprase);
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, ks);
			byte[] encryptedBytes = cipher.doFinal(input.getBytes());
			String base64 = Base64.encodeBase64String(encryptedBytes);
			encrypted = new String(base64);
		} catch (Exception e) {
			log.error(e.toString());
		}

		return encrypted;
	}
	
	public static String decAES128(String input, String passprase) throws Exception {
		SecretKeySpec ks = (SecretKeySpec) generateKey(passprase);

		Cipher c = Cipher.getInstance("AES/ECB/PKCS5Padding");
		c.init(Cipher.DECRYPT_MODE, ks);
		    
		byte[] byteStr = Base64.decodeBase64(input.getBytes("UTF-8"));
		String decStr = new String(c.doFinal(byteStr), "UTF-8");

		return decStr;
	}
	
	public static Key generateKey(String key) throws Exception {
		Key keySpec;

		byte[] keyBytes = new byte[16];
		byte[] b = key.getBytes("UTF-8");

		int len = b.length;
		if(len > keyBytes.length) {
			len = keyBytes.length;
		}

		System.arraycopy(b, 0, keyBytes, 0, len);
		keySpec = new SecretKeySpec(keyBytes, "AES");
		return keySpec;
	}
	
	/**
	 * @title  SHA-1
	 * @param  String
	 * @return String
	 */
	public static String getSHA1(String input) {
		String result = null;
		
		try {
		    MessageDigest digest = MessageDigest.getInstance("SHA-1");
		    digest.reset();
		    digest.update(input.getBytes("utf8"));
		    result = String.format("%064x", new BigInteger(1, digest.digest()));
		} catch (Exception e) {
		    log.error(e.toString());
		}
		
		return result;
	}
	
	/**
	 * @title  SHA-256
	 * @param  String
	 * @return String
	 */
	public static String getSHA256(String input) {
		String result = null;
		
		try {
		    MessageDigest digest = MessageDigest.getInstance("SHA-256");
		    digest.reset();
		    digest.update(input.getBytes("utf8"));
		    result = String.format("%064x", new BigInteger(1, digest.digest()));
		} catch (Exception e) {
		    log.error(e.toString());
		}
		
		return result;
	}
	
	/**
	 * @title  SHA-512
	 * @param  String
	 * @return String
	 */
	public static String getSHA512(String input) {
		String result = null;
		
		try {
		    MessageDigest digest = MessageDigest.getInstance("SHA-512");
		    digest.reset();
		    digest.update(input.getBytes("utf8"));
		    result = String.format("%0128x", new BigInteger(1, digest.digest()));
		} catch (Exception e) {
		    log.error(e.toString());
		}
		
		return result;
	}
	
	public static String passwordDecrypt(String ciphertext, String passphrase) {
        try {
            final int keySize = 256;
            final int ivSize = 128;

            byte[] ctBytes = Base64.decodeBase64(ciphertext.getBytes("UTF-8"));
            byte[] saltBytes = Arrays.copyOfRange(ctBytes, 8, 16);
            byte[] ciphertextBytes = Arrays.copyOfRange(ctBytes, 16, ctBytes.length);
            byte[] key = new byte[keySize / 8];
            byte[] iv = new byte[ivSize / 8];
            EvpKDF(passphrase.getBytes("UTF-8"), keySize, ivSize, saltBytes, key, iv);

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key, "AES"), new IvParameterSpec(iv));
            byte[] recoveredPlaintextBytes = cipher.doFinal(ciphertextBytes);
 
            return new String(recoveredPlaintextBytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
 
        return null;
    }
    
    private static byte[] EvpKDF(byte[] password, int keySize, int ivSize, byte[] salt, byte[] resultKey, byte[] resultIv) throws NoSuchAlgorithmException {
        return EvpKDF(password, keySize, ivSize, salt, 1, "MD5", resultKey, resultIv);
    }
 
    private static byte[] EvpKDF(byte[] password, int keySize, int ivSize, byte[] salt, int iterations, String hashAlgorithm, byte[] resultKey, byte[] resultIv) throws NoSuchAlgorithmException {
        keySize = keySize / 32;
        ivSize = ivSize / 32;
        int targetKeySize = keySize + ivSize;
        byte[] derivedBytes = new byte[targetKeySize * 4];
        int numberOfDerivedWords = 0;
        byte[] block = null;
        MessageDigest hasher = MessageDigest.getInstance(hashAlgorithm);
        while (numberOfDerivedWords < targetKeySize) {
            if (block != null) {
                hasher.update(block);
            }
            hasher.update(password);            
            block = hasher.digest(salt);
            hasher.reset();
            for (int i = 1; i < iterations; i++) {
                block = hasher.digest(block);
                hasher.reset();
            }
            System.arraycopy(block, 0, derivedBytes, numberOfDerivedWords * 4, Math.min(block.length, (targetKeySize - numberOfDerivedWords) * 4));
            numberOfDerivedWords += block.length / 4;
        }
        System.arraycopy(derivedBytes, 0, resultKey, 0, keySize * 4);
        System.arraycopy(derivedBytes, keySize * 4, resultIv, 0, ivSize * 4);
        return derivedBytes;
    }  
    
    public static void main(String[] args) {
    	EncUtil encUtil = new EncUtil();
    	
    	String enc = encUtil.encAES128("ktai123#", "4F8ABBD4EE68E655F42146E87D6E4022");
    	
    	System.out.println(enc);
    	
    }
}
