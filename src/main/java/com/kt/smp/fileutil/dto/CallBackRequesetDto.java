package com.kt.smp.fileutil.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author jaime
 * @title CallBackRequesetDto
 * @see\n <pre>
 * </pre>
 * @since 2022-03-08
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CallBackRequesetDto {

    private String filePath;

}
