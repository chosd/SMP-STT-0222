package com.kt.smp.stt.test.domain;

import com.google.gson.Gson;
import lombok.*;

/**
 * @author jaime
 * @title SttResult
 * @see\n <pre>
 * </pre>
 * @since 2022-03-22
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SttTestResult {

    private String transcript;

    private String confidence;

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
