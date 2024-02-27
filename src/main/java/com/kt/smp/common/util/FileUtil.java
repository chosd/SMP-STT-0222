package com.kt.smp.common.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * The type File util.
 *
 * @author Brian
 * @title 파일유틸
 * @see <pre> </pre>
 * @since 2022. 01. 29.
 */
@Slf4j
@Component
public class FileUtil extends FileUtils {

	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
	private static DecimalFormat df = new DecimalFormat("#,###");

	/**
	 * @title 디렉터리를 생성한다.
	 * @param rootPath
	 * @return String
	 * @see
	 * <pre>
	 * 전달받은 최상위 디렉터리 하위에 년월로 디렉터리를 생성한다.
	 * 반환값 : 최종 생성 경로
	 *
	 * ex) rootPath/202201
	 * </pre>
	 */
	public static String generateDir(String rootPath) {
		String path = rootPath + sdf.format(new Date()) + "/";
		File dir = new File(path);

		if (!dir.isDirectory()) {
			log.info("Generate Upload Directory : " + path);
			dir.mkdirs();
		} else {
			log.info("Upload Directory Name : " + path);
		}

		return path;
	}

	/**
	 * @title 파일명을 생성한다.
	 * @return String
	 */
	public static String generateFileNm() {
		return UUID.randomUUID().toString();
	}

	/**
	 * @title 허용 가능 확장자인지 확인한다.
	 * @param atOrigNm, arrowExensions
	 * @return boolean
	 */
	public static boolean isAllowExtension(String atOrigNm, String arrowExensions) {
		String fileExtension = StringUtils.lowerCase(FilenameUtils.getExtension(atOrigNm));
		String[] arrExtension = null;

		try {
			arrExtension = arrowExensions.split(",");
		} catch (NullPointerException npe) {
			log.error("isAllowExtension :: 프로퍼티 파일에 허용 가능한 확장자 배열을 선언해 주십시오. ex) png,jpg,gif");
			return false;
		}

		if (StringUtils.isEmpty(fileExtension)) {
			return false;
		}

		if (arrExtension.length == 0) {
			return false;
		}

		for (String arrowExtension : arrExtension) {
			if (arrowExtension.equals(fileExtension)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * @title 업로드된 첨부파일이 허용가능 크기인지 확인한다.
	 * @param atSz, maxSz
	 * @return boolean
	 */
	public static boolean isAllowSize(long atSz, String maxSz) {
		try {
			if (StringUtils.isEmpty(maxSz)) {
				return false;
			} else {
				long longMaxSz = 0;

				if (maxSz.contains("KB")) {
					longMaxSz = Long.parseLong(maxSz.replaceAll("\\D", "")) * ONE_KB;
				} else if (maxSz.contains("MB")) {
					longMaxSz = Long.parseLong(maxSz.replaceAll("\\D", "")) * ONE_MB;
				} else if (maxSz.contains("GB")) {
					longMaxSz = Long.parseLong(maxSz.replaceAll("\\D", "")) * ONE_GB;
				} else if (maxSz.contains("TB")) {
					longMaxSz = Long.parseLong(maxSz.replaceAll("\\D", "")) * ONE_TB;
				} else {
					longMaxSz = Long.parseLong(maxSz.replaceAll("\\D", ""));
				}

				log.info("첨부파일 허용 가능 크기 : " + df.format(longMaxSz) + " Bytes");
				log.info("업로드 첨부파일 크기 : " + df.format(atSz) + " Bytes");

				if (atSz > longMaxSz) {
					return false;
				} else {
					return true;
				}
			}
		} catch (Exception e) {
			log.error("isAllowSize :: " + e.getMessage());
			return false;
		}
	}

	public static synchronized void deleteFile(File file) {
		file.delete();
	}
}
