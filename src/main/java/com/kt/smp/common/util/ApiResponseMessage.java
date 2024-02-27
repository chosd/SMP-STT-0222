package com.kt.smp.common.util;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;

import java.util.Map;

public class ApiResponseMessage {
	private String retcode;
	private String retsysmsg;
	private String retmsg;
	private Object data;

	public ApiResponseMessage() {
		this.retcode = ApiResponseCode.SUCCESS.getRetCode();
		this.retmsg = ApiResponseCode.SUCCESS.getRetSysMsg();
		this.retsysmsg = ApiResponseCode.SUCCESS.getRetSysMsg();
	}

	public ApiResponseMessage(ApiResponseCode returnStr) {
		this.retcode = returnStr.getRetCode();
		this.retsysmsg = returnStr.getRetSysMsg();
		this.retmsg = returnStr.getRetMsg();

		/*
		 * if (StringUtils.isEmpty(returnStr.getRetMsg())) { this.retmsg =
		 * returnStr.getRetSysMsg(); } else if (returnStr.getRetMsg().equals("fail")) {
		 * this.retmsg = ApiResponseCode.FAIL.getRetSysMsg(); } else if
		 * (returnStr.getRetMsg().equals("fail2")) { this.retmsg =
		 * ApiResponseCode.PARAM_FAIL.getRetSysMsg(); } else { this.retmsg =
		 * returnStr.getRetMsg(); }
		 */
	}

	public ApiResponseMessage(ApiResponseCode returnStr, String[] args) {
		this.retcode = returnStr.getRetCode();
		this.retsysmsg = returnStr.getRetSysMsg(args);

		/*
		 * if (StringUtils.isEmpty(returnStr.getRetMsg())) { this.retmsg =
		 * returnStr.getRetSysMsg(); } else if (returnStr.getRetMsg().equals("fail")) {
		 * this.retmsg = ApiResponseCode.FAIL.getRetSysMsg(); } else if
		 * (returnStr.getRetMsg().equals("fail2")) { this.retmsg =
		 * ApiResponseCode.PARAM_FAIL.getRetSysMsg(); } else { this.retmsg =
		 * returnStr.getRetMsg(args); }
		 */
	}

	public ApiResponseMessage(Object data) {
		this.retcode = ApiResponseCode.SUCCESS.getRetCode();
		this.retmsg = ApiResponseCode.SUCCESS.getRetMsg();
		this.retsysmsg = ApiResponseCode.SUCCESS.getRetSysMsg();
		this.data = data;
	}

	public ApiResponseMessage(ApiResponseCode returnStr, Map<String, String> map) {
		this.retcode = returnStr.getRetCode();
		this.retsysmsg = returnStr.getRetSysMsg();

		/*
		 * if (StringUtils.isEmpty(returnStr.getRetMsg())) { this.retmsg =
		 * returnStr.getRetSysMsg(); } else if (returnStr.getRetMsg().equals("fail")) {
		 * this.retmsg = ApiResponseCode.FAIL.getRetSysMsg(); } else if
		 * (returnStr.getRetMsg().equals("fail2")) { this.retmsg =
		 * ApiResponseCode.PARAM_FAIL.getRetSysMsg(); } else { this.retmsg =
		 * returnStr.getRetMsg(); }
		 */

		this.data = map;
	}

	public ApiResponseMessage(String code, String msg) {
		this.retcode = code;
		if ( msg != null ) { //smp null_return 대비
			this.retsysmsg = msg;
		} else {
			this.retsysmsg = "";
		}
		this.retmsg = msg;
	}

	public ApiResponseMessage(String code, String sysMsg, String msg) {
		this.retcode = code;
		this.retsysmsg = sysMsg;
		this.retmsg = msg;
	}

	public ApiResponseMessage(String code, String sysMsg, String msg, Object data) {
		this.retcode = code;
		this.retsysmsg = sysMsg;
		this.retmsg = msg;
		this.data = data;
	}

	@ApiModelProperty(notes = "retcode", required = true)
	public String getRetcode() {
		return retcode;
	}

	@ApiModelProperty(notes = "retsysmsg", required = true)
	public String getRetSysMsg() {
		return retsysmsg;
	}

	@ApiModelProperty(notes = "retmsg", required = true)
	public String getRetmsg() {
		return retmsg;
	}

	@JsonProperty(required = true)
	@ApiModelProperty(notes = "json data", required = true)
	public Object getData() {
		return data;
	}

}
