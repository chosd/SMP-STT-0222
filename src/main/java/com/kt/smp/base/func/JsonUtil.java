package com.kt.smp.base.func;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.kt.smp.stt.verify.history.controller.VerifyHistoryApiController;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Map;
import java.util.TimeZone;

/**
 * Jackson을 통한 Object <-> String 유틸
 *
 * @author AICC 기술개발TF 지관욱
 * @since 2021.01.07
 * @version 1.0
 *
 * <pre>
 * << 개정 이력 >>
 * 수정일          수정자                     수정 내용
 * 2021.01.07     AICC 기술개발TF 지관욱       최초 작성
 *
 * </pre>
 */
@Slf4j
public class JsonUtil {
    static private final ObjectMapper mapper = new ObjectMapper();
    static {
        // default 시간 format
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        df.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
        mapper.setDateFormat(df);

        // fromJson 시 없는 Class에 존재하지 않는 key value는 무시
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        mapper.configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL, true);

        mapper.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
    }

    /**
     * 입력된 문자가 Json 형태인지 체크
     *
     * @param str
     * @return
     */
    public static boolean isJsonString(String str) {
        Boolean isJson = false;
        try {
            if (str != null && !str.trim().equals("")) {
               if (str.trim().startsWith("{")) {
                   isJson = fromJson(str, Map.class) != null;
               } else if (str.trim().startsWith("[")) {
                   isJson = fromJson(str, ArrayList.class) != null;
               }
            }
        } catch (Throwable e) { 
        	log.error(e.getMessage());
        }
        return isJson;
    }

    /**
     * 입력된 값을 Json String 형태로 변경
     *
     * @param value
     * @return
     * @throws JsonProcessingException - json 변환 오류
     */
    static public String toJson(Object value) throws JsonProcessingException {
        return toJson(value, false);
    }

    /**
     * 입력된 값을 Json String 형태로 변경
     *
     * @param value
     * @return
     * @throws JsonProcessingException - json 변환 오류
     */
    static public String toJson(Object value, Boolean featureEnabled) throws JsonProcessingException {
        if (value == null) {
            return "";
        }
        if (featureEnabled) {
            // 들여쓰기 제공 여부
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
        } else {
            mapper.disable(SerializationFeature.INDENT_OUTPUT);
        }

        return mapper.writeValueAsString(value);
    }

    /**
     * Json String을 입력받아 입력된 type의 데이터로 변환
     *
     * @param <T>
     * @param value
     * @param ref
     * @return
     * @throws IOException - 입력값에 대한 오류
     */
    static public <T> T fromJson(String value, TypeReference<T> ref) throws IOException {
        if (value == null) {
            return null;
        }
        return mapper.readValue(value, ref);
    }

    /**
     * Json String을 입력 받아 Map 형태로 변환
     *
     * @param value
     * @return
     * @throws IOException - 입력값에 대한 오류
     */
    static public <T> T fromJson(String value) throws IOException {
        Object obj = null;
        if (value == null) {
            return (T) obj;
        }
        if (value != null ) {
            String target = value.trim();
            if (!target.equals("") && target.startsWith("{")) {
                obj = fromJson(value, Map.class);
            } else if (!target.equals("") && target.startsWith("[")) {
                obj = fromJson(value, ArrayList.class);
            }
        }
        return (T) obj;
    }

    /**
     * Json String을 입력받아 특정 class 형태로 변환
     * NOTE :: 입력된 class는 기본 생성자가 필요하며, static 또는 바로 접근 가능해야 변환이 된다.
     * @param <T>
     * @param value
     * @param clazz
     * @return
     * @throws IOException - 입력값에 대한 오류
     */
    static public <T> T fromJson(String value, Class<T> clazz) throws IOException {
        if (value == null) {
            return null;
        }

        return clazz.cast(mapper.readValue(value, clazz));
    }

    /**
     * Json String을 입력 받아 지정된 Class들을 기준으로 변환함.
     *
     * @param <T>
     * @param value
     * @param parametrized
     * @param parameterClasses
     * @return
     * @throws IOException - 입력값에 대한 오류
     */
    static public <T> T fromJson(String value, Class<?> parametrized, Class<?>... parameterClasses) throws IOException {
        if (value == null) {
            return null;
        }

        return mapper.readValue(value, getTypeFactory().constructParametricType(parametrized, parameterClasses));
    }

    /**
     * Json String을 입력 받아 java Type을 기준으로 변환함.
     *
     * @param <T>
     * @param value
     * @param type
     * @return
     * @throws IOException - 입력값에 대한 오류
     */
    static public <T> T fromJson(String value, JavaType type) throws IOException {
        if (value == null) {
            return null;
        }
        return mapper.readValue(value, type);
    }

    /**
     * 입력된 내용을 선택택된 특정 type으로 변환한다.
     * @param value
     * @param type
     * @param <T>
     * @return
     */
    static public <T> T convertValue(Object value, JavaType type) {
        if (value == null) {
            return null;
        }
        return mapper.convertValue(value, type);
    }

    /**
     * 입력된 내용을 선택택된 특정 class로 변환한다.
     * @param value
     * @param clazz
     * @param <T>
     * @return
     */
    static public <T> T convertValue(Object value, Class<T> clazz) {
        if (value == null) {
            return null;
        }

        return mapper.convertValue(value, clazz);
    }

    /**
     * 입력된 내용을 선택택된 특정 TypeReference에 동일한 타입로 변환한다.
     * @param value
     * @param ref
     * @param <T>
     * @return
     */
    static public <T> T convertValue(Object value, TypeReference<T> ref) {
        if (value == null) {
            return null;
        }

        return mapper.convertValue(value, ref);
    }

    /**
     * Java Type을 생성하기 위한 Factory 반한
     *
     * @return
     */
    static public TypeFactory getTypeFactory() {
        return mapper.getTypeFactory();
    }
}
