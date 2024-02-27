package com.kt.smp.common.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kt.smp.stt.common.dto.BaseResultDto;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @title Jackson util
 * @since 2022.02.17
 * @author soohyun
 * @see  <pre><pre>
 */
@Slf4j
public class JacksonUtil {

	private static final ObjectMapper JACKSON;
	private static final ObjectMapper JACKSON_EXCLUDE_ANNOTATION;

	/**
	 * Constructor
	 */
	private JacksonUtil() {
	}

	static {
		JACKSON = new ObjectMapper();
		JACKSON_EXCLUDE_ANNOTATION = new ObjectMapper().disable(MapperFeature.USE_ANNOTATIONS);
	}

	/**
	 * @title Object to JSON String
	 * @author soohyun
	 * @param object object
	 * @return string
	 * @date 2022.02.17
	 * @see <pre>
	 *     Object -> JSON
	 *     {"id":"kim", "age":28}
	 * </pre>
	 */
	public static String objectToJson(Object object) {
		String jsonString = null;
		try {
			jsonString = JACKSON.writeValueAsString(object);
		} catch (JsonProcessingException e) {
			log.error("JsonProcessingException in JacksonUtil : {}", e.toString());
		}

		return jsonString;
	}

	/**
	 * @title Object to JSON String with Pretty Print
	 * @author soohyun
	 * @param object object
	 * @return string
	 * @date 2022.02.17
	 * @see  <pre>
	 *     Object -> JSON
	 *     {
	 * 	    "id":"kim",
	 * 	    "age":28
	 *      }
	 * </pre>
	 */
	public static String objectToJsonWithPrettyPrint(Object object) {
		if (object == null) {
			return null;
		}

		String jsonString = null;
		try {
			jsonString = JACKSON.writerWithDefaultPrettyPrinter().writeValueAsString(object);
		} catch (JsonProcessingException e) {
			log.error("JsonProcessingException in JacksonUtil : {}", e.toString());
		}

		return jsonString;
	}

	/**
	 * @title Object to JSON String with Exclude Annotation in POJO
	 * @author soohyun
	 * @param object object
	 * @return string
	 * @date 2022.02.17
	 * @see  <pre>
	 *     Object -> JSON
	 *     {"id":"kim", "age":28}
	 * </pre>
	 */
	public static String objectToJsonExcludeAnnotation(Object object) {
		if (object == null) {
			return null;
		}

		String jsonString = null;
		try {
			jsonString = JACKSON_EXCLUDE_ANNOTATION.disable(MapperFeature.USE_ANNOTATIONS).writeValueAsString(object);
		} catch (JsonProcessingException e) {
			log.error("JsonProcessingException in JacksonUtil : {}", e.toString());
		}

		return jsonString;
	}

	/**
	 * @title JSON String to Object
	 * @author soohyun
	 * @param <T>  the type parameter
	 * @param jsonString json string
	 * @param clazz : String.class -> return Json String
	 * @param clazz : UserVO.class -> return UserVO Binding
	 * @return t
	 * @date 2022.02.
	 * @see  <pre></pre>
	 */
	public static <T> T jsonToObject(String jsonString, Class<T> clazz) {
		if (jsonString == null || "".equals(jsonString)) {
			return null;
		}

		T object = null;
		try {
			object = JACKSON.readValue(jsonString, clazz);
		} catch (IOException e) {
			log.error("JsonProcessingException in JacksonUtil : {}", e.toString());
		}

		return object;
	}

	/**
	 * @title JSON Bytes to Object
	 * @author soohyun
	 * @param <T>  the type parameter
	 * @param jsonBytes json bytes
	 * @param clazz : String.class -> return Json String
	 * @param clazz : UserVO.class -> return UserVO Binding
	 * @return t
	 * @date 2022.02.
	 * @see  <pre></pre>
	 */
	public static <T> T jsonToObject(byte[] jsonBytes, Class<T> clazz) {
		if (jsonBytes == null || jsonBytes.length == 0) {
			return null;
		}

		T object = null;
		try {
			object = JACKSON.readValue(jsonBytes, clazz);
		} catch (IOException e) {
			log.error("JsonProcessingException in JacksonUtil : {}", e.toString());
		}

		return object;
	}

	/**
	 * @title JSON String to TypeReference
	 * @author soohyun
	 * @param <T>  the type parameter
	 * @param jsonString json string
	 * @param typeRef : new TypeReference&ltHashMap&ltString, Object&gt&gt(){} -> return Map&ltString, Object&gt Type
	 * @param typeRef : new TypeReference&ltArrayList&ltString&gt&gt(){} -> return List&ltString&gt Type
	 * @return t
	 * @date 2022.02.17
	 * @see  <pre></pre>
	 */
	public static <T> T jsonToTypeReference(String jsonString, TypeReference<T> typeRef) {
		if (jsonString == null || "".equals(jsonString)) {
			return null;
		}

		T object = null;
		try {
			object = JACKSON.readValue(jsonString, typeRef);
		} catch (IOException e) {
			log.error("JsonProcessingException in JacksonUtil : {}", e.toString());
		}

		return object;
	}

	/**
	 * @title JSON String to Map<String, Object>
	 * @author soohyun
	 * @param jsonString json string
	 * @return map
	 * @date 2022.02.17
	 * @see  <pre></pre>
	 */
	public static Map<String, Object> jsonToMap(String jsonString) {
		if (jsonString == null || "".equals(jsonString)) {
			return null;
		}

		Map<String, Object> object = null;
		try {
			object = JACKSON.readValue(jsonString, new TypeReference<HashMap<String, Object>>() {
			});
		} catch (IOException e) {
			log.error("JsonProcessingException in JacksonUtil : {}", e.toString());
		}

		return object;
	}

	/**
	 * @title JSON String to List<String>
	 * @author soohyun
	 * @param jsonString json string
	 * @return list
	 * @date 2022.02.17
	 * @see  <pre></pre>
	 */
	public static List<String> jsonToList(String jsonString) {
		if (jsonString == null || "".equals(jsonString)) {
			return null;
		}

		List<String> object = null;
		try {
			object = JACKSON.readValue(jsonString, new TypeReference<ArrayList<String>>() {
			});
		} catch (IOException e) {
			log.error("JsonProcessingException in JacksonUtil : {}", e.toString());
		}

		return object;
	}
}
