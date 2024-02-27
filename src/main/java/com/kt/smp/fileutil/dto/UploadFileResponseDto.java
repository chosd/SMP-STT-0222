package com.kt.smp.fileutil.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author jaime
 * @title UploadFileResponse
 * @see\n <pre>
 * </pre>
 * @since 2022-03-14
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Builder
public class UploadFileResponseDto {

    private boolean uploadSuccess;

    private String fileName;

    private String filePath;

    private String fileType;

    private Long size;
    
    private List<String> trainE2ESLDataPathList;
    
    private List<String> trainE2ESLWavPathList;
    
    private List<String> trainE2EUSLWavPathList;

    @Override
    public String toString() {
        return "UploadFileResponseDto{" +
                "uploadSuccess=" + uploadSuccess +
                ", fileName='" + fileName + '\'' +
                ", filePath='" + filePath + '\'' +
                ", fileType='" + fileType + '\'' +
                ", size=" + size +
                '}';
    }
}
