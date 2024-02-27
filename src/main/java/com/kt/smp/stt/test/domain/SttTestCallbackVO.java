package com.kt.smp.stt.test.domain;

import com.google.gson.Gson;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author jaime
 * @title SttTestCallbackRequestDto
 * @see\n <pre>
 * </pre>
 * @since 2022-03-22
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SttTestCallbackVO {

    private String uuid;

    private String resultCode;

    private String resultMsg;

    private String serviceCode;

    private List<SttTestResult> sttResult = new ArrayList<>();

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
