package com.kt.smp.stt.dictation.service;

import static java.nio.file.StandardOpenOption.CREATE;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorOutputStream;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kt.smp.common.util.JacksonUtil;
import com.kt.smp.common.util.crypto.AudioCrypto;
import com.kt.smp.common.util.crypto.TextCrypto;
import com.kt.smp.multitenancy.config.TenantContextHolder;
import com.kt.smp.multitenancy.dto.ConfigDto;
import com.kt.smp.multitenancy.dto.MasterSmpRequestHeaderDto;
import com.kt.smp.multitenancy.service.ConfigService;
import com.kt.smp.stt.comm.directory.service.DirectoryService;
import com.kt.smp.stt.comm.preference.PreferenceValueHolder;
import com.kt.smp.stt.common.EngineUrlResolver;
import com.kt.smp.stt.common.ErrorTypeCode;
import com.kt.smp.stt.common.ResultCode;
import com.kt.smp.stt.common.dto.BaseResultDto;
import com.kt.smp.stt.common.dto.SttConfigDto;
import com.kt.smp.stt.confidence.dto.ConfidenceConfigInsertDTO;
import com.kt.smp.stt.dictation.dto.BulkUsageSaveDto;
import com.kt.smp.stt.dictation.dto.ConfidenceConfigDto;
import com.kt.smp.stt.dictation.dto.ConfidenceDto;
import com.kt.smp.stt.dictation.dto.ConfidenceGetResultDto;
import com.kt.smp.stt.dictation.dto.DictationDeleteDto;
import com.kt.smp.stt.dictation.dto.DictationDto;
import com.kt.smp.stt.dictation.dto.DictationListDto;
import com.kt.smp.stt.dictation.dto.DictationSaveDto;
import com.kt.smp.stt.dictation.dto.DictationSearchCondition;
import com.kt.smp.stt.dictation.dto.DictationToVerifyDataDto;
import com.kt.smp.stt.dictation.dto.DictationUpdateDto;
import com.kt.smp.stt.dictation.dto.ResponseDto;
import com.kt.smp.stt.dictation.dto.UsageDto;
import com.kt.smp.stt.dictation.dto.UsageSaveDto;
import com.kt.smp.stt.dictation.exception.DictationException;
import com.kt.smp.stt.dictation.repository.DictationRepository;
import com.kt.smp.stt.dictation.repository.UsageRepository;
import com.kt.smp.stt.dictation.type.UsageType;
import com.kt.smp.stt.error.service.SttErrorService;
import com.kt.smp.stt.log.dto.LogDto;
import com.kt.smp.stt.log.repository.LogRepository;
import com.kt.smp.stt.train.trainData.dto.SttTrainDataMultipartSaveDto;
import com.kt.smp.stt.train.trainData.repository.SttTrainDataRepository;
import com.kt.smp.stt.verify.data.dto.AnswerSheetDto;
import com.kt.smp.stt.verify.data.dto.VerifyDataDto;
import com.kt.smp.stt.verify.data.dto.VerifyDataSaveDto;
import com.kt.smp.stt.verify.data.service.VerifyDataService;
import com.kt.smp.stt.verify.dataset.dto.VerifyDatasetDto;
import com.kt.smp.stt.verify.dataset.service.VerifyDatasetService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DictationService {

  private static final String CONFIDENCE_PATH = "/stt/confidence/";
  private static final String TAR_GZ = ".tar.gz";
  private static final String TXT = ".txt";
  private static final String SAMPLE = ".sample";
  private final SttTrainDataRepository sttTrainDataRepository;

  private final AudioCrypto audioCrypto;
  private final SttErrorService sttErrorService;
  private final DictationRepository dictationRepository;
  private final UsageRepository usageRepository;
  private final LogRepository logRepository;
  private final DirectoryService directoryService;
  private final VerifyDatasetService verifyDatasetService;
  private final VerifyDataService verifyDataService;
  private final RestTemplate restTemplate;
  private final EngineUrlResolver engineUrlResolver;
  private final TextCrypto textCrypto;
  private final ConfigService configService;

  @Value("${directory.home}")
  private String directoryHome;

  public int count(DictationSearchCondition searchCondition) {
    String searchDictatedText = searchCondition.getDictatedText();
    setEncryptedTextForSearch(searchCondition, searchDictatedText);

    return dictationRepository.count(searchCondition);
  }

  public List<DictationListDto> search(DictationSearchCondition searchCondition) {
    List<DictationListDto> result = dictationRepository.search(searchCondition);
    for (DictationListDto data : result) {
      String decryptTranscript = textCrypto.decrypt(data.getTranscript());
      data.setTranscript(decryptTranscript);
      String decryptText = textCrypto.decrypt(data.getDictatedText());
      data.setDictatedText(decryptText);
    }
    return result;
  }

  /**
   * @param searchCondition
   * @param searchDictatedText
   * @MethodName : setEncryptedTextForSearch
   * @작성일 : 2023. 12. 11.
   * @작성자 : rapeech
   * @변경이력 :
   * @Method설명 : 암호화된 전사데이터 텍스트를 조회하기 위해 검색 텍스트 암호화
   */
  private void setEncryptedTextForSearch(DictationSearchCondition searchCondition, String searchDictatedText) {
    if (searchDictatedText != null && !searchDictatedText.equals("")) {
      searchCondition.setDictatedText(textCrypto.encrypt(searchDictatedText));
    }
  }

  public DictationDto get(Integer id) {
    DictationDto dictation = dictationRepository.findById(id);
    dictation.setDictatedText(textCrypto.decrypt(dictation.getDictatedText()));
    dictation.setTranscript(textCrypto.decrypt(dictation.getTranscript()));
    if (dictation == null) {
      throw new IllegalArgumentException("등록되지 않은 전사데이터입니다");
    }

    setUsage(dictation);
    setPrevAndNext(dictation);
    return dictation;
  }

  private void setPrevAndNext(DictationDto target) {

    List<DictationDto> dictationList = dictationRepository.findAll();
    for (int i = 0; i < dictationList.size(); i++) {

      DictationDto prev = (i == 0) ? null : dictationList.get(i - 1);
      DictationDto next = (i == dictationList.size() - 1) ? null : dictationList.get(i + 1);
      DictationDto cur = dictationList.get(i);

      if (cur.isEqual(target)) {
        target.setPrev(prev);
        target.setNext(next);
      }
    }
  }

  private void setUsage(DictationDto dictation) {

    List<UsageDto> usageList = usageRepository.findByDictationId(dictation.getId());

    if (dictation.getUsageType().equals(UsageType.VERIFY_DATA)) {
      List<Integer> verifyDatasetIdList = getVerifyDatasetIdList(usageList);
      dictation.setVerifyDatasetIdList(verifyDatasetIdList);
      return;
    }

    if (dictation.getUsageType().equals(UsageType.AM_TRAIN_DATA)) {
      List<Integer> amTrainDataDirectoryIdList = getAmTrainDataDirectoryIdList(usageList);
      if (amTrainDataDirectoryIdList.size() > 0) {
        dictation.setAmTrainDataDirectoryId(amTrainDataDirectoryIdList.get(0));
      }
    }
  }

  private List<Integer> getVerifyDatasetIdList(List<UsageDto> usageList) {

    return usageList.stream()
      .filter(usage -> usage.getType().equals(UsageType.VERIFY_DATA))
      .map(UsageDto::getVerifyDatasetId)
      .collect(Collectors.toList());
  }

  private List<Integer> getAmTrainDataDirectoryIdList(List<UsageDto> usageList) {

    return usageList.stream()
      .filter(usage -> usage.getType().equals(UsageType.AM_TRAIN_DATA))
      .map(UsageDto::getAmTrainDataDirectoryId)
      .collect(Collectors.toList());
  }

  @Transactional
  public void save(DictationSaveDto dictation) {

    String useYn = dictationRepository.findUseYnBySttLogId(dictation.getSttLogId());

//    	String projectCode = TenantContextHolder.getProjectCode();
//    	
//    	if(PreferenceValueHolder.textEncrypt.get(projectCode)) {
//    		dictation.setTranscript(textCrypto.encrypt(dictation.getTranscript()));
//    	} else {
//    		dictation.setTranscript(textCrypto.decrypt(dictation.getTranscript()));
//    	}

    if (useYn == null) {
      dictationRepository.save(dictation);
    } else if (useYn.equals("N")) {
      dictationRepository.updateReUse(dictation);
    }

  }

  @Transactional
  public void update(DictationUpdateDto modifiedDictation) {

    updateUsage(modifiedDictation);

//        if (modifiedDictation.getUsageList().isEmpty()) {
//            modifiedDictation.setUsageType(UsageType.NONE);
//        }
    String projectCode = TenantContextHolder.getProjectCode();
    if (PreferenceValueHolder.textEncrypt.get(projectCode)) {
      modifiedDictation.setDictatedText(textCrypto.encrypt(modifiedDictation.getDictatedText()));
    }

    dictationRepository.update(modifiedDictation);
  }

  @Transactional
  public void update(DictationUpdateDto modifiedDictation, Map<String, Object> resultMap, List<Path> resultList, List<String> dictatedTextList) {

    log.info("================== start update() ====================");
    updateUsage(modifiedDictation, resultMap, resultList, dictatedTextList);

//        if (modifiedDictation.getUsageList().isEmpty()) {
//            modifiedDictation.setUsageType(UsageType.NONE);
//        }

    dictationRepository.update(modifiedDictation);
  }

  @Transactional
  public void delete(DictationDeleteDto target) {
    List<Integer> targetIdList = target.getTargetIdList();
    for (int id : targetIdList) {
      changeUsedAsDictationOfLog(id);
      usageRepository.deleteByDictationId(id);
      dictationRepository.delete(id);
    }
  }

  @Transactional
  public void saveUsage(HttpServletRequest request, BulkUsageSaveDto usageSave) {
    Map<String, Object> finalWavFilePath = new HashMap<>();

    List<Path> resultPathList = new ArrayList<>();
    List<String> resultDictatedTextList = new ArrayList<>();
    List<Integer> dictationIdList = usageSave.getDictationIdList();

    usageSave.setAmDataPath(directoryHome); // directoryHome + basePath + detailPath + serviceCode + /
    String amDataPath = usageSave.getAmDataPath();
    finalWavFilePath.put("serviceCode", usageSave.getServiceModelId());
    finalWavFilePath.put("amDataPath", amDataPath);

    List<DictationUpdateDto> modifiedDictationList = makeModifiedDictationFrom(usageSave);

    for (DictationUpdateDto modifiedDictation : modifiedDictationList) {
      log.info(">>> modifiedDictation.getId : " + modifiedDictation.getId() + " / modifiedDictation.getUsageType : " + modifiedDictation.getUsageType());

      if (modifiedDictation.getDictatedText() == null || modifiedDictation.getDictatedText().isBlank()) {
        throw new IllegalArgumentException("전사데이터 항목이 입력되어야 합니다.");
      }

      modifiedDictation.audit(request);
      update(modifiedDictation, finalWavFilePath, resultPathList, resultDictatedTextList);
    }

    // 위 반복문에서 AM학습데이터 경로로 복사한 음원파일들을 tar.gz형식으로 압축한 파일과, 정답지파일 생성
    String directoryPath = (String) finalWavFilePath.get("directoryPath");
    String getCurrentDateTime = currentDateTime();
    String wavFileName = "am_traindata_" + getCurrentDateTime + TAR_GZ;
    String answerFileName = "answer_" + getCurrentDateTime + SAMPLE;
    try {
      log.info(">> directoryPath : " + directoryPath);
      log.info(">> usageSave.getServiceModelId : " + usageSave.getServiceModelId());
      log.info(">>> wavFileName : " + wavFileName);
      log.info(">> answerFileName : " + answerFileName);

      List<Path> tarPathList = resultPathList;
      Path tarOutputPath = Paths.get(directoryPath, wavFileName);
      Path answerOutputPath = Paths.get(directoryPath, answerFileName);

      createTarGzipFiles(tarPathList, tarOutputPath);
      createAnswerFile(resultDictatedTextList, answerOutputPath);
    } catch (IOException e) {
      //e.printStackTrace();
      log.error("wav file tar.gz archive create ERROR {}", e.getMessage());
    }

    // 여기에 am학습데이터로 저장하는 로직 들어가야함
    SttTrainDataMultipartSaveDto newData = new SttTrainDataMultipartSaveDto();
    newData.audit(request);
    newData.setAmDataPath(Paths.get(directoryPath).toString());
    newData.setTrainVoiceCount(dictationIdList.size());
    newData.setVoiceFileName(wavFileName);
    newData.setAnswerFileName(answerFileName);
    newData.setModelType(usageSave.getModelType());
    newData.setServiceModelId(usageSave.getServiceModelId());
    //String datasetName =getCurrentDateTime+" 전사데이터 학습데이터셋";
    newData.setDatasetName(usageSave.getDatasetName());
    newData.setDescription(usageSave.getDescription());
    newData.setBasePath(usageSave.getBasePath());
    newData.setDetailPath(usageSave.getDetailPath());
    newData.setDataSource(2); // 데이터 출처가 전사데이터일 경우 2

    sttTrainDataRepository.amInsert(newData);

  }

  private void createAnswerFile(List<String> list, Path path) {
    File file = path.toFile();
    try (FileOutputStream fos = new FileOutputStream(file);
         OutputStreamWriter osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
         BufferedWriter writer = new BufferedWriter(osw)) {

      for (int i = 0; i < list.size(); i++) {
        log.info(">>>>> before file input answer list value : " + list.get(i));
        String data = list.get(i);
        String[] splitData = data.split("\\|");
        String fileName = splitData[0];
        String dictatedData = textCrypto.decrypt(splitData[1]);

        writer.append(fileName + "\t" + dictatedData);
        writer.newLine();

      }
      writer.flush();
    } catch (IOException e) {
      log.error("[create answer file fail] ERROR {}", e);
      throw new DictationException("정답지 파일을 생성하는 중 에러가 발생하였습니다", e);
    }

  }

  private List<DictationUpdateDto> makeModifiedDictationFrom(BulkUsageSaveDto usageSave) {

    Map<UsageType, List<Integer>> splitMap = splitDictation(usageSave.getDictationIdList());
    List<DictationUpdateDto> result = new ArrayList<>();
    result.addAll(makeModifiedDictationForAmTrainData(splitMap, usageSave));
    //result.addAll(makeModifiedDictationForVerifyData(splitMap, usageSave)); 전사데이터에서 검증데이터로 밀어넣는 업무는 없다고함

    return result;

  }

  private List<DictationUpdateDto> makeModifiedDictationForAmTrainData(Map<UsageType, List<Integer>> splitMap, BulkUsageSaveDto usageSave) {

    List<Integer> dictationIdList = splitMap.get(UsageType.AM_TRAIN_DATA);
    List<DictationUpdateDto> modifiedDictations = new ArrayList<>();

    for (Integer dictationId : dictationIdList) {

      DictationDto dictation = dictationRepository.findById(dictationId);

      if (dictation == null) {
        throw new IllegalArgumentException("등록되지 않은 전사데이터입니다");
      }

      log.info(">>>> dictation.getDictatedText: " + dictation.getDictatedText() + " / dictation.getWavFilePath: " + dictation.getWavFilePath()
        + " / dictation.getWavFileName: " + dictation.getWavFileName());

//      UsageSaveDto newUsage = UsageSaveDto.makeAmTrainDataUsageSave(dictation.getId(), usageSave.getAmTrainDataDirectoryId());
      UsageSaveDto newUsage = UsageSaveDto.makeAmTrainDataUsageSave(dictation.getId(), usageSave.getAmDataPath());

      DictationUpdateDto modifiedDictation = new DictationUpdateDto(dictation, UsageType.AM_TRAIN_DATA);

      log.info(">>> after newUsage.getAmDataPath :" + newUsage.getAmDataPath() + " / newUsage.getDictationId :" + newUsage.getDictationId());
      modifiedDictation.addUsage(newUsage);

      log.info(">>>>> after modifiedDictation.getDictatedText : " + modifiedDictation.getDictatedText() + " / modifiedDictation.getUsageType :" + modifiedDictation.getUsageType());
      modifiedDictations.add(modifiedDictation);

    }

    return modifiedDictations;
  }

  private List<DictationUpdateDto> makeModifiedDictationForVerifyData(Map<UsageType, List<Integer>> splitMap, BulkUsageSaveDto usageSave) {

    List<Integer> dictationIdList = splitMap.get(UsageType.VERIFY_DATA);
    List<DictationUpdateDto> modifiedDictations = new ArrayList<>();

    for (Integer dictationId : dictationIdList) {

      DictationDto dictation = dictationRepository.findById(dictationId);
      if (dictation == null) {
        throw new IllegalArgumentException("등록되지 않은 전사데이터입니다");
      }

      if (StringUtils.isBlank(dictation.getDictatedText())) {
        throw new IllegalArgumentException("전사데이터가 작성되지 않았습니다(음원파일: " + dictation.getWavFileName() + ")");
      }

      DictationUpdateDto modifiedDictation = new DictationUpdateDto(dictation, UsageType.VERIFY_DATA);
      for (Integer datasetId : usageSave.getVerifyDatasetIdList()) {
        UsageSaveDto newUsage = UsageSaveDto.makeVerifyDataUsageSave(dictationId, datasetId);
        modifiedDictation.addUsage(newUsage);
      }

      modifiedDictations.add(modifiedDictation);
    }

    return modifiedDictations;
  }

  private Map<UsageType, List<Integer>> splitDictation(List<Integer> dictationIdList) {

    int numOfDictationUsingAsVerifyData = (int) Math.round(dictationIdList.size() * 0.1);

    List<Integer> indexList = getShuffledIndexList(dictationIdList.size());
    List<Integer> indexesForVerifyData = indexList.subList(0, numOfDictationUsingAsVerifyData);
    List<Integer> indexesForAmTrainData = indexList.subList(numOfDictationUsingAsVerifyData, dictationIdList.size());

    List<Integer> dictationsUsingAsVerifyData = indexesForVerifyData.stream()
      .map(dictationIdList::get)
      .collect(Collectors.toList());

    List<Integer> dictationsUsingAsAmTrainData = indexesForAmTrainData.stream()
      .map(dictationIdList::get)
      .collect(Collectors.toList());

    Map<UsageType, List<Integer>> result = new HashMap<>();
    result.put(UsageType.VERIFY_DATA, dictationsUsingAsVerifyData);
    result.put(UsageType.AM_TRAIN_DATA, dictationsUsingAsAmTrainData);

    return result;
  }

  private List<Integer> getShuffledIndexList(int totalSize) {
    List<Integer> indexList = new ArrayList<>();
    for (int i = 0; i < totalSize; i++) {
      indexList.add(i);
    }

    Collections.shuffle(indexList);

    return indexList;
  }

  private void changeUsedAsDictationOfLog(Integer dictationId) {
    DictationDto dictation = dictationRepository.findById(dictationId);
    if (dictation != null) {
      LogDto log = logRepository.findByWavFilePath(dictation.getWavFilePath());
      if (log != null) {
        logRepository.updateUsedAsDictation(log.getId(), "N");
      }
    }
  }

  private void updateUsage(DictationUpdateDto modifiedDictation) {

    try {
      DictationDto dictation = dictationRepository.findById(modifiedDictation.getId());

      if (modifiedDictation.hasSameUsageTypeWith(dictation)) {
        changeUsageList(dictation, modifiedDictation);
        return;
      }

      changeUsageType(dictation, modifiedDictation);

    } catch (NullPointerException e) {
      throw new IllegalArgumentException("수정 중 오류가 발생하였습니다.");
    }

  }

  private void updateUsage(DictationUpdateDto modifiedDictation, Map<String, Object> resultMap, List<Path> resultList, List<String> dictatedTextList) {

    try {
      DictationDto dictation = dictationRepository.findById(modifiedDictation.getId());

//    		String projectCode = TenantContextHolder.getProjectCode();
//    		String dicteatedText = dictation.getDictatedText();
//    		if (PreferenceValueHolder.textEncrypt.get(projectCode)) {
//    			dictation.setDictatedText(textCrypto.encrypt(dicteatedText));
//    		} else {
//    			dictation.setDictatedText(textCrypto.decrypt(dicteatedText));
//    		}

      if (modifiedDictation.hasSameUsageTypeWith(dictation)) {
        changeUsageList(dictation, modifiedDictation, resultMap, resultList, dictatedTextList);
        return;
      }

      changeUsageType(dictation, modifiedDictation, resultMap, resultList, dictatedTextList);

    } catch (NullPointerException e) {
      throw new IllegalArgumentException("수정 중 오류가 발생하였습니다.");
    }

  }

  private void changeUsageList(DictationDto dictation, DictationUpdateDto modifiedDictation) {
    log.info("================ start changeUsageList() ================");
    if (dictation.getUsageType().equals(UsageType.AM_TRAIN_DATA)) {
      changeAmTrainDataUsageList(dictation, modifiedDictation);
      return;
    }

    changeVerifyDataUsageList(dictation, modifiedDictation);

  }

  private void changeUsageList(DictationDto dictation, DictationUpdateDto modifiedDictation, Map<String, Object> resultMap, List<Path> resultList, List<String> dictatedTextList) {

    log.info("================ start changeUsageList() ================");
    if (dictation.getUsageType().equals(UsageType.AM_TRAIN_DATA)) {
      changeAmTrainDataUsageList(dictation, modifiedDictation, resultMap, resultList, dictatedTextList);
      return;
    }

    changeVerifyDataUsageList(dictation, modifiedDictation);

  }

  private void changeAmTrainDataUsageList(DictationDto dictation, DictationUpdateDto modifiedDictation) {

    List<UsageDto> oldUsageList = usageRepository.findByDictationIdAndType(dictation.getId(), UsageType.AM_TRAIN_DATA);
    List<UsageSaveDto> newUsageList = modifiedDictation.getUsageList();

    for (UsageDto oldUsage : oldUsageList) {
      excludeAmTrainDataUsage(oldUsage, newUsageList);
    }

    for (UsageSaveDto newUsage : newUsageList) {
      newUsage.setWavFileName(dictation.getWavFileName());
      newUsage.audit(modifiedDictation);
      includeAmTrainDataUsage(dictation, newUsage, oldUsageList);
    }
  }

  private void changeAmTrainDataUsageList(DictationDto dictation, DictationUpdateDto modifiedDictation, Map<String, Object> resultMap, List<Path> resultList, List<String> dictatedTextList) {

    log.info("============== start changeAmTrainDataUsageList() =============");
    List<UsageDto> oldUsageList = usageRepository.findByDictationIdAndType(dictation.getId(), UsageType.AM_TRAIN_DATA);
    List<UsageSaveDto> newUsageList = modifiedDictation.getUsageList();

    for (UsageDto oldUsage : oldUsageList) {
      excludeAmTrainDataUsage(oldUsage, newUsageList);
    }

    for (UsageSaveDto newUsage : newUsageList) {
      newUsage.setWavFileName(dictation.getWavFileName());
      newUsage.audit(modifiedDictation);
      includeAmTrainDataUsage(dictation, newUsage, oldUsageList, resultMap, resultList, dictatedTextList);
    }
  }

  private void excludeAmTrainDataUsage(UsageDto oldUsage, List<UsageSaveDto> newUsageList) {
    log.info("============== start excludeAmTrainDataUsage() =============");
    /*for (UsageSaveDto newUsage : newUsageList) {
      if (newUsage.isSameAmTrainDataUsage(oldUsage)) {
        return;
      }
    }*/

    deleteAmTrainDataUsage(oldUsage);
  }

  private void includeAmTrainDataUsage(DictationDto dictation, UsageSaveDto newUsage, List<UsageDto> oldUsageList) {

    for (UsageDto oldUsage : oldUsageList) {
      if (newUsage.isSameAmTrainDataUsage(oldUsage)) {
        return;
      }
    }

    Path source = Paths.get(dictation.getWavFilePath());
//    String directoryPath = directoryService.getAbsolutePath(newUsage.getAmTrainDataDirectoryId());
    String directoryPath = newUsage.getAmDataPath();
    Path target = Paths.get(directoryPath, source.getFileName().toString());

    try {
      Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
      usageRepository.save(newUsage);
    } catch (IOException ex) {
      throw new DictationException("AM 학습데이터 음원파일 복사에 실패했습니다(원본파일 경로: " + source + ", 복사본 경로: " + target + ")", ex);
    }
  }

  private void includeAmTrainDataUsage(DictationDto dictation, UsageSaveDto newUsage, List<UsageDto> oldUsageList, Map<String, Object> resultMap, List<Path> resultList, List<String> dictatedTextList) {
    log.info("========== start includeAmTrainDataUsage() =========");
//        for (UsageDto oldUsage : oldUsageList) {
//            if (newUsage.isSameAmTrainDataUsage(oldUsage)) {
//                return; 기존 로직
//            }
//        }

    String serviceCode = resultMap.get("serviceCode") + "";
    Path source = Paths.get(dictation.getWavFilePath());

//    String directoryPath = directoryService.getAbsolutePath(newUsage.getAmTrainDataDirectoryId());
    String directoryPath = newUsage.getAmDataPath();

    Path target = Paths.get(directoryPath, source.getFileName().toString());
    resultMap.put("directoryPath", directoryPath);
    resultList.add(target);
    dictatedTextList.add(source.getFileName().toString() + "|" + dictation.getDictatedText());

    try {

      File targetFolder = new File(target.toString());

      if (!targetFolder.exists()) {
        targetFolder.mkdirs();
      }

      Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
      usageRepository.save(newUsage);
    } catch (IOException ex) {
      throw new DictationException("AM 학습데이터 음원파일 복사에 실패했습니다(원본파일 경로: " + source + ", 복사본 경로: " + target + ")", ex);
    }
  }

  private void changeVerifyDataUsageList(DictationDto dictation, DictationUpdateDto modifiedDictation) {

    List<UsageDto> oldUsageList = usageRepository.findByDictationIdAndType(dictation.getId(), UsageType.VERIFY_DATA);
    List<UsageSaveDto> newUsageList = modifiedDictation.getUsageList();

    for (UsageDto oldUsage : oldUsageList) {
      excludeVerifyDataUsage(oldUsage, newUsageList);
    }

    for (UsageSaveDto newUsage : newUsageList) {
      newUsage.setWavFileName(dictation.getWavFileName());
      newUsage.audit(modifiedDictation);
      includeVerifyDataUsage(dictation, newUsage, oldUsageList);
    }
  }

  private void excludeVerifyDataUsage(UsageDto oldUsage, List<UsageSaveDto> newUsageList) {

    for (UsageSaveDto newUsage : newUsageList) {
      if (newUsage.isSameVerifyDataUsageWith(oldUsage)) {
        return;
      }
    }

    VerifyDatasetDto verifyDataset = verifyDatasetService.get(oldUsage.getVerifyDatasetId());
    VerifyDataDto verifyData = verifyDataService.getByDatasetAndWavFile(verifyDataset, oldUsage.getWavFileName());

    if (verifyData == null) {
      return;
    }

    Path wavFilePath = Paths.get(verifyDataset.getDirectoryPath(), verifyData.getWavFileName());
    try {

      Files.deleteIfExists(wavFilePath);
      usageRepository.delete(oldUsage.getId());
      verifyDataService.delete(verifyData);

    } catch (IOException ex) {
      throw new DictationException("검증데이터 음원파일 삭제에 실패했습니다(경로: " + wavFilePath + ")", ex);
    }

  }

  private void includeVerifyDataUsage(DictationDto dictation, UsageSaveDto newUsage, List<UsageDto> oldUsageList) {

    for (UsageDto oldUsage : oldUsageList) {
      if (newUsage.isSameVerifyDataUsageWith(oldUsage)) {
        updateVerifyDataUsage(dictation, newUsage);
        return;
      }
    }

    saveVerifyDataUsage(dictation, newUsage);
  }

  private void changeUsageType(DictationDto dictation, DictationUpdateDto modifiedDictation) {

    if (dictation.getUsageType().equals(UsageType.AM_TRAIN_DATA)) {

      if (modifiedDictation.getUsageType().equals(UsageType.VERIFY_DATA)) {
        changeAmTrainDataToVerifyData(dictation, modifiedDictation);
        return;
      }

      deleteAllAmTrainDataUsage(dictation);
      return;
    }

    if (dictation.getUsageType().equals(UsageType.VERIFY_DATA)) {

      if (modifiedDictation.getUsageType().equals(UsageType.AM_TRAIN_DATA)) {
        changeVerifyDataToAmTrainData(dictation, modifiedDictation);
        return;
      }

      deleteAllVerifyDataUsage(dictation);
      return;
    }

    if (dictation.getUsageType().equals(UsageType.NONE)) {

      if (modifiedDictation.getUsageType().equals(UsageType.AM_TRAIN_DATA)) {
        saveAllAmTrainData(dictation, modifiedDictation);
        return;
      }

      if (modifiedDictation.getUsageType().equals(UsageType.VERIFY_DATA)) {
        //saveAllVerifyDataUsage(dictation, modifiedDictation);  전사데이터에서 검증데이터로 밀어넣는 업무는 없다고함
      }
    }

  }

  private void changeUsageType(DictationDto dictation, DictationUpdateDto modifiedDictation, Map<String, Object> resultMap, List<Path> resultList, List<String> dictatedTextList) {

    if (dictation.getUsageType().equals(UsageType.AM_TRAIN_DATA)) {

      if (modifiedDictation.getUsageType().equals(UsageType.VERIFY_DATA)) {
        changeAmTrainDataToVerifyData(dictation, modifiedDictation);
        return;
      }

      deleteAllAmTrainDataUsage(dictation);
      return;
    }

    if (dictation.getUsageType().equals(UsageType.VERIFY_DATA)) {

      if (modifiedDictation.getUsageType().equals(UsageType.AM_TRAIN_DATA)) {
        changeVerifyDataToAmTrainData(dictation, modifiedDictation);
        return;
      }

      deleteAllVerifyDataUsage(dictation);
      return;
    }

    if (dictation.getUsageType().equals(UsageType.NONE)) {

      if (modifiedDictation.getUsageType().equals(UsageType.AM_TRAIN_DATA)) {
        saveAllAmTrainData(dictation, modifiedDictation, resultMap, resultList, dictatedTextList);
        return;
      }

      if (modifiedDictation.getUsageType().equals(UsageType.VERIFY_DATA)) {
        //saveAllVerifyDataUsage(dictation, modifiedDictation); 기존 로직임
      }
    }

  }

  private void changeVerifyDataToAmTrainData(DictationDto dictation, DictationUpdateDto modifiedDictation) {

    deleteAllVerifyDataUsage(dictation);
    saveAllAmTrainData(dictation, modifiedDictation);
  }

  private void saveAllAmTrainData(DictationDto dictation, DictationUpdateDto modifiedDictation) {

    List<UsageSaveDto> newUsageList = modifiedDictation.getUsageList();

    for (UsageSaveDto newUsage : newUsageList) {

      newUsage.setWavFileName(dictation.getWavFileName());
      newUsage.audit(modifiedDictation);

//      String directoryPath = directoryService.getAbsolutePath(newUsage.getAmTrainDataDirectoryId());
      String directoryPath = newUsage.getAmDataPath();
      Path source = Paths.get(dictation.getWavFilePath());
      Path target = Paths.get(directoryPath, source.getFileName().toString());

      try {

        File targetFolder = new File(source.toString());
        if (!targetFolder.exists()) {
          targetFolder.mkdirs();
        }

        Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
        usageRepository.save(newUsage);

      } catch (IOException ex) {
        throw new DictationException("AM학습데이터 음원파일 복사에 실패했습니다(원본파일 경로: " + source + ", 복사본 경로: " + target + ")", ex);
      }
    }
  }

  private void saveAllAmTrainData(DictationDto dictation, DictationUpdateDto modifiedDictation, Map<String, Object> resultMap, List<Path> resultList, List<String> dictatedTextList) {

    List<UsageSaveDto> newUsageList = modifiedDictation.getUsageList();
    String directoryPath = "";

    for (UsageSaveDto newUsage : newUsageList) {

      newUsage.setWavFileName(dictation.getWavFileName());
      newUsage.audit(modifiedDictation);
      String serviceCode = resultMap.get("serviceCode") + "";

      directoryPath = newUsage.getAmTrainDataDirectoryId() != null ? Paths.get(directoryService.getAbsolutePath(newUsage.getAmTrainDataDirectoryId()), serviceCode).toString() : newUsage.getAmDataPath();
      log.info(">>> directoryPath : " + directoryPath);

      //directoryPath = "C:\\Users\\USER\\git\\KT-AMP\\kt-stt-service_r\\nas"+directoryPath;
      Path source = Paths.get(dictation.getWavFilePath());
      log.info(">>>>> source dictation.getWavFilePath() : " + dictation.getWavFilePath());
      Path target = Paths.get(directoryPath, source.getFileName().toString());
      log.info(">>>> tartget : " + target.toString());
      resultMap.put("directoryPath", directoryPath);
      resultList.add(target);
      dictatedTextList.add(source.getFileName().toString() + "|" + dictation.getDictatedText());
      try {

        File targetFolder = new File(target.toString());

        if (!targetFolder.exists()) {
          targetFolder.mkdirs();
        }
        Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);

        usageRepository.save(newUsage);

      } catch (IOException ex) {
        throw new DictationException("AM학습데이터 음원파일 복사에 실패했습니다(원본파일 경로: " + source + ", 복사본 경로: " + target + ")", ex);
      }
    } // End for


  }

  private void createTarGzipFiles(List<Path> paths, Path output) throws IOException {

    try (OutputStream fOut = Files.newOutputStream(output, CREATE);
         BufferedOutputStream buffOut = new BufferedOutputStream(fOut);
         GzipCompressorOutputStream gzOut = new GzipCompressorOutputStream(buffOut);
         TarArchiveOutputStream tOut = new TarArchiveOutputStream(gzOut)) {

      for (Path path : paths) {
        if (!Files.isRegularFile(path)) {
          throw new IOException("!!! Support only file !!!");
        }

        TarArchiveEntry tarEntry = new TarArchiveEntry(path.toFile(), path.getFileName().toString());
        log.info(">>> tarEntry.getName : " + tarEntry.getName() + "/ tarEntry.getSize : " + tarEntry.getSize() + " / tarEntry.getRealSize: " + tarEntry.getRealSize());
        tOut.putArchiveEntry(tarEntry);

        // copy file to TarArchiveOutputStream
        Files.copy(path, tOut);

        tOut.closeArchiveEntry();
      } // end for

      tOut.finish();
    }
  }

  private void createZipFiles(List<Path> paths, Path output) throws IOException {

    try (OutputStream fOut = Files.newOutputStream(output, CREATE);
         BufferedOutputStream buffOut = new BufferedOutputStream(fOut);
         ZipOutputStream zOut = new ZipOutputStream(buffOut);
    ) {

      for (Path path : paths) {
        if (!Files.isRegularFile(path)) {
          throw new IOException("!!! Support only file !!!");
        }

        ZipEntry zipEntry = new ZipEntry(path.toFile().getName());
        log.info(">>> zipEntry.getName : " + zipEntry.getName() + "/ zipEntry.getSize : " + zipEntry.getSize());
        zOut.putNextEntry(zipEntry);

        // copy file to TarArchiveOutputStream
        Files.copy(path, zOut);

        zOut.closeEntry();
      } // end for

      zOut.finish();
    }
  }

  private String currentDateTime() {
    String format = "MMdd-HHmmss";
    LocalDateTime now = LocalDateTime.now();
    String formatedNow = now.format(DateTimeFormatter.ofPattern(format));

    return formatedNow;
  }

  private void deleteAllVerifyDataUsage(DictationDto dictation) {

    List<UsageDto> oldUsageList = usageRepository.findByDictationIdAndType(dictation.getId(), UsageType.VERIFY_DATA);

    for (UsageDto oldUsage : oldUsageList) {

      VerifyDatasetDto verifyDataset = verifyDatasetService.get(oldUsage.getVerifyDatasetId());
      Path wavFilePath = Paths.get(verifyDataset.getDirectoryPath(), oldUsage.getWavFileName());

      try {

        Files.deleteIfExists(wavFilePath);
        VerifyDataDto verifyData = verifyDataService.getByDatasetAndWavFile(verifyDataset, oldUsage.getWavFileName());
        verifyDataService.delete(verifyData);
        usageRepository.delete(oldUsage.getId());

      } catch (IOException ex) {

        throw new DictationException("검증데이터 음원파일 삭제에 실패했습니다(경로: " + wavFilePath + ")", ex);
      }

    }
  }

  private void changeAmTrainDataToVerifyData(DictationDto dictation, DictationUpdateDto modifiedDictation) {

    deleteAllAmTrainDataUsage(dictation);
    saveAllVerifyDataUsage(dictation, modifiedDictation);
  }

  private void saveAllVerifyDataUsage(DictationDto dictation, DictationUpdateDto modifiedDictation) {

    List<UsageSaveDto> newUsageList = modifiedDictation.getUsageList();

    for (UsageSaveDto newUsage : newUsageList) {
      newUsage.setWavFileName(dictation.getWavFileName());
      newUsage.audit(modifiedDictation);
      saveVerifyDataUsage(dictation, newUsage);
    }
  }

  private void deleteAllAmTrainDataUsage(DictationDto dictation) {

    List<UsageDto> oldUsageList = usageRepository.findByDictationIdAndType(dictation.getId(), UsageType.AM_TRAIN_DATA);
    for (UsageDto oldUsage : oldUsageList) {
      deleteAmTrainDataUsage(oldUsage);
    }
  }

  private void saveVerifyDataUsage(DictationDto dictation, UsageSaveDto usage) {

    VerifyDatasetDto verifyDataset = verifyDatasetService.get(usage.getVerifyDatasetId());
    Path source = Paths.get(dictation.getWavFilePath());
    Path target = Paths.get(verifyDataset.getDirectoryPath(), source.getFileName().toString());

    try {

      Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
      VerifyDataSaveDto newVerifyData = new VerifyDataSaveDto(dictation, usage);
      verifyDataService.save(newVerifyData);
      usageRepository.save(usage);

    } catch (IOException ex) {
      throw new DictationException("검증데이터 음원파일 복사에 실패했습니다(원본파일 경로: " + source + ", 복사본 경로: " + target + ")", ex);
    }
  }

  private void updateVerifyDataUsage(DictationDto dictation, UsageSaveDto usage) {

    VerifyDataSaveDto modifiedSaveDto = new VerifyDataSaveDto(dictation, usage);
    verifyDataService.save(modifiedSaveDto);
  }

  private void deleteAmTrainDataUsage(UsageDto usage) {
    log.info("============= start deleteAmTrainDataUsage() ==============");
//    String directoryPath = directoryService.getAbsolutePath(usage.getAmTrainDataDirectoryId());
    String directoryPath = usage.getAmTrainDataDirectoryId() != null ? directoryService.getAbsolutePath(usage.getAmTrainDataDirectoryId()) : usage.getAmDataPath();

    Path wavFilePath = Paths.get(directoryPath, usage.getWavFileName());

    try {
      Files.deleteIfExists(wavFilePath);
      usageRepository.delete(usage.getId());
    } catch (IOException ex) {
      throw new DictationException("AM 학습데이터 음원파일 삭제에 실패했습니다(경로: " + wavFilePath + ")", ex);
    }
  }

  public byte[] getWavFileBytes(Integer id) {
    List<ConfigDto> configDtos = configService.getAllUserDefined();

    for (ConfigDto configDto : configDtos) {
      TenantContextHolder.set(configDto.getProjectCode());
    }

    DictationDto dictation = dictationRepository.findById(id);
    String filePath = dictation.getWavFilePath();

    if (audioCrypto.isEncrypted(filePath)) {
      return audioCrypto.getDecryptedByteArray(filePath);
    }

    Path wavFilePath = Paths.get(filePath);

    try {
      return Files.readAllBytes(Paths.get(filePath));
    } catch (IOException ex) {
      throw new DictationException("음원파일을 읽을 수 없습니다(경로: " + wavFilePath + ")", ex);
    }

  }

  public DictationDto getByWavFilePath(String wavFilePath) {
    return dictationRepository.findByWavFilePath(wavFilePath);
  }

  public void preempt(Integer id) {

    DictationDto dictation = dictationRepository.findById(id);
    if (dictation == null) {
      throw new IllegalArgumentException("등록되지 않은 전사데이터입니다");
    }
    //TODO 사용자 ID에 따라 다르게 처리
    dictationRepository.updatePreempt(dictation.getId(), "Y");
  }

  public void freePreempt(Integer id) {

    DictationDto dictation = dictationRepository.findById(id);
    if (dictation == null) {
      throw new IllegalArgumentException("등록되지 않은 전사데이터입니다");
    }
    //TODO 사용자 ID에 따라 다르게 처리
    dictationRepository.updatePreempt(dictation.getId(), "N");

  }

  public String callApi(String url, ConfidenceDto dto, String requestType, boolean depUrl) {
    ResponseEntity<String> responseEntity = null;
    try {
      String coreUrl = engineUrlResolver.resolve();
      if (depUrl) coreUrl = engineUrlResolver.resolveSub("");
      log.info("URL >>> {}", coreUrl + url + requestType);

      responseEntity = restTemplate.postForEntity(coreUrl + url + requestType, makeRequestData(dto, requestType), String.class);
      if (responseEntity == null) {
        throw new DictationException("반환값이 전달되지 않았습니다");
      } else {
        String response = responseEntity.getBody();
        Map<String, Object> resultMap = JacksonUtil.jsonToMap(response);
        if (!resultMap.get("resultCode").equals("0000")) {
          try {
            String jsonString = JacksonUtil.objectToJson(response);
            sttErrorService.saveErrorLog(jsonString, ErrorTypeCode.IF, url);
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
      }
      log.info("CallAPI RESULT >>> {}", JacksonUtil.objectToJsonWithPrettyPrint(responseEntity));
    } catch (Exception e) {
      log.error("[SttConfidenceCallAPI ERROR] {}", e.getMessage());
      int jsonStart = e.getMessage().indexOf("{");
      String jsonString = e.getMessage().substring(jsonStart);
      BaseResultDto errorResult = JacksonUtil.jsonToObject(jsonString, BaseResultDto.class);
      sttErrorService.saveErrorLog(jsonString, ErrorTypeCode.IF, url);

      throw new DictationException(ResultCode.findByCode(errorResult.getResultCode()).getCode(), e);
    }
    return ObjectUtils.isNotEmpty(responseEntity) ? responseEntity.getBody() : null;
  }

  public String callApi(String url, ConfidenceDto dto, String requestType, String serviceCode) {
    return callApi(url, dto, requestType, serviceCode, false);
  }

  public String callApi(String url, ConfidenceDto dto, String requestType, String serviceCode, boolean depUrl) {
    ResponseEntity<String> responseEntity = null;
    try {
      String coreUrl = engineUrlResolver.resolve();
      if (depUrl) coreUrl = engineUrlResolver.resolveSub("");

      log.info("URL >>> {}", coreUrl + url + requestType);
      dto.setServiceCode(serviceCode);
      responseEntity = restTemplate.postForEntity(coreUrl + url + requestType, makeRequestData(dto, requestType), String.class);
      if (responseEntity == null) {
        throw new DictationException("반환값이 전달되지 않았습니다");
      } else {
        String response = responseEntity.getBody();
        Map<String, Object> resultMap = JacksonUtil.jsonToMap(response);
        if (!resultMap.get("resultCode").equals("0000")) {
          try {
            String jsonString = JacksonUtil.objectToJson(response);
            sttErrorService.saveErrorLog(jsonString, ErrorTypeCode.IF, url);
          } catch (Exception e) {
            e.printStackTrace();
          }
        }
      }
      log.info("CallAPI RESULT >>> {}", JacksonUtil.objectToJsonWithPrettyPrint(responseEntity));
    } catch (Exception e) {
      log.error("[SttConfidenceCallAPI ERROR] {}", e.getMessage());
      int jsonStart = e.getMessage().indexOf("{");
      String jsonString = e.getMessage().substring(jsonStart);
      BaseResultDto errorResult = JacksonUtil.jsonToObject(jsonString, BaseResultDto.class);
      sttErrorService.saveErrorLog(jsonString, ErrorTypeCode.IF, url);

      throw new DictationException(ResultCode.findByCode(errorResult.getResultCode()).getCode(), e);
    }
    return ObjectUtils.isNotEmpty(responseEntity) ? responseEntity.getBody() : null;
  }

  public Map<String, Object> makeRequestData(ConfidenceDto dto, String requestType) {
    Map<String, Object> map = new HashMap<String, Object>();

    if (requestType.equals("stop") || requestType.equals("get")) {
      map.put("serviceCode", dto.getServiceCode());
      return map;
    }

    map.put("serviceCode", dto.getServiceCode());
    map.put("confidence", dto.getConfidence());
    map.put("encFlag", dto.getEncFlag());
    map.put("saveFlag", 1);
    map.put("apiFlag", 1);

    if (requestType.equals("start")) {
      map.put("fileCount", dto.getFileCount());
    }

    log.info("MAP >>> {}", JacksonUtil.objectToJsonWithPrettyPrint(map));
    return map;
  }

  public SttConfigDto confidenceConfig(ConfidenceConfigDto dto) {
    return dictationRepository.confidenceConfig(dto);
  }

  public int confidenceSave(List<ConfidenceConfigInsertDTO> dto) {
    return dictationRepository.confidenceSave(dto);
  }

  public List<ConfidenceConfigInsertDTO> makeInsertData(HttpServletRequest request, ConfidenceDto dto, String type) {
    MasterSmpRequestHeaderDto header = MasterSmpRequestHeaderDto.generate(request);
    String name = header.getUserId();
    String ip = request.getRemoteAddr();
    log.info("SttConfidenceConfigVO >>> {}", JacksonUtil.objectToJsonWithPrettyPrint(dto));
    List<ConfidenceConfigInsertDTO> confidenceConfigInsertDTOList = new ArrayList<>();
    if (type.equals("stop")) {
      ConfidenceConfigInsertDTO confidenceConfigInsertDTO = new ConfidenceConfigInsertDTO();
      confidenceConfigInsertDTO.setCodeValue("N");
      confidenceConfigInsertDTO.setCodeKey(String.valueOf(dto.getServiceCode()));
      confidenceConfigInsertDTO.setDescription("CONFIDENCE_USEYN");
      confidenceConfigInsertDTO.setRegId(name);
      confidenceConfigInsertDTO.setRegIp(ip);
      confidenceConfigInsertDTO.setUpdId(name);
      confidenceConfigInsertDTO.setUpdIp(ip);
      confidenceConfigInsertDTO.setUseYn("Y");
      confidenceConfigInsertDTOList.add(confidenceConfigInsertDTO);
    } else {
      String[] configList = {"CONFIDENCE_CONFIG", "CONFIDENCE_FILE", "CONFIDENCE_ENCRYPT", "CONFIDENCE_USEYN"};
      for (String item : configList) {
        // 신뢰도 저장을 위한 데이터 파싱
        ConfidenceConfigInsertDTO confidenceConfigInsertDTO = new ConfidenceConfigInsertDTO();
        if (item.equals("CONFIDENCE_CONFIG")) {
          confidenceConfigInsertDTO.setCodeValue(String.valueOf(dto.getConfidence()));
        } else if (item.equals("CONFIDENCE_FILE")) {
          confidenceConfigInsertDTO.setCodeValue(String.valueOf(dto.getFileCount()));
        } else if (item.equals("CONFIDENCE_ENCRYPT")) {
          String flag = "Y";
          if (dto.getEncFlag() == 0) {
            flag = "N";
          }
          confidenceConfigInsertDTO.setCodeValue(flag);
        } else if (item.equals("CONFIDENCE_USEYN")) {
          confidenceConfigInsertDTO.setCodeValue("Y");
        }
        confidenceConfigInsertDTO.setCodeKey(String.valueOf(dto.getServiceCode()));
        confidenceConfigInsertDTO.setDescription(item);
        confidenceConfigInsertDTO.setRegId(name);
        confidenceConfigInsertDTO.setRegIp(ip);
        confidenceConfigInsertDTO.setUpdId(name);
        confidenceConfigInsertDTO.setUpdIp(ip);
        confidenceConfigInsertDTO.setUseYn("Y");
        confidenceConfigInsertDTOList.add(confidenceConfigInsertDTO);
      }
    }
    return confidenceConfigInsertDTOList;
  }

  public ConfidenceConfigDto getConfidenceValue(String serviceCode) {

    String callResult = callApi(CONFIDENCE_PATH, ConfidenceDto.builder().serviceCode(serviceCode).build(), "get", true);

    ResponseDto jsonObject = JacksonUtil.jsonToObject(callResult, ResponseDto.class);
    ConfidenceGetResultDto confidenceInfo = jsonObject.getConfidenceInfo();
	ConfidenceConfigDto result = new ConfidenceConfigDto();
	
	if (confidenceInfo != null) {
		result.setConfidence(confidenceInfo.getConfidence());
		result.setEncryptYn(confidenceInfo.getEncFlag() < 1 ? "N" : "Y");
		result.setExeYn(confidenceInfo.getExeFlag() < 1 ? "N" : "Y");
		result.setFileSaveCount(confidenceInfo.getFileSaveCount());
		result.setFileStartCount(confidenceInfo.getFileStartCount());
		result.setServiceCode(serviceCode);
	}
	
	log.info("dto >>> {}",JacksonUtil.objectToJsonWithPrettyPrint(result));
    
    return result;
  }

  @Transactional
  public void saveVerifyData(HttpServletRequest request, DictationToVerifyDataDto verifyDataDto) {
    log.info("[DictationService.saveVerifyData] request verifyDataDto >>> " + verifyDataDto.toString());

    verifyDataDto.audit(request);

    List<String> dictationTextList = new ArrayList<>();
    String saveDirectory = getSaveDirectory(verifyDataDto);

    Random random = new Random();

    List<Path> dictationWavPathList = new ArrayList<>();

    String answerFileName = "answer_" + currentDateTime() + ".answer";

    verifyDataDto.setDatasetId(random.nextInt(99999999)); // 검증데이터셋화면 제거후 혹시 모를 오류방지 위함
    verifyDataDto.setVerifyDataPath(saveDirectory);
    for (int dictationId : verifyDataDto.getDictationIdList()) {
      DictationDto dictation = dictationRepository.findById(dictationId);
      if (dictation == null) {
        continue;
      }

      if (isDictationTextBlank(dictation.getDictatedText())) {
        throw new IllegalArgumentException("전사데이터 항목이 입력되어야 합니다.");
      }

      dictationWavPathList.add(Path.of(dictation.getWavFilePath()));

      dictationTextList.add(dictation.getWavFileName() + "|" + dictation.getDictatedText());
      dictation.setDictatedText(dictation.getDictatedText());

      verifyDataDto.setRegId(dictation.getUpdId());
      VerifyDataSaveDto saveDto = new VerifyDataSaveDto(verifyDataDto, dictation);

      saveDto.setAnswerFileName(answerFileName);
      verifyDataService.save(saveDto);
    }

    copyWavToDatasetDirectory(dictationWavPathList, saveDirectory);

    Path answerOutputPath = Paths.get(saveDirectory + "", answerFileName);

    createAnswerFile(dictationTextList, answerOutputPath);

  }

  private String getSaveDirectory(DictationToVerifyDataDto newData) {

    String basePath = "";
    if (newData.getBasePath().contains("verify")) {
      basePath = "verifyData";
    }

    Path realPath = Paths.get(directoryHome, basePath, newData.getDetailPath(), newData.getServiceModelId().toString());

    return realPath.toString();
  }

  private void copyWavToDatasetDirectory(List<Path> dictationWavPathList, String outputDirectoryPath) {
    File datasetWavDirectoryPath = new File(outputDirectoryPath + "/wav");

    // wav 디렉토리 없으면 생성
    if (!datasetWavDirectoryPath.exists()) {
      datasetWavDirectoryPath.mkdirs();
    }

    // wav파일 검증데이터셋 경로에 모두 복사
    for (Path dictationWavPath : dictationWavPathList) {
      try {
        Files.copy(dictationWavPath, datasetWavDirectoryPath.toPath().resolve(dictationWavPath.getFileName()),
          StandardCopyOption.REPLACE_EXISTING);
      } catch (IOException e) {
        log.info("dictation wav path error >> {}", e);
        throw new DictationException("음원 파일을 복사하는 중 에러가 발생하였습니다", e);
      }
    }
  }

  private boolean isDictationTextBlank(String dictationText) {
    if (dictationText == null || dictationText.equals("")) {
      return true;
    }
    return false;
  }

  public int getIdByWavFilePath(String wavFilePath) {
    return dictationRepository.getIdByWavFilePath(wavFilePath);
  }

  public int findIdBySttLogId(Integer sttLogId) {
    return dictationRepository.findIdBySttLogId(sttLogId);
  }
}
