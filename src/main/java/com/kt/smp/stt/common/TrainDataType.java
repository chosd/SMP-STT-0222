package com.kt.smp.stt.common;

import lombok.Getter;

@Getter
public enum TrainDataType {
	COMMON(1, "일반"),
	DICTATION(2, "전사"),
	EXTERNAL(3, "외부");

	private final int code;
	private final String label;

	TrainDataType(int code, String label) {
		this.code = code;
		this.label = label;
	}

	public static TrainDataType valueOfLabel(String label) {
		for (TrainDataType e : values()) {
			if (e.label.equals(label)) {
				return e;
			}
		}
		return null;
	}

	public static boolean validEnum(String label) {

		for (TrainDataType type : TrainDataType.values()) {
			if (type.getLabel().equals(label)) {
				return true;
			}
		}

		return false;
	}
}
