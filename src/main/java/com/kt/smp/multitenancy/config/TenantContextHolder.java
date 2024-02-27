package com.kt.smp.multitenancy.config;

public class TenantContextHolder {

	private static ThreadLocal<String> CONTEXT = new ThreadLocal<>();

	public static void set(String projectCode) {

		CONTEXT.set(projectCode);
	}


	public static String getProjectCode() {

		return CONTEXT.get();
	}

	public static void clear() {

		CONTEXT.remove();
	}
}
