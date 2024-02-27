package com.kt.smp.stt.test.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author jaime
 * @title SttTestRequestDto
 * @see\n <pre>
 * </pre>
 * @since 2022-03-22
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SttTestRequestDto {

    private String serviceCode;

    private String testWavPath;

    private String callbackUrl;
}
