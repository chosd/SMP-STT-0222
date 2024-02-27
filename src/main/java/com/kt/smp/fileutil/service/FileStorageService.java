package com.kt.smp.fileutil.service;

import com.kt.smp.fileutil.dto.UploadFileResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioSystem;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;

/**
 * The type File storage service.
 *
 * @author jaime
 * @title FileStorageService
 * @see\n <pre> </pre>
 * @since 2022 -03-14
 */
@Service
@Slf4j
public class FileStorageService {

    /**
     * Store file upload file response dto.
     *
     * @param file     the file
     * @param filePath the file path
     * @return the upload file response dto
     */
    public UploadFileResponseDto storeMultipartFile(MultipartFile file, String filePath) {
        return uploadFile(file, filePath);
    }

    /**
     * Store test wav file upload file response dto.
     *
     * @param file     the file
     * @param filePath the file path
     * @return the upload file response dto
     */
    public UploadFileResponseDto storeTestWavFile(MultipartFile file
            , String  filePath
            , String serviceCode) {
        UploadFileResponseDto responseDto = uploadFile(file, filePath);

        if (!isWavFileCorrect(responseDto, serviceCode)) {
            return UploadFileResponseDto.builder()
                    .uploadSuccess(false)
                    .build();
        }

        return responseDto;
    }

    /**
     * Store model file upload file response dto.
     *
     * @param file          the file
     * @param filePath      the file path
     * @param modelFileName the model file name
     * @return the upload file response dto
     */
    public UploadFileResponseDto storeModelFile(MultipartFile file
            , String filePath
            , String modelFileName) {
        /**
         * 1. 우선, zip 파일을 저장하고
         * 2. zip 파일에서 파라미터로 전달받은 모델 파일명을 통해 모델 파일 추출 후 저장
         * 3. 저장한 zip 파일 삭제
         * 4. tar.gz 파일 read 권한 부여
         */
        UploadFileResponseDto uploadFileResponseDto = uploadFile(file, filePath);
        Path zipFilePath = Paths.get(uploadFileResponseDto.getFilePath());

        try (FileSystem fileSystem = FileSystems.newFileSystem(zipFilePath, ClassLoader.getSystemClassLoader())) {
            Path fileToExtract = fileSystem.getPath(modelFileName);
            Files.copy(fileToExtract, Paths.get(filePath + modelFileName), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            log.error("[FileStorageService.storeModelFile] ERROR {}", e.getMessage());

            return UploadFileResponseDto.builder()
                    .uploadSuccess(false)
                    .build();
        }

        File zipFile = new File(String.valueOf(zipFilePath));

        if (zipFile.exists()) {
            deleteFile(zipFile);
        }

        File modelFile = new File(filePath + modelFileName);
        modelFile.setReadable(true, false);

        return UploadFileResponseDto.builder()
                .uploadSuccess(true)
                .fileName(modelFileName)
                .filePath(filePath + modelFileName)
                .build();
    }

    private UploadFileResponseDto uploadFile(MultipartFile file, String filePath) {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            File newFile = new File(filePath + fileName);
            File directory = new File(newFile.getParentFile().getAbsolutePath());
            directory.mkdirs();
            file.transferTo(newFile);
            newFile.setReadable(true, false);
            directory.setReadable(true, false);

            return UploadFileResponseDto.builder()
                    .uploadSuccess(true)
                    .fileName(fileName)
                    .filePath(filePath + fileName)
                    .fileType(file.getContentType())
                    .size(file.getSize())
                    .build();
        } catch (Exception e) {
            log.error("[FileStorageService.storeFile] ERROR {}", e.getMessage());

            return UploadFileResponseDto.builder()
                    .uploadSuccess(false)
                    .build();
        }
    }

    private boolean isWavFileCorrect(UploadFileResponseDto responseDto, String serviceCode) {
        File wavFile = new File(responseDto.getFilePath());

        try {
            AudioFileFormat fileFormat = AudioSystem.getAudioFileFormat(wavFile);

            if (ObjectUtils.isEmpty(fileFormat)) {
                log.error("[FileStorageService.isWavFileCorrect] can't access to AudioFileFormat");

                return false;
            }

            int sampleSizeInBits = fileFormat.getFormat().getSampleSizeInBits();
            float sampleRate = fileFormat.getFormat().getSampleRate();

            log.info("[FileStorageService.isWavFileCorrect] sampleSizeInBits: {}, sampleRate: {}, fileFormat: {}"
                    , sampleSizeInBits, sampleRate, fileFormat.getFormat().toString());

            /**
             * no-codec
             */
            return (sampleSizeInBits == 8 || sampleSizeInBits == 16)
                    && ((int) Math.floor(sampleRate) == 8000 || (int) Math.floor(sampleRate) == 16000)
                    && fileFormat.getFormat().toString().contains("mono");
        } catch (Exception e) {
            log.error("[FileStorageService.isWavFileCorrect] error message: {}", e.getMessage());

            if (wavFile.exists()) {
                deleteFile(wavFile);
            }

            return false;
        }
    }

    private boolean checkAudioFormat(AudioFileFormat fileFormat
            , int sampleSizeInBits
            , float sampleRate
            , int acceptableSampleRate) {
        return (sampleSizeInBits == 16)
                && ((int) Math.floor(sampleRate) == acceptableSampleRate)
                && fileFormat.getFormat().toString().contains("mono");
    }

    private void deleteFile(File file) {
    	synchronized (file) {
    		file.delete();
		}
    }
}
