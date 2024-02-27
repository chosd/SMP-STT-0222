package com.kt.smp.fileutil.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author jaime
 * @title UploadFileRequestDto
 * @see\n <pre>
 * </pre>
 * @since 2022-03-14
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UploadFileRequestDto {

    private MultipartFile file;
}
