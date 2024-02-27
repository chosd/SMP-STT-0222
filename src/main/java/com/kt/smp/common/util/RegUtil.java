package com.kt.smp.common.util;

import java.nio.file.Path;
import java.util.regex.Pattern;

/**
 * @title 정규식 유틸
 * @since 2022.03.03
 * @author soohyun
 * @see  <pre></pre>
 */
public class RegUtil {

	/**
	 * @title 패턴 적용하여 정규식의 결과 반환
	 * @author soohyun
	 * @param str str
	 * @param pattern pattern
	 * @return boolean
	 * @date 2022.03.04
	 * @see  <pre></pre>
	 */
	public static boolean match(String str, String pattern) {
		return Pattern.matches(pattern, str);
	}

	/**
	 * @title 디렉토리 영문 숫자 /\-_만 허용
	 * @author soohyun
	 * @param dir dir
	 * @return boolean
	 * @date 2022.03.04
	 * @see  <pre></pre>
	 */
	public static boolean checkDir(String dir) {
		if (dir == null) {
			return false;
		}

		if (dir.indexOf("..") >= 0) {
			return false;
		}

		if (dir.indexOf("\\") >= 0) {
			return false;
		}

		String pattern = "^[\\\\\\/A-Za-z0-9\\-\\_]+$";
		return match(dir, pattern);
	}

	/**
	 * @title Check file name
	 * @author soohyun
	 * @param name name
	 * @return boolean
	 * @date 2022.03.04
	 * @see  <pre></pre>
	 */
	public static boolean checkFileName(String name) {
		if (name == null) {
			return false;
		}

		if (name.indexOf("..") >= 0) {
			return false;
		}

		String pattern = "^[\\s\\.A-Za-z0-9\\-\\_]+$";
		return match(name, pattern);
	}

	public static boolean checkIpv4(String ipv4) {
		if (ipv4 == null) {
			return false;
		}

		String pattern = "^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$";
		return match(ipv4, pattern);
	}

	/**
	 *
	 * @title 압축 파일 ZIP 파일 명이 알맞은 형식인지 확인
	 * @author soohyun
	 * @param path path
	 * @return boolean
	 * @date 2022.03.04
	 * @see  <pre></pre>
	 */
	public static boolean isInvalidZipFileName(Path path) {
		String fileNameWithExtension = path.getFileName().toString();
		int index = fileNameWithExtension.lastIndexOf(".");

		if (index == -1) {
			return true;
		}

		String fileName = fileNameWithExtension.substring(0, index);
		String extenstion = fileNameWithExtension.substring(index + 1);

		if (fileName.matches("[^0-9a-zA-Z\\s\\.\\-\\_]") || !extenstion.equalsIgnoreCase("zip")) {
			return true;
		}

		return false;
	}

	/**
	 * @title 자주 사용하는 정규식 모음
	 * @since 2022.03.04
	 * @author soohyun
	 * @see  <pre></pre>
	 */
	public enum RegexDictionary {
		/**
		 *Numeric regex dictionary.
		 */
		NUMERIC("숫자", Pattern.compile("^[0-9]*$")),
		/**
		 *Alpha regex dictionary.
		 */
		ALPHA("영문자", Pattern.compile("^[a-zA-Z]*$")),
		/**
		 *Alpha numeric regex dictionary.
		 */
		ALPHA_NUMERIC("영숫자", Pattern.compile("^[0-9|a-z|A-Z]*")),
		/**
		 *Hangul regex dictionary.
		 */
		HANGUL("한글", Pattern.compile("^[가-힣]*$")),
		/**
		 *Email regex dictionary.
		 */
		EMAIL("이메일",
			Pattern.compile("^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$")),
		/**
		 *Special char regex dictionary.
		 */
		SPECIAL_CHAR("특수문자", Pattern.compile("[<>@!#$%^&*()_+\\[\\]{}?:;|\\'\\\",.\\/~`\\-=\\\\]+$")),
		;

		private String name;
		private Pattern pattern;

		RegexDictionary(String name, Pattern pattern) {
			this.name = name;
			this.pattern = pattern;
		}

		/**
		 * @title Get name string
		 * @author soohyun
		 * @return string
		 * @date 2022.03.04
		 * @see  <pre></pre>
		 */
		public String getName() {
			return name;
		}

		/**
		 * @title Get pattern pattern
		 * @author soohyun
		 * @return pattern
		 * @date 2022.03.04
		 * @see  <pre></pre>
		 */
		public Pattern getPattern() {
			return pattern;
		}
	}
}
