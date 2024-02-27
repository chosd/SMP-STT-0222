package com.kt.smp.stt.verify.data.service;

import com.kt.smp.common.util.crypto.TextCrypto;
import com.kt.smp.multitenancy.config.TenantContextHolder;
import com.kt.smp.multitenancy.dto.ConfigDto;
import com.kt.smp.multitenancy.service.ConfigService;
import com.kt.smp.stt.comm.directory.service.DirectoryService;
import com.kt.smp.stt.comm.preference.PreferenceValueHolder;
import com.kt.smp.stt.comm.preference.service.PreferenceService;
import com.kt.smp.stt.verify.data.dto.*;
import com.kt.smp.stt.verify.data.repository.VerifyDataRepository;
import com.kt.smp.stt.verify.dataset.dto.*;
import com.kt.smp.stt.verify.dataset.repository.VerifyDatasetRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItem;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.sound.midi.MidiFileFormat;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;


/**
 * @FileName : VerifyDataService.java
 * @Project : stt-smp
 * @Date : 2024. 1. 17.
 * @작성자 : munho.jang
 * @변경이력 :
 * @프로그램설명 :
 */

/**
 *@FileName : VerifyDataService.java
 *@Project : stt-smp
 *@Date : 2024. 1. 17.
 *@작성자 : munho.jang
 *@변경이력 : 
 *@프로그램설명 : 
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VerifyDataService {

    private final VerifyDataRepository dataRepository;
    private final VerifyDatasetRepository datasetRepository;
    private final DirectoryService directoryService;
    private final ConfigService configService;
    private final TextCrypto textCrypto;
    private final PreferenceService preferenceService;
    
    @Value("${spring.profiles.active}")
    private String profile;
    
    @Value("${directory.home}")
    private String directoryHome;

    public int count(VerifyDataSearchCondition searchCondition) {
    	return dataRepository.count(searchCondition);
    }

    public List<VerifyDataListDto> search(VerifyDataSearchCondition searchCondition) {
    	List<VerifyDataListDto> findList = dataRepository.search(searchCondition);
    	for (VerifyDataListDto dto : findList) {
    		dto.setDictatedText(textCrypto.decrypt(dto.getDictatedText()));
    	}

    	return dataRepository.search(searchCondition);
    }

	public VerifyDataDto get(int id) {
	  VerifyDataDto result = dataRepository.findById(id);
	  result.setDictatedText(textCrypto.decrypt(result.getDictatedText()));
	  return result;
	}

    public VerifyDataDto getByDatasetAndWavFile(VerifyDatasetDto verifyDataset, String wavFileName) {
    		
        return dataRepository.findByDatasetIdAndWavFileName(verifyDataset.getServiceModelId(), wavFileName);
    }
    
    public List<VerifyDataListDto> getDatasetGroup() {
        return dataRepository.findDatasetGroup();
        
    }

    // 현재 미사용...
	public List<VerifyDataDto> getByDataset(VerifyDatasetDto dataset) {
	   return dataRepository.findByDatasetId(dataset.getId());
	}
	
	public boolean isExistName(String name) {
	  return 0 < dataRepository.countByName(name);
	}
	
	public boolean isExistPath(String path, String code) {
	  return 0 < dataRepository.countByPath(path, code);
	}

  @Transactional
  public void save(VerifyDataMultipartSaveDto newData) throws IOException {

    if (hasWavFile(newData)) {
      saveWavFile(newData);
    }
    if (hasAnswerSheetFile(newData)) {
      saveAnswerSheetFile(newData);
    }
    saveVerifyData(newData);
  }

    @Transactional
    public void save(VerifyDataSaveDto newData) {
//        VerifyDataDto savedData = dataRepository.findByDatasetIdAndWavFileName(newData.getServiceModelId(), newData.getWavFileName());
//        if (savedData == null) {
    	
//    	String projectCode = TenantContextHolder.getProjectCode();    
//		if (PreferenceValueHolder.textEncrypt.get(projectCode)) {
//        	String encryptedText = textCrypto.encrypt(newData.getDictatedText());
//        	newData.setDictatedText(encryptedText);
//        }
        
		dataRepository.save(newData);
//            return;
//        }

//        VerifyDataUpdateDto modifiedData = new VerifyDataUpdateDto(
//                savedData.getId(),
//                newData.getDictatedText(),
//                newData.getUpdId(),
//                newData.getUpdIp());
//
//        dataRepository.update(modifiedData);

  }

  @Transactional
  public void delete(VerifyDataDeleteDto target) throws IOException {
    List<Integer> targetIdList = target.getTargetIdList();

    // targetIdList 에 여러 데이터셋이 섞여 있을 수 있음을 고려
    int datasetId = 0;
    String datasetName = "";

    for (int i = 0; i < targetIdList.size(); i++) {
      VerifyDataDto verifyData = dataRepository.findById(targetIdList.get(i));

      datasetName = verifyData.getDatasetName();
      datasetId = verifyData.getDatasetId();
      log.info(">>>>>> target datasetName :  " + datasetName);

          /*if( !Objects.nonNull(verifyData.getDatasetName()) ) {
        		if(i == 0) {
        			target.setDatasetId(verifyData.getDatasetId());
        			target.setDatasetName(verifyData.getDatasetName());
        		}
        	}*/

      // 실제 파일 삭제 처리
      Path wavPath = Paths.get(verifyData.getVerifyDataPath(), "wav", verifyData.getWavFileName());
      log.info("======== before file delete ======= | " + wavPath.toString());
      Files.deleteIfExists(wavPath); // 파일삭제\
      dataRepository.delete(targetIdList.get(i)); // 테이블데이터 삭제

      List<VerifyDataDto> resultData = remainDatasetData(datasetName, datasetId); // 남은 데이터 찾기

      if (resultData.size() > 0) {
        remakeAnswerFile(resultData); // 정답지 파일 재생성
      } else {
        // 데이터셋에 해당하는 검증 데이터가 남지 않았을 경우 폴더 삭제
        deleteDatasetDirectory(verifyData);
      }
    }
  }


  /**
   *@MethodName : remakeAnswerFile
   *@작성일 : 2024. 1. 17.
   *@작성자 : munho.jang
   *@변경이력 : 2024. 01. 23. chanmi.joo
   *@Method설명 : 검증데이터 삭제시 해당 데이터셋그룹에 대한 정답지파일 재생성하기
   * @param resultData
   * @throws IOException
   */
  private void remakeAnswerFile(List<VerifyDataDto> resultData) {
    log.info(">>>> selected verify data path : " + resultData.get(0).getVerifyDataPath());
    File newFile = createNewAnswerFile(resultData);
    MultipartFile newAnswerFile = convertFileToMultipartFile(newFile);
    //FileUploadUtil.uploadFile(newAnswerFile, resultData.get(0).getVerifyDataPath());
  }

  /**
   *@MethodName : deleteDatasetDirectory
   *@작성일 : 2024. 1. 23.
   *@작성자 : chanmi.joo
   *@변경이력 :
   *@Method설명 : 검증 데이터 삭제시 해당 데이터셋 그룹에 남은 검증 데이터가 없을 경우 해당 데이터셋 경로 삭제
   * @param data
   * @throws
   */

  private boolean deleteDatasetDirectory(VerifyDataDto data) {

    Path path = Paths.get(directoryHome, data.getBasePath(), data.getDetailPath());
    File datasetFolder = new File(path.toString());

    try {
      if(datasetFolder.exists()){
        // 해당 데이터셋 폴더 내에 다른 파일이 남아있을 경우
        // 함께 삭제 해야 하는 파일 및 폴더 : 하위 service model id, answer, 빈 wav 폴더
        File[] folderList = datasetFolder.listFiles();

        for (int i = 0; i < folderList.length; i++){
          if (folderList[i].isFile()){
            log.info(">>>> delete file " + path + folderList[i] );
            folderList[i].delete();
          } else {
            deleteFolder(folderList[i].getPath()); // deleteFolder 함수 호출
            log.info(">>>> delete folder " + folderList[i].getPath() );
          }
          folderList[i].delete();
          log.info(">>>> delete folder " + folderList[i].getPath() );
        }
        log.info(">>>> delete verify dataset path : " + path);
        datasetFolder.delete();
      }

    } catch (Exception e) {
      log.error(" dataset 경로 삭제에 오류가 발생했습니다 >>> " + e.getMessage());
      return false;
    }

    return true;
  }



  private MultipartFile convertFileToMultipartFile(File file) {
    FileItem fileItem = null;
    try {
      fileItem = new DiskFileItem("file", Files.probeContentType(file.toPath()), false, file.getName(), (int) file.length(), file.getParentFile());
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    try (InputStream is = new FileInputStream(file); OutputStream os = fileItem.getOutputStream();) {

      IOUtils.copy(is, os);

    } catch (IOException e) {
      e.printStackTrace();
      ;
    }

    MultipartFile multipartFile = new CommonsMultipartFile(fileItem);
    log.info("========== return multipartFile ==========");
    return multipartFile;
  }

  private File createNewAnswerFile(List<VerifyDataDto> resultData) {
    Path path = Paths.get(resultData.get(0).getVerifyDataPath(), resultData.get(0).getAnswerFileName());
    log.info(">>>>>> create file path : " + path);
    File file = path.toFile();
    try (FileOutputStream fos = new FileOutputStream(file);
         OutputStreamWriter osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
         BufferedWriter writer = new BufferedWriter(osw)) {

      for (int i = 0; i < resultData.size(); i++) {
        log.info(">>>>> before file input answer list value : [" + i + "]번째 " + resultData.get(i).getWavFileName() + " | " + resultData.get(i).getDictatedText());
        VerifyDataDto data = resultData.get(i);

        String fileName = data.getWavFileName();
        String dictatedData = data.getDictatedText();

        writer.append(fileName + "\t" + dictatedData);
        writer.newLine();

      }
      writer.flush();
    } catch (IOException e) {
      e.printStackTrace();
      //throw new DictationException("정답지 파일을 생성하는 중 에러가 발생하였습니다", e);
    }

    return file;
  }

  @Transactional
  public void delete(VerifyDataDto target) {
    dataRepository.delete(target.getId());
  }

  private boolean hasWavFile(VerifyDataMultipartSaveDto newData) {
    return newData.getWavFile() != null && !newData.getWavFile().isEmpty();
  }

  private boolean hasAnswerSheetFile(VerifyDataMultipartSaveDto newData) {
    return newData.getAnswerSheet() != null && !newData.getAnswerSheet().isEmpty();
  }

  // wav파일 저장
  private void saveWavFile(VerifyDataMultipartSaveDto newData) throws IOException {
    String directory = getSaveDirectory(newData);
//    	String directory = "C:\\2nas_home\\stt\\test-project-code\\verifyData\\callbot"; //로컬테스트를 위한 하드코딩
//    	directory += File.separator + newData.getDatasetId();

//        String directory = "C:\\2nas_home" + getSaveDirectory(newData); // 테스트용

    MultipartFile wavFile = newData.getWavFile();

    byte[] buffer = new byte[1024];

    try (ZipInputStream zipInputStream = new ZipInputStream(wavFile.getInputStream())) {
      ZipEntry zipEntry;
      // 여기서 업로드 하는 경로와 뒤에 로직에서 파일존재 유무 체크하는 경로가 달라서 맞춰줌
      String newDirectory = "";
      newDirectory = directory;
      log.info(">>>>>>>> before extractFolder new directory : " + newDirectory);

      File extractFolder = new File(newDirectory);
      if (!extractFolder.exists()) {
        extractFolder.mkdirs();
      }

      while ((zipEntry = zipInputStream.getNextEntry()) != null) {
        String entryName = zipEntry.getName();
        log.info(">>> entryName : " + entryName);
        String entryPath = "";
        Path wavPath = null;
        entryPath = newDirectory + File.separator + entryName; // wav 폴더에 저장
        wavPath = Paths.get(newDirectory, "wav");
        log.info(">>> entryPath : " + entryPath);
        log.info(">>> wavPath : " + wavPath);

        Files.createDirectories(wavPath);

        try (FileOutputStream fileOutputStream = new FileOutputStream(entryPath)) {
          int len;
          while ((len = zipInputStream.read(buffer)) > 0) {
            fileOutputStream.write(buffer, 0, len);
          }
        }

        zipInputStream.closeEntry();
      }
    }
  }

  private void saveAnswerSheetFile(VerifyDataMultipartSaveDto newData) {
    String directory = getSaveDirectory(newData);
//        String directory = "C:\\2nas_home" + getSaveDirectory(newData); // 테스트용
//    	String directory = "C:\\2nas_home\\stt\\test-project-code\\verifyData\\callbot"; //로컬테스트를 위한 하드코딩
//    	directory += File.separator + newData.getDatasetId();
    MultipartFile answerFile = newData.getAnswerSheet();

    String newDirectory = "";
    newDirectory = directory;

    Path filePath = Paths.get(newDirectory);
    File f = new File(filePath.toString(), answerFile.getOriginalFilename());
    log.info("Path >>> {}", f.getPath());
    try {
      log.info(">>>> before extractFolder directory : " + newDirectory);
      File extractFolder = new File(newDirectory);
      if (!extractFolder.exists()) {
        extractFolder.mkdirs();
      }
      answerFile.transferTo(f);

    } catch (Exception e) {
      log.error(e.getMessage());
    }

  }

  private List<VerifyDataDto> remainDatasetData(String datasetName, int datasetId) {
    return dataRepository.findByDatasetNameId(datasetName, datasetId);
  }

  private boolean isFile(String path) {
    return !StringUtils.contains(path, File.separator);
  }

  private String getSaveDirectory(VerifyDataMultipartSaveDto newData) {

//        VerifyDatasetDto dataset = datasetRepository.findById(newData.getDatasetId());
//        return directoryService.getAbsolutePath(dataset.getDirectoryId());
    String basePath = "";
    if (newData.getBasePath().contains("verify")) {
      basePath = "verifyData";
    }

    Path realPath = Paths.get(directoryHome, basePath, newData.getDetailPath(), newData.getServiceModelId().toString());

    return realPath.toString();
  }

    private void saveVerifyData(VerifyDataMultipartSaveDto newData) throws IOException {
    	String projectCode = TenantContextHolder.getProjectCode();
        List<VerifyDataSaveDto> newVerifyDataList = makeNewVerifyData(newData);
        for (VerifyDataSaveDto verifyData : newVerifyDataList) {
        	
        	if (PreferenceValueHolder.textEncrypt.get(projectCode)) {
        		String encryptedText = textCrypto.encrypt(verifyData.getDictatedText());
        		verifyData.setDictatedText(encryptedText);
        	}
        	
            if (isNewWavFile(verifyData)) {
                dataRepository.save(verifyData);
            } else {
                updateDictatedText(verifyData);
            }
        }
    }

  private void updateDictatedText(VerifyDataSaveDto newVerifyData) {

    VerifyDataDto verifyData = dataRepository.findByDatasetIdAndWavFileName(newVerifyData.getServiceModelId(), newVerifyData.getWavFileName());

    VerifyDataUpdateDto modifiedData = new VerifyDataUpdateDto(
      verifyData.getId(),
      newVerifyData.getDictatedText(),
      newVerifyData.getUpdId(),
      newVerifyData.getUpdIp());

    dataRepository.update(modifiedData);

  }

  private boolean isNewWavFile(VerifyDataSaveDto verifyData) {
    return dataRepository.countByDatasetIdAndWavFileName(verifyData.getDatasetName(), verifyData.getWavFileName()) < 1;
  }

  private List<VerifyDataSaveDto> makeNewVerifyData(VerifyDataMultipartSaveDto newData) throws IOException {

    String saveDirectory = getSaveDirectory(newData);
//        String saveDirectory = "C:\\2nas_home" + getSaveDirectory(newData); // 테스트용

//    	String saveDirectory = "C:\\2nas_home\\stt\\test-project-code\\verifyData\\callbot";
//    	saveDirectory += File.separator + newData.getDatasetId() + File.separator + "wav" + File.separator;
//        saveDirectory += File.separator + "wav" + File.separator;
    List<AnswerSheetDto> answerSheetList = parseAnswerSheetFile(newData);
    validateAnswerSheet(saveDirectory, answerSheetList);
    Random random = new Random();
    newData.setDatasetId(random.nextInt(99999999)); // 검증데이터셋화면 제거후 혹시 모를 오류방지 위함
    newData.setVerifyDataPath(saveDirectory);
    return answerSheetList.stream().map(answerSheet -> new VerifyDataSaveDto(newData, answerSheet)).collect(Collectors.toList());
  }

  private List<AnswerSheetDto> parseAnswerSheetFile(VerifyDataMultipartSaveDto newData) throws IOException {
    List<AnswerSheetDto> answerSheetList = new ArrayList<>();

    String directory = getSaveDirectory(newData);
//            String directory = "C:\\2nas_home" + getSaveDirectory(newData); // 테스트용

//        	String directory = "C:\\2nas_home\\stt\\test-project-code\\verifyData\\callbot"; //로컬테스트를 위한 하드코딩
//        	directory += File.separator + newData.getDatasetId();
    String newDirectory = "";
    newDirectory = directory;

    File file = new File(Paths.get(newDirectory).toString(), newData.getAnswerSheet().getOriginalFilename());
    try (BufferedReader reader = new BufferedReader(new FileReader(file.getPath()))) {
//            	BufferedReader reader = new BufferedReader(new FileReader(file.getPath()));	
      String line;
      while ((line = reader.readLine()) != null) {
        String[] split = line.split("\t");

        if (split.length != 2) {
          throw new IllegalArgumentException("정답지 양식에 맞지 않는 파일입니다 양식: 음원파일명[tab]전사데이터");
        }
        answerSheetList.add(new AnswerSheetDto(split[0], split[1]));
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return answerSheetList;
  }

  private void validateAnswerSheet(String saveDirectory, List<AnswerSheetDto> answerSheetList) {

    for (AnswerSheetDto answerSheet : answerSheetList) {

      if (StringUtils.isBlank(answerSheet.getWavFileName())) {
        throw new IllegalArgumentException("입력되지 않은 음원파일명이 있습니다");
      }

      if (StringUtils.isBlank(answerSheet.getDictatedText())) {
        throw new IllegalArgumentException("입력되지 않은 전사데이터가 있습니다");
      }
      log.info(">>>>> before wavFile exists : " + saveDirectory + File.separator + "wav");
      log.info(">>>>> before wavFile exists answerSheet.getWavFileName : " + answerSheet.getWavFileName());
      String chkPath = "";
      chkPath = saveDirectory + File.separator + "wav";

      File wavFile = Paths.get(chkPath, answerSheet.getWavFileName()).toFile();

      if (!wavFile.exists()) {
        throw new IllegalArgumentException(answerSheet.getWavFileName() + "은 존재하지 않는 파일입니다");
      }
    }
  }

  public byte[] getWavFileBytes(Integer id) {
    byte[] resultByte = null;
    List<ConfigDto> configDtos = configService.getAllUserDefined();

    for (ConfigDto configDto : configDtos) {
      TenantContextHolder.set(configDto.getProjectCode());
    }

    VerifyDataDto data = dataRepository.findById(id);
    //VerifyDatasetDto dataset = datasetRepository.findById(data.getDatasetId());
    Path wavFilePath = null;
    wavFilePath = Paths.get(data.getVerifyDataPath(), "wav", data.getWavFileName());

    log.info(">>>>> after wavFilePath : " + wavFilePath);
    try {
      resultByte = Files.readAllBytes(wavFilePath);
    } catch (IOException ex) {
      log.error("음원파일을 읽을 수 없습니다(경로: " + wavFilePath + ")");
    }
    return resultByte;
  }

  // 특정 경로 내 폴더 및 파일 삭제용
  private void deleteFolder(String path){
    File folder = new File(path);

    try {
      if (folder.exists()){
        File[] folderList = folder.listFiles();

        for (int i = 0; i < folderList.length; i++){
          if (folderList[i].isFile()){
            folderList[i].delete();
            log.info(">>>> delete file " + path + folderList[i] );
          } else {
            deleteFolder(folderList[i].getPath()); // 재귀 함수 호출
            log.info(">>>> delete folder " + folderList[i].getPath() );
          }

          folderList[i].delete();
        }

        folder.delete();
        log.info(">>>> delete folder " + folder.getPath() );
      }
    } catch (Exception e){
      log.error(path + " 경로 삭제에 오류가 발생했습니다 >>> " + e.getMessage());
    }
  }

}


