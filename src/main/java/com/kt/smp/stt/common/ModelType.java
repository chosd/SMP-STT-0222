package com.kt.smp.stt.common;

import lombok.Getter;

@Getter
public enum ModelType {
	CLASS(1, "CLASS"),
	SERVICE(2, "SERVICE"),
	E2ELM(3, "E2ELM"),
	E2ESL(4, "E2ESL"),
	E2EMSL(5, "E2EMSL"),
	E2EUSL(6, "E2EUSL");

	private final int code;
	private final String label;

	ModelType(int code, String label) {
		this.code = code;
		this.label = label;
	}

	public static ModelType valueOfLabel(String label) {
		for (ModelType e : values()) {
			if (e.label.equals(label)) {
				return e;
			}
		}
		return null;
	}

	public static boolean validEnum(String label) {

		for (ModelType type : ModelType.values()) {
			if (type.getLabel().equals(label)) {
				return true;
			}
		}

		return false;
	}
}
