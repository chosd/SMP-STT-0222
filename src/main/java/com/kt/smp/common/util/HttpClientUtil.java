package com.kt.smp.common.util;

import lombok.extern.slf4j.Slf4j;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * @title http client를 위한 util.
 * @since 2022.02.16
 * @author soohyun
 * @see  <pre></pre>
 */
@Slf4j
public class HttpClientUtil {
	private static final int DEFAULT_TIME_OUT = 2000;
	private static final String PROTOCOL_HTTP = "http://";
	private static final String PROTOCOL_HTTPS = "https://";

	/**
	 * @title url 생성
	 * @author soohyun
	 * @param domain 도메인
	 * @param path path
	 * @param port 포트
	 * @return String 타입의 url
	 * @date 2022.02.16
	 * @see  <pre></pre>
	 */
	public static String makeUrl(String domain, String path, int port) {

		boolean isProtocolAppend = true;
		if (domain != null && (domain.toLowerCase().startsWith(PROTOCOL_HTTP) || domain.toLowerCase()
			.startsWith(PROTOCOL_HTTPS))) {
			isProtocolAppend = false;
		}

		if (port == 80) {
			return isProtocolAppend ? PROTOCOL_HTTP + domain + path : domain + path;
		} else {
			return isProtocolAppend ? PROTOCOL_HTTP + domain + ":" + port + path : domain + ":" + port + path;
		}
	}

	/**
	 * @title HTTPS 여부를 받아 url 생성
	 * @author soohyun
	 * @param useHttps use https
	 * @param domain domain
	 * @param path path
	 * @param port port
	 * @return String 타입의 url
	 * @date 2022.05.18
	 * @see  <pre></pre>
	 */
	public static String makeUrl(boolean useHttps, String domain, String path, int port) {
		String protocol = useHttps ? PROTOCOL_HTTPS : PROTOCOL_HTTP;

		boolean isProtocolAppend = true;
		if (domain != null && (domain.toLowerCase().startsWith(protocol))) {
			isProtocolAppend = false;
		}

		if (port == 80) {
			return isProtocolAppend ? protocol + domain + path : domain + path;
		} else {
			return isProtocolAppend ? protocol + domain + ":" + port + path : domain + ":" + port + path;
		}
	}

	/**
	 * @title Local MAC Address
	 * @author soohyun
	 * @return Local MAC Address
	 * @date 2022.02.16
	 * @see  <pre></pre>
	 */
	public static String getMacAddress() {
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress() && !inetAddress.isLinkLocalAddress()
						&& inetAddress.isSiteLocalAddress()) {

						NetworkInterface networkInterface = NetworkInterface.getByInetAddress(inetAddress);

						if (networkInterface.getHardwareAddress() != null
							&& networkInterface.getHardwareAddress().length > 0) {
							StringBuffer sb = new StringBuffer();
							for (int i = 0; i < networkInterface.getHardwareAddress().length; i++) {
								sb.append(String.format("%02X", networkInterface.getHardwareAddress()[i]));
							}

							return sb.toString();
						}
					}
				}
			}
		} catch (SocketException e) {
			log.error("(!) failed getMacAddress - {}", e.getMessage());
		}
		return null;
	}
}
