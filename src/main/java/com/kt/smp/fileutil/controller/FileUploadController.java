package com.kt.smp.fileutil.controller;

import com.kt.smp.fileutil.dto.UploadFileResponseDto;
import com.kt.smp.fileutil.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author jaime
 * @title SampleFileUploadController
 * @see\n <pre>
 * </pre>
 * @since 2022-03-11
 */
@RestController
@RequiredArgsConstructor
@Slf4j
public class FileUploadController {

    private final FileStorageService fileStorageService;

    @PostMapping(value = "/file/upload")
    public ResponseEntity<UploadFileResponseDto> uploadMultipartFile(@RequestParam(value = "file") MultipartFile file
            , @RequestParam(value = "filePath") String filePath) {
        return ResponseEntity.ok()
                .body(fileStorageService.storeMultipartFile(file, filePath));
    }

    @PostMapping(value = "/file/test/wav/upload")
    public ResponseEntity<UploadFileResponseDto> uploadTestWavFile(@RequestParam(value = "file") MultipartFile file
            , @RequestParam(value = "filePath") String filePath
            , @RequestParam(value = "serviceCode") String serviceCode) {
        return ResponseEntity.ok()
                .body(fileStorageService.storeTestWavFile(file, filePath, serviceCode));
    }

    @PostMapping(value = "/file/model/upload")
    public ResponseEntity<UploadFileResponseDto> uploadModelFile(@RequestParam(value = "file") MultipartFile file
            , @RequestParam(value = "filePath") String filePath
            , @RequestParam(value = "modelFileName") String modelFileName) {
        return ResponseEntity.ok()
                .body(fileStorageService.storeModelFile(file, filePath, modelFileName));
    }

}
