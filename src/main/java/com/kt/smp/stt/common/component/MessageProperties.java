package com.kt.smp.stt.common.component;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.NoSuchMessageException;
import org.springframework.stereotype.Component;

import java.util.Locale;

/**
 * @title messages.properties 관련 component
 * @since 2022.07.21
 * @author soohyun
 * @see <pre></pre>
 */
@Component
@RequiredArgsConstructor
public class MessageProperties {

	private final MessageSource messageSource;

	public String getMessage(String key) {

		return this.getMessage(key, new String[] {}, Locale.getDefault());
	}

	public String getMessage(String key, Locale locale) {

		return this.getMessage(key, new String[] {}, locale);
	}

	public String getMessage(String key, String[] args, Locale locale) {

		String msg = null;
		try {
			msg = messageSource.getMessage(key, args, locale);
		} catch (NoSuchMessageException e) {
			msg = "";
		}

		return msg;
	}

}
