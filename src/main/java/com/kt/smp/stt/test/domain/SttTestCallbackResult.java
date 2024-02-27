package com.kt.smp.stt.test.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.Gson;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jaime
 * @title SttTestCallbackResult
 * @see\n <pre>
 * </pre>
 * @since 2022-05-05
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SttTestCallbackResult {

    @JsonProperty("결과 메시지")
    private String resultMsg;

    @JsonProperty("서비스 모델")
    private String serviceCode;

    @JsonProperty("STT 결과")
    private List<SttTestResult> sttResult = new ArrayList<>();

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
