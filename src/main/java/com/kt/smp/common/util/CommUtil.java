package com.kt.smp.common.util;

import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The type Comm util.
 *
 * @author Brian
 * @title 공통 유틸
 * @see <pre> </pre>
 * @since 2022. 01. 29.
 */
public class CommUtil {
	
	/**
	 * @title 첫 번째 페이지를 구한다.
	 * @param int, int
	 * @return int
	 */
	public static Integer getStartPage(Integer pageNum, Integer pageSize) {
		int endPage = getEndPage(pageNum, pageSize);
		return endPage - (pageSize - 1);
	}
	
	/**
	 * @title 마지막 페이지를 구한다.
	 * @param int, int
	 * @return int
	 */
	public static Integer getEndPage(Integer pageNum, Integer pageSize) {
		int endPage = (int) (Math.ceil(pageNum / (double) pageSize) * pageSize);
		return endPage;
	}
	
	/**
	 * @title 브라우저 정보를 가져온다.
	 * @param HttpSevletRequet
	 * @return String
	 */
	public static String getBrowser(HttpServletRequest request) {
		String userAgent = request.getHeader("User-Agent");
		String browser = "Etc";
		
		if(StringUtils.isNotEmpty(userAgent)) {
			if(userAgent.indexOf("Trident") > -1 || userAgent.indexOf("MSIE") > -1) {
				if(userAgent.indexOf("Trident/7") > -1) {
					browser = "Internet Explorer 11";
				} else if(userAgent.indexOf("Trident/6") > -1) {
					browser = "Internet Explorer 10";
				} else if(userAgent.indexOf("Trident/5") > -1) {
					browser = "Internet Explorer 9";
				} else if(userAgent.indexOf("Trident/4") > -1) {
					browser = "Internet Explorer 8";
				} else if(userAgent.indexOf("edge") > -1) {
					browser = "Microsoft Edge";
				} else {
					browser = "Internet Explorer";
				}
			} else if (userAgent.indexOf("Chrome") > -1) {
				browser = "Chrome";
			} else if (userAgent.indexOf("Firefox") > -1) {
				browser = "Firefox";
			} else if (userAgent.indexOf("Opera") > -1) {
				browser = "Opera";
			} else if (userAgent.indexOf("iPhone") > -1 && userAgent.indexOf("Mobile") > -1) {
				browser = "iPhone";
			} else if (userAgent.indexOf("Android") > -1 && userAgent.indexOf("Mobile") > -1) {
				browser = "Android";
			} else if (userAgent.indexOf("Whale") > -1) {
				browser = "Naver Whale";
			}
		}
		
		return browser;
	}
	
	/**
	 * @title 이메일 유효성을 확인한다.
	 * @param String
	 * @return boolean
	 */
	public static boolean isEmail(String email) {
		boolean chk = false;
		String regex = "^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$";   
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(email);
		if(m.matches()) chk = true;
		return chk;
	}
	
	/**
	 * @param str
	 * @return 필터링된 문자열
	 * @title : 문자열의 특수문자를 제거해 필터링
	 * @author : search1
	 * @see <pre></pre>
	 */
	public static String filterString(String str) {
		String match = "[^\uAC00-\uD7A30-9a-zA-Z]";

		if (str == null) {
			return "";
		}
		str = str.replaceAll(match, " ");
		return str;
	}
	
	/**
	* @MethodName : maskText
	* @작성일 : 2023. 10. 12.
	* @작성자 : homin.lee
	* @변경이력 :
	* @Method설명 : 문자열에 포함된 전화번호 중간자리 *로 마스킹
	* @param text 문자열
	* @return 마스킹된 문자열 ex) 010-****-1234
	*/
	public static String maskPhoneNumberInText(String text) {
		String regex = "\\b01[0-9]-[0-9]{3,4}-[0-9]{4}\\b";
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(text);

		while (matcher.find()) {
            String phoneNumber = matcher.group();
            // 휴대폰 번호에서 중간 번호 부분을 '*'로 대체
            String[] parts = phoneNumber.split("-");
            if (parts.length == 3) {
            	String middlePart = parts[1];
                String maskedMiddlePart = "*".repeat(middlePart.length());
                String maskedPhoneNumber = parts[0] + "-" + maskedMiddlePart + "-" + parts[2];
                text = text.replace(phoneNumber, maskedPhoneNumber);
            }
        }

        return text;
	}
}
