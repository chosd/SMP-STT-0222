package com.kt.smp.stt.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @title ResponseVO
 * @since 2022. 03. 29.
 * @author KT AICC 기술개발팀 김민우
 * @see <pre><pre>
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JsonViewResponseVO {
	private int code;
	private String msg;
}
