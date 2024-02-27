package com.kt.smp.base.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.kt.smp.base.type.ResponseStatus;
import lombok.Getter;

/**
 * 사용자의 rest api 요청 응답 dto
 *
 * @param <OUTPUT>
 *
 * @author AICC 기술개발TF 지관욱
 * @since 2021.10.14
 * @version 1.0
 */
@Getter
public class HttpResponse<OUTPUT> {
    private int code;		    // 요청 결과 코드
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String	message;    // 요청 결과 코드에 따른 메시지
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Integer	total;      // 요청 결과 개수 (결과가 리스트 형태인 경우)
    private OUTPUT	object;     // 요청 결과 내용
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Object	debugInfo;  // debug 내용

    /**
     * 생성자
     */
    public HttpResponse() {}

    /**
     * 생성자
     * @param code
     * @param message
     * @param total
     * @param object
     * @param debugInfo
     */
    public HttpResponse(final int code, final String message, final Integer total, final OUTPUT object, final Object debugInfo) {
        this.code=code;
        this.message=message;
        this.total=total;
        this.object=object;
        this.debugInfo=debugInfo;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////
    // success wrapper
    /**
     * 성공-데이터 제공
     * @param object
     * @param <OUTPUT>
     * @return
     */
    public static <OUTPUT> HttpResponse<OUTPUT> onSuccess(final OUTPUT object){
        return onSuccess(null, object, null);
    }

    /**
     * 성공-리스트 형태 데이터 제공
     * @param total
     * @param object
     * @param <OUTPUT>
     * @return
     */
    public static <OUTPUT> HttpResponse<OUTPUT> onSuccess(final Integer total, final OUTPUT object){
        return onSuccess(total, object, null);
    }

    /**
     * 성공-리스트 형태 데이터 및 debug 제공
     * @param total
     * @param object
     * @param debugInfo
     * @param <OUTPUT>
     * @return
     */
    public static <OUTPUT> HttpResponse<OUTPUT> onSuccess(final Integer total, final OUTPUT object, final Object debugInfo){
        return new HttpResponse<OUTPUT>(ResponseStatus.SUCCESS.getCode(), ResponseStatus.SUCCESS.getMessage(), total, object, debugInfo);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////
    // fail wrapper

    /**
     * 실패-일반적인 실패 제공
     * NOTE :: 결과 코드는 알수 없기 때문에 500으로 제공함..
     * @param message
     * @return
     */
    public static HttpResponse<?> onFailure(final String message){
        return onFailure(ResponseStatus.INTERNAL_SERVER_ERROR.getCode(), message,  null);
    }

    /**
     * 실패-일반적인 실패 
     * NOTE :: 정의된 코드 및 메시지 제공
     * @param code
     * @return
     */
    public static HttpResponse<?> onFailure(final int code){
        return onFailure(code, ResponseStatus.getStatusMessage(code),  null);
    }

    /**
     * 실패-일반적인 실패
     * NOTE :: 정의된 코드 및 메시지 제공
     * @param status
     * @return
     */
    public static HttpResponse<?> onFailure(final ResponseStatus status){
        return onFailure(status.getCode(), status.getMessage(),  null);
    }

    /**
     * 실패-임의의 정의된 코드 및 메시지 제공
     * @param code
     * @param message
     * @return
     */
    public static HttpResponse<?> onFailure(final int code, final String message){
        return onFailure(code, message,  null);
    }

    /**
     * 실패-Exception 발생 시 해당 내용을 제공
     * NOTE :: 상세내용은 제공하지 않음
     * @param e
     * @return
     */
    public static HttpResponse<?> onFailure(final Throwable e){
        return onFailure(ResponseStatus.INTERNAL_SERVER_ERROR.getCode(), e.getMessage(), null);
    }

    /**
     * 실패-Exception 발생 시 해당 내용 및 Debug 내용을 제공
     * NOTE :: 상세내용은 제공하지 않음
     * @param e
     * @param debugInfo
     * @return
     */
    public static HttpResponse<?> onFailure(final Throwable e, final Object debugInfo){
        return onFailure(ResponseStatus.INTERNAL_SERVER_ERROR.getCode(), e.getMessage(), debugInfo);
    }

    /**
     * 실패-코드 및 Exception 메시지 제공
     * NOTE :: 상세내용은 제공하지 않음
     * @param code
     * @param e
     * @return
     */
    public static HttpResponse<?> onFailure(final int code, final Throwable e){
        return onFailure(code, e.getMessage(), null);
    }

    /**
     * 실패-코드 및 Exception 메시지&debug 내용 제공
     * @param code
     * @param e
     * @param debugInfo
     * @return
     */
    public static HttpResponse<?> onFailure(final int code, final Throwable e, final Object debugInfo){
        return onFailure(code, e.getMessage(), debugInfo);
    }

    /**
     * 실패-코드 및 임의의 메시지&debug 내용 제공
     * @param code
     * @param message
     * @param debugInfo
     * @return
     */
    public static HttpResponse<?> onFailure(final int code, final String message, final Object debugInfo){
        return new HttpResponse<>(code, message,  null, null, debugInfo);
    }
}
