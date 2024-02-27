package com.kt.smp.common.util;

import java.util.regex.Pattern;

public enum ApiResponseCode {
	RET_CODE("retcode") /**/
	, RET_MESSAGE("retmsg") /**/
	, RET_SYS_MESSAGE("retSysMsg") /**/
	, RET_DATA("data") /**/
	, SUCCESS("OK", "정상적으로 처리 되었습니다.", "SUCCESS") /**/
	, FAIL("FAIL", "예기치 못한 오류가 발생하였습니다. 재 시도 해 주시기 바랍니다.", "fail") /**/
	, PAGE_FAIL("PF00", "페이지를 찾을 수 없습니다.", "fail") /**/
	, ENC_FAIL("EF00", "암복호화 오류", "fail") /**/
	, MAX_FAIL("MAX_FAIL", "최대 개수를 초과할 수 없습니다.", "MAX_FAIL") /**/
	, CHANNEL_FAIL01("FAIL", "채널 하위에 카테고리가 존재하여 삭제 할 수 없습니다.", "FAIL") /**/
	, DUPLICATE_FAIL("FAIL", "중복 된 데이터가 존재합니다.", "FAIL") /**/
//	, PARAM_FAIL("PA00", "필수 파라미터 부족", "fail") /**/
	/*************************************************************************
	 * DX_LOCALPAY : Added
	 *************************************************************************/
	, PARAM_FAIL("P000", "필수 파라미터 부족")
	, PARAM_FAIL03("PA03", "필수 파라미터 오류")
	, PARAM_FAIL04("PA04", "필수 파라미터 오류", "파일이 존재하지 않습니다.")

	/////////////////////////////////////////////////////////////////
	;

	private String retKey;
	private String retCode;
	private String retMsg;
	private String retSysMsg;

	private final String regex = ".*\\{\\d+\\}+.*";

	public String getRetKey() {
		return retKey;
	}

	public void setRetKey(String retKey) {
		this.retKey = retKey;
	}

	public String getRetCode() {
		return retCode;
	}

	public void setRetCode(String retCode) {
		this.retCode = retCode;
	}

	public String getRetMsg(String[] args) {

		if (!Pattern.matches(this.regex, retMsg)) {
			return retMsg;
		}

		int idx = 0;

		String strTemp = retMsg;
		for (String arg : args) {
			strTemp = strTemp.replaceFirst("\\{" + (++idx) + "\\}", arg);
		}

		return strTemp;
	}

	public String getRetMsg() {
		return retMsg;
	}

	public void setRetMsg(String retMsg) {
		this.retMsg = retMsg;
	}

	public String getRetSysMsg(String[] args) {

		if (!Pattern.matches(this.regex, retSysMsg)) {
			return retSysMsg;
		}

		int idx = 0;

		String strTemp = retSysMsg;
		for (String arg : args) {
			strTemp = strTemp.replaceFirst("\\{" + (++idx) + "\\}", arg);
		}

		return strTemp;
	}

	public String getRetSysMsg() {
		return retSysMsg;
	}

	public void setRetSysMsg(String retSysMsg) {
		this.retSysMsg = retSysMsg;
	}

	ApiResponseCode(String retKey) {
		this.retKey = retKey;
	}

	ApiResponseCode(String retCode, String retSysMsg) {
		this.retCode = retCode;
		this.retSysMsg = retSysMsg;
		this.retMsg = retSysMsg;
	}

	ApiResponseCode(String retCode, String retSysMsg, String retMsg) {
		this.retCode = retCode;
		this.retSysMsg = retSysMsg;
		this.retMsg = retMsg;
	}

	public static boolean has(String key) {
		ApiResponseCode[] codes = ApiResponseCode.values();
		for (ApiResponseCode code : codes)
			if (code.name().equals(key))
				return true;
		return false;
	}
}
