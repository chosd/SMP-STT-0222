package com.kt.smp.config;

import java.net.InetAddress;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CommonConstants {

	public static final String X_SMP_PROJECT_CODE = "x-smp-project-code";
	public static final String REG_ID = "system";
	public static String LOCAL_IP = "localhost";
	public static final String LOCAL_PROFILE = "local";
	public static final String DEV_PROFILE = "dev";
	
	static {
		try {
			InetAddress addr = InetAddress.getLoopbackAddress();
			if(addr != null) {
				LOCAL_IP = addr.getHostAddress();
			}
		} catch(Exception e) { log.error("<<<< error from InetAddress : " + e.getMessage()); }
	}
}
