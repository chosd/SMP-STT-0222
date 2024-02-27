/**
 * 
 */
package com.kt.smp.stt.remover;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.kt.smp.stt.comm.preference.PreferenceValueHolder;
import com.kt.smp.stt.comm.preference.dto.RemoveStringListDto;
import com.kt.smp.stt.comm.preference.enums.FilesToRemove;
import com.kt.smp.stt.comm.preference.service.PreferenceService;
import com.kt.smp.stt.remover.dto.RemoveAmDataDirectoryDto;
import com.kt.smp.stt.remover.repository.FileRemoverRepository;
import com.kt.smp.stt.test.controller.SttTestApiController;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class FileRemover {

	private final PreferenceService preferenceService;
	private final FileRemoverRepository fileRemoverRepository;
	
	private static final String SINGLE_ACTION_TEST_PATH = "/singleActionTest/svc/";
	
	@Value("${callinfo.wav-download-path}")
	private String wavDownloadPath;
	
    @Value("${directory.home}")
    private String directoryHome;
	
    @Transactional
	public void run(String projectCode) {
		int removerStandard = PreferenceValueHolder.removerStandard.get(projectCode);
		log.info(">>> Remover standard : {} days ago", removerStandard);
		log.info(">>> Start to remove of datas. projectCode is " + projectCode);
		removeDatas(projectCode, removerStandard);
		log.info(">>> End to remove of datas. projectCode is " + projectCode);
		
		log.info(">>> Start to remove of files. projectCode is " + projectCode);
		removeFiles(projectCode, removerStandard);
		log.info(">>> End to remove of files. projectCode is " + projectCode);

	}

	private void removeDatas(String projectCode, int removerStandard) {
		// 삭제할 데이터 목록 조회
		RemoveStringListDto removeStringListDto = preferenceService.getRemoveStringList();
		
		// DB 데이터 삭제
		fileRemoverRepository.removeFileRelatedData(removerStandard, removeStringListDto.getDatasToRemoveList());
	}
    
	private void removeFiles(String projectCode, int removerStandard) {
		List<String> removeFileList = PreferenceValueHolder.filesToRemove.get(projectCode);
		
		List<String> directoryList = getRemoveFilePathList(removeFileList, removerStandard);
		
		for (String directory : directoryList) {
			if (directory == null) {
				continue;
			}
			try {
				removeCircuitPath(Path.of(directory), removerStandard);
			} catch (IOException e) {
				log.info("Fail to remove filese. exception: " + e);
			}
		}
	}

	private List<String> getRemoveFilePathList(List<String> removeFileList, int removerStandard) {
		List<String> directoryList = new ArrayList<>();
		
		if(removeFileList.contains(FilesToRemove.AM_TRAIN_DATA.getDescription())) {
			List<RemoveAmDataDirectoryDto> removeAmDataDirectoyList = fileRemoverRepository.findRemoveAmDataDirectoryList(removerStandard);
			for (RemoveAmDataDirectoryDto dto : removeAmDataDirectoyList) {
				directoryList.add(dto.getAnsFilePath());
				directoryList.add(dto.getVoiceFilePath());
			}
		}
		if(removeFileList.contains(FilesToRemove.CALL_INFO.getDescription())) {
			List<String> removeCallInfoDirectoryList = fileRemoverRepository.findRemoveCallInfoDirectoryList(removerStandard);
			directoryList.addAll(removeCallInfoDirectoryList);
		}
		if(removeFileList.contains(FilesToRemove.DEPLOY_MODEL.getDescription())) {
			List<String> removeDeployModelDirectoryList = fileRemoverRepository.findRemoveDeployModelDirectoryList(removerStandard);
			directoryList.addAll(removeDeployModelDirectoryList);
		}
		if(removeFileList.contains(FilesToRemove.TEST.getDescription())) {
			String testDirectory = directoryHome + SINGLE_ACTION_TEST_PATH;
			directoryList.add(testDirectory);
		}
		if(removeFileList.contains(FilesToRemove.VERIFY_DATA.getDescription())) {
			List<String> removeVerifyDataDirectoryList = fileRemoverRepository.findRemoveVerifyDataDirectoryList(removerStandard);
			directoryList.addAll(removeVerifyDataDirectoryList);
		}
		
		return directoryList;
	}

	private void removeCircuitPath(Path path, int removerStandard) throws IOException {
		
		Files.walkFileTree(path, new FileVisitor<Path> (){

			@Override
			public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
				// TODO 디렉토리 방문 전
				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
				// TODO 파일 방문
				String absolutePath = file.toAbsolutePath().toString();
				if (absolutePath.contains("singleActionTest") || absolutePath.contains("verifyData")) {
					FileTime lastModifiedTime = attrs.lastModifiedTime();
					
					LocalDate cycleDate = LocalDate.now().minusDays(removerStandard-1);
					
					LocalDate fileLocalDate = lastModifiedTime.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
					
					if (fileLocalDate.isBefore(cycleDate)) {
						Files.delete(file);
						log.info(file+"가 삭제되었습니다.");
					}
				} else {
					Files.delete(file);
					log.info(file+"가 삭제되었습니다.");
				}

				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
				// TODO 디렉토리 방문 후
				return FileVisitResult.CONTINUE;
			}

			@Override
			public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
				// TODO 파일 방문 실패
				return FileVisitResult.CONTINUE;
			}
			
		});
	}

}
