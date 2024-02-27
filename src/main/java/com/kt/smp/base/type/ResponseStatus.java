package com.kt.smp.base.type;

import lombok.Getter;

/**
 * 사용자의 rest api 요청 응답 상태 Type
 *
 * NOTE :: 기본적인 code 및 message는 HTTP STATUS CODE의 내용을 따름
 *
 * @author AICC 기술개발TF 지관욱
 * @since 2021.10.14
 * @version 1.0
 */
@Getter
public enum ResponseStatus {
    /*응답 성공 관련*/
    SUCCESS(200, "Success"),                                    // 일반적인 성공인 경우
    CREATED(201, "Created"),                                    // 데이터 생성이 성공인 경우
    ACCEPTED(202, "Accepted"),                                  // 요청은 받았지만 아직 수행되지 않고 이후 처리되는 경우
    CANCELLED(230, "Cancelled"),                                // 취소 요청이 성공적으로 수행된 경우

    /*요청 오류 관련*/
    BAD_REQUEST(400, "Bad Request"),                            // 파라미터가 잘못 요청된 경우
    UNAUTHORIZED(401, "Unauthorized"),                          // 사용자의 인증이 필요한 경우
    FORBIDDEN(403, "Forbidden"),                                // 사용자가 로그인하였지만 해당 내용이 접근이 불가능한 경우
    NOT_FOUND(404, "Not Found"),                                // 사용자가 요청한 데이터가 없는 경우
    METHOD_NOT_ALLOWED(405, "Method Not Allowed"),              // GET만 허용되는데 POST를 호출하는 것과 같은 경우
    NOT_ACCEPTABLE(406, "Not Acceptable"),                      // 요청한 기능에 허용되는 요청이나 요건 사항이 잘못되었을때
    REQUEST_TIMEOUT(408, "Request Timeout"),                    // 요청한 기능에 대한 처리 시간이 정해진 시간보다 길어 연결을 종료할때
    CONFLICT(409, "Conflict"),                                  // 요청한 기능이 현재 데이터와 충돌이 발생하여 처리할 수 없을 때
    GONE(410, "Gone"),                                          // 요청한 기능이 이전에는 있었으나 삭제되어 없어졌을때
    PAYLOAD_TOO_LARGE(413, "Payload Too Large"),                // 요청한 내용이 너무 큰 경우
    UNSUPPORTED_MEDIA_TYPE(415, "Unsupported Media Type"),      // 요청한 데이터의 타입이 지원되지 않는 타입인 경우
    LOCKED(423, "Locked"),                                      // 요청한 데이터가 잠겨 있는 경우
    FAILED_DEPENDENCY(424, "Failed Dependency"),                // 이전에 요청의 실패로 인해서 요청이 실패하는 경우
    TOO_MANY_REQUESTS(429, "Too Many Requests"),                // 짧은 시간 내에 너무 많은 요청을 하는 경우 또는 API 호출 제한 한도를 초과한 경우
    ALREADY_EXISTS(432, "Already Exists"),                      // 클라이언트가 생성하려고 시도한 엔티티가 이미 존재하는 경우
    ABORTED(433, "Aborted"),                                    // 요청한 작업이 유효하지 않아서 취소 처리된 경우
    OUT_OF_RANGE(434, "Out Of Range"),                          // 유효한 범위를 넘어서 작업을 시도한 경우

    /*서버 오류 관련*/
    INTERNAL_SERVER_ERROR(500, "Internal Server Error"),        // 요청이 처리 방법을 모르는 오류가 발생했을 때
    NOT_IMPLEMENTED(501, "Not Implemented"),                    // 구현해야 하는데 아직 구현되지 않음 또는 기능이 활성화 되어 있지 않은 경우
    BAD_GATEWAY(502, "Bad Gateway"),                            // 내부에서 서버가 다른 서버로 요청하였으나 잘못된 서버인 경우
    SERVICE_UNAVAILABLE(503, "Service Unavailable"),            // 요청된 기능이 사용할 수 없는 경우
    GATEWAY_TIMEOUT(504, "Gateway Timeout"),                    // 내부에서 서버가 다른 서버로 요청하였으나 Timeout이 발생한 경우
    ;

    private int code;
    private String message;
    ResponseStatus(int code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * 코드값을 기준으로 Status Type을 반환
     * @param code
     * @return
     */
    static public ResponseStatus codeOf(int code) {
        for (ResponseStatus status : ResponseStatus.values()) {
            if (code == status.getCode()) {
                return status;
            }
        }
        return null;
    }

    /**
     * 입력된 코드값에 해당하는 메시지를 반환
     * @param code
     * @return
     */
    static public String getStatusMessage(int code) {
        for (ResponseStatus status : ResponseStatus.values()) {
            if (code == status.getCode()) {
                return status.getMessage();
            }
        }
        return null;
    }
}