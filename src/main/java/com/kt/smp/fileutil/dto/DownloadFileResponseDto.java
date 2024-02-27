package com.kt.smp.fileutil.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Arrays;

/**
 * @author jaime
 * @title DownloadFileResponseDto
 * @see\n <pre>
 * </pre>
 * @since 2022-03-29
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DownloadFileResponseDto {

    private boolean downloadSuccess;

    private byte[] fileBytes;

    private String extension;

    @Override
    public String toString() {
        return "DownloadFileResponseDto{" +
                "downloadSuccess=" + downloadSuccess +
                ", fileBytes=" + Arrays.toString(fileBytes) +
                ", extension='" + extension + '\'' +
                '}';
    }
}
