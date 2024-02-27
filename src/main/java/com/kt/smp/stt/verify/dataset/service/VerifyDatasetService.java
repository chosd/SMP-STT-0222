package com.kt.smp.stt.verify.dataset.service;

import com.kt.smp.stt.comm.directory.service.DirectoryService;
import com.kt.smp.stt.verify.data.dto.VerifyDataDto;
import com.kt.smp.stt.verify.data.repository.VerifyDataRepository;
import com.kt.smp.stt.verify.dataset.dto.*;
import com.kt.smp.stt.verify.dataset.repository.VerifyDatasetRepository;
import com.kt.smp.stt.verify.history.service.VerifyHistoryService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VerifyDatasetService {

    private final VerifyDatasetRepository datasetRepository;
    private final VerifyDataRepository dataRepository;
    private final DirectoryService directoryService;

    public int count(VerifyDatasetSearchCondition searchCondition) {
        return datasetRepository.count(searchCondition);
    }

    public List<VerifyDatasetListDto> search(VerifyDatasetSearchCondition searchCondition) {
        return datasetRepository.search(searchCondition);
    }

    public List<VerifyDatasetListDto> getAll() {
        return datasetRepository.findAll();
    }

    public VerifyDatasetDto get(int id) {
        return datasetRepository.findById(id);
    }

    public boolean isExistName(String name) {
        return 0 < datasetRepository.countByName(name);
    }

    public boolean isExistDirectory(Integer directory) {
        return 0 < datasetRepository.countByDirectory(directory);
    }
    @Transactional
    public void save(VerifyDatasetSaveDto newDataset) {

        if (isExistName(newDataset.getName())) {
            throw new IllegalArgumentException("이미 등록되어 있는 데이터셋 이름 입니다");
        }

        datasetRepository.save(newDataset);
    }

    @Transactional
    public void update(int id, VerifyDatasetUpdateDto modifiedDataset) {

        modifiedDataset.setId(id);
        datasetRepository.update(modifiedDataset);
    }

    @Transactional
    public void delete(VerifyDatasetDeleteDto target) {
        List<Integer> targetIdList = target.getTargetIdList();
        for (Integer id : targetIdList) {

            dataRepository.deleteByDatasetId(id);
            datasetRepository.delete(id);
        }
    }

    public Path getAnswerSheetAndWavFile(Integer id) {
    	Path zipFilePath = null;
        try {
            VerifyDatasetDto dataset = datasetRepository.findById(id);
            if (dataset == null) {
                log.info("알 수 없는 데이터셋입니다");
            } else {
            	List<VerifyDataDto> dataList = dataRepository.findByDatasetId(dataset.getId());
                Path answerSheetPath = getAnswerSheetPath(dataList, dataset);
                List<Path> wavFilePaths = getWavFilePath(dataList, dataset);
                zipFilePath = zipAnswerSheetAndWavFile(answerSheetPath, wavFilePaths, dataset);
                Files.deleteIfExists(answerSheetPath);
            }
        } catch (IOException ex) {
        	log.error("정답지 임시 파일 삭제에 실패했습니다");
        }
        return zipFilePath;
    }

    private Path zipAnswerSheetAndWavFile(Path answerSheetPath, List<Path> wavFilePaths, VerifyDatasetDto dataset) {
    	Path zipFilePath = null;
        try {
            zipFilePath = Paths.get(dataset.getDirectoryPath(), "tmp_" + LocalDateTime.now() + ".zip");
            List<Path> targetFilePaths = new ArrayList<>(wavFilePaths);
            targetFilePaths.add(answerSheetPath);

            try(ZipOutputStream zos = new ZipOutputStream(Files.newOutputStream(Paths.get(zipFilePath.toString())))) {

                for (Path src : targetFilePaths) {
                    try(FileInputStream fis = new FileInputStream(src.toFile())) {

                        ZipEntry zipEntry = new ZipEntry(src.getFileName().toString());
                        zos.putNextEntry(zipEntry);

                        byte[] buffer = new byte[1024];
                        int len = 0;
                        while((len = fis.read(buffer)) > 0) {
                            zos.write(buffer, 0, len);
                        }
                        zos.closeEntry();
                    }
                }
            }
        } catch (IOException ex) {
            log.error("정답지, 음원파일 압축파일 생성에 실패했습니다");
        }
        return zipFilePath;
    }

    private List<Path> getWavFilePath(List<VerifyDataDto> dataList, VerifyDatasetDto dataset) {

        String wavDir = dataset.getDirectoryPath() +  File.separator + "wav";

        return dataList.stream()
                .map(data -> Paths.get(wavDir, data.getWavFileName()))
                .collect(Collectors.toList());
    }

    private Path getAnswerSheetPath(List<VerifyDataDto> dataList, VerifyDatasetDto dataset) {
    	Path path = null;
        try {
            String content = getAnswerSheetContent(dataList);
            String filename = "answerSheet_" + LocalDateTime.now() + ".txt";
            path = Paths.get(dataset.getDirectoryPath(),filename);
            Files.write(path, content.getBytes(StandardCharsets.UTF_8));

        } catch (IOException ex) {
            log.error("정답지 파일 생성에 실패했습니다");
        }
        return path;
    }

    private String getAnswerSheetContent(List<VerifyDataDto> dataList) {

        StringBuilder content = new StringBuilder();
        for (VerifyDataDto data : dataList) {
            content.append(data.getWavFileName());
            content.append("\t");
            content.append(data.getDictatedText());
            content.append("\n");
        }

        return content.toString();
    }
}
