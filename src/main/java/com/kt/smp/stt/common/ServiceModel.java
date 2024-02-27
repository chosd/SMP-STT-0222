package com.kt.smp.stt.common;

import lombok.Getter;

@Getter
public enum ServiceModel {
	CallBot("2", "콜봇"),
	RealTime("3", "실시간"),
	ChatBot("4", "챗봇");

	private final String code;
	private final String label;

	ServiceModel(String code, String label) {
		this.code = code;
		this.label = label;
	}

	public static ServiceModel generateByCode(String code) {
		if ("2".equals(code)) {
			return CallBot;
		}

		if ("3".equals(code)) {
			return RealTime;
		}

		if ("4".equals(code)) {
			return ChatBot;
		}

		throw new IllegalArgumentException("알 수 없는 서비스모델 코드");
	}

	public static ServiceModel valueOfLabel(String label) {
		for (ServiceModel e : values()) {
			if (e.label.equals(label)) {
				return e;
			}
		}
		return null;
	}

	public static boolean validEnum(String label) {
		for (ServiceModel type : ServiceModel.values()) {
			if (type.getLabel().equals(label)) {
				return true;
			}
		}

		return false;
	}
}
