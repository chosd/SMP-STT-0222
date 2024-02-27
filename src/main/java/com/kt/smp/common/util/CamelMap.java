package com.kt.smp.common.util;

import org.springframework.jdbc.support.JdbcUtils;

import java.util.HashMap;

/**
 * @title  Camelcase Map
 * @author Brian
 * @since  2022. 01. 29.
 */
@SuppressWarnings("serial")
public class CamelMap extends HashMap<String, Object> {
	
	@Override
    public Object put(String key, Object value) {
        return super.put(JdbcUtils.convertUnderscoreNameToPropertyName(key), value);
    }
}
