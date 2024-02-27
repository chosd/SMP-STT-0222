package com.kt.smp.stt.train.trainData.service;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import com.github.pagehelper.Page;
import com.kt.smp.common.util.EncUtil;
import com.kt.smp.common.util.crypto.TextCrypto;
import com.kt.smp.common.util.excel.ExcelFileType;
import com.kt.smp.common.util.excel.ExcelRead;
import com.kt.smp.common.util.excel.ExcelReadOption;
import com.kt.smp.fileutil.builder.SxssfExcelBuilder;
import com.kt.smp.fileutil.util.FileUploadUtil;
import com.kt.smp.multitenancy.config.TenantContextHolder;
import com.kt.smp.multitenancy.dto.ConfigDto;
import com.kt.smp.multitenancy.dto.MasterSmpRequestHeaderDto;
import com.kt.smp.multitenancy.service.ConfigService;
import com.kt.smp.stt.comm.directory.service.DirectoryService;
import com.kt.smp.stt.comm.directory.service.HomePathResolver;
import com.kt.smp.stt.comm.preference.PreferenceValueHolder;
import com.kt.smp.stt.comm.serviceModel.domain.ServiceModelVO;
import com.kt.smp.stt.comm.serviceModel.service.ServiceModelService;
import com.kt.smp.stt.common.TrainDataType;
import com.kt.smp.stt.train.trainData.cache.SttCacheManager;
import com.kt.smp.stt.train.trainData.cache.SttTrainDataBulkCache;
import com.kt.smp.stt.train.trainData.domain.SttTrainAmDataVO;
import com.kt.smp.stt.train.trainData.domain.SttTrainDataAmSearchCondition;
import com.kt.smp.stt.train.trainData.domain.SttTrainDataListVO;
import com.kt.smp.stt.train.trainData.domain.SttTrainDataSearchCondition;
import com.kt.smp.stt.train.trainData.domain.SttTrainDataVO;
import com.kt.smp.stt.train.trainData.dto.SttTrainDataAmSaveDto;
import com.kt.smp.stt.train.trainData.dto.SttTrainDataAnswerSheetDto;
import com.kt.smp.stt.train.trainData.dto.SttTrainDataBulkSaveReqDto;
import com.kt.smp.stt.train.trainData.dto.SttTrainDataMultipartSaveDto;
import com.kt.smp.stt.train.trainData.dto.SttTrainDataSaveReqDto;
import com.kt.smp.stt.train.trainData.dto.SttTrainDatasetListDto;
import com.kt.smp.stt.train.trainData.repository.SttTrainDataRepository;
import com.kt.smp.stt.verify.data.dto.VerifyDataDto;
import com.kt.smp.stt.verify.data.dto.VerifyDataSaveDto;
import com.kt.smp.stt.verify.dataset.dto.VerifyDatasetDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * The type SMP_STT API_연동규격서 결과코드 3.3 정의에 따름 train data service.
 *
 * @author jieun.chang
 * @title SttTrainDataService
 * @see\n
 * 
 *        <pre></pre>
 * 
 * @since 2022-12-08
 */
@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class SttTrainDataService {

	private final SttTrainDataRepository sttTrainDataRepository;

	private final ServiceModelService serviceModelService;

	private static final String AM_TRAIN_DATA_PATH = "/trainData/svc/am/";

	private final ConfigService configService;

	private final TextCrypto textCrypto;
	
	@Value("${directory.home}")
	private String directoryHome;

	@Value("${spring.profiles.active}")
	private String profile;

	/**
	 * list.
	 *
	 * @param searchCondition the search condition
	 * @return the list
	 */
	public Page<SttTrainDataVO> listPage(SttTrainDataSearchCondition searchCondition) {

		Page<SttTrainDataVO> page = sttTrainDataRepository.listPage(searchCondition);
		
		List<SttTrainDataVO> resultList = page.getResult();
		
		for (SttTrainDataVO vo : resultList) {
			vo.setContents(textCrypto.decrypt(vo.getContents()));
		}
		
		return page;
	}

	public Page<SttTrainAmDataVO> amDataSearch(SttTrainDataAmSearchCondition searchCondition) {
		Page<SttTrainAmDataVO> page = sttTrainDataRepository.amDataSearch(searchCondition);

		return page;
	}

	/**
	 * @MethodName : amDatasetAll
	 * @작성일 : 2023. 11. 17.
	 * @작성자 : munho.jang
	 * @변경이력 :
	 * @Method설명 : 전사데이터에서 학습데이터 등록팝업 사용
	 * @return
	 */
	public List<SttTrainDatasetListDto> amDatasetAll() {

		List<SttTrainDatasetListDto> result = sttTrainDataRepository.amDatasetAll();

		return result;
	}

//  public Page<DirectoryListDto> directorySearch(SttTrainDataAmSearchCondition searchCondition) {
//		
//	  Page<DirectoryListDto> page = sttTrainDataRepository.directorySearch(searchCondition);
//
//	  return page;
//  }

	/**
	 * Detail stt train data vo.
	 *
	 * @param trainDataId the train data id
	 * @return the stt train data vo
	 */
	public SttTrainDataVO detail(long trainDataId) {
		if (!sttTrainDataRepository.exists(trainDataId)) {
			throw new IllegalStateException("ERROR: trainDataId does not exists");
		}

		SttTrainDataVO detail = sttTrainDataRepository.detail(trainDataId);
		
		detail.setContents(textCrypto.decrypt(detail.getContents()));
		
		return detail;
	}

	public SttTrainAmDataVO amDetail(long amDataId) {
		if (!sttTrainDataRepository.existsAmData(amDataId)) {
			throw new IllegalStateException("ERROR: amDataId does not exists");
		}

		SttTrainAmDataVO detail = sttTrainDataRepository.amDetail(amDataId);

		return detail;
	}

	/**
	 * Count int.
	 *
	 * @param searchCondition the search condition
	 * @return the int
	 */
	public int count(SttTrainDataSearchCondition searchCondition) {
		return sttTrainDataRepository.count(searchCondition);
	}

	/**
	 * Insert int.
	 *
	 * @param sttTrainDataVO the stt train data vo
	 * @return the int
	 */
	@Transactional
	public int insert(SttTrainDataVO sttTrainDataVO) {
		if (hasInvalidContents(sttTrainDataVO.getContents())) {
			throw new IllegalArgumentException("Error: Invalid insert contents request");
		}

		if (hasInvalidDescription(sttTrainDataVO.getDescription())) {
			throw new IllegalArgumentException("Error: Invalid insert description request");
		}

		String projectCode = TenantContextHolder.getProjectCode();
		if (PreferenceValueHolder.textEncrypt.get(projectCode)) {
			sttTrainDataVO.setContents(textCrypto.encrypt(sttTrainDataVO.getContents()));
		}
		
		return sttTrainDataRepository.insert(sttTrainDataVO);
	}

	@Transactional
	public void amDataInsert(SttTrainDataMultipartSaveDto data, HttpServletRequest request) {
		log.info("============================= start amDataInsert()  ============================== / " + profile);
		// String serviceCode =
		// serviceModelService.detail(data.getServiceModelId()).getServiceCode();
		String homePath = getHomePath(request);
		// Path uploadPath = Paths.get(homePath, "upload", AM_TRAIN_DATA_PATH,
		// serviceCode);
		Path tempPath = Paths.get(homePath, "/trainData/svc/2"); // 수협테스트 위한 임시경로
		log.info(">>>>> after tempPath : " + tempPath);

//    DirectoryListDto getDirectory =  sttTrainDataRepository.directorySearch();
//    log.info(">>>> getDirectory.getPath : "+getDirectory.getPath()); 24.01.12 이전 소스
		String basePath = "";
		if (data.getBasePath().contains("train")) {
			basePath = "trainData";
		}

		Path realPath = Paths.get(homePath, basePath, data.getDetailPath(), data.getServiceModelId().toString());

		log.info(">>>>>> original real path : " + realPath);
		String directory = "";

		directory = realPath + "/";

		if (hasAnswerFile(data)) { // 모델타입(E2ESL,E2EMSL 경우만)에 따라 정답지가 존재
			saveAnswerVoiceFile(data, realPath);
		} else {
			Map<String, Object> resultVoiceFile = saveVoiceFile(data, realPath);
			data.setTrainVoiceCount((Integer) resultVoiceFile.get("voiceFileCnt"));
			data.setAmDataPath(directory);
			data.setDataSource(1); // 데이터 출처가 일반일 경우 1, 전사데이터일 경우 2
			data.setVoiceFileName((String) resultVoiceFile.get("wavFileName"));

			sttTrainDataRepository.amInsert(data);
		}
	}

	/**
	 * @MethodName : amDataDirectInsert
	 * @작성일 : 2024. 01. 17.
	 * @작성자 : chanmi.joo
	 * @변경이력 :
	 * @Method설명 : AM학습데이터 초기 데이터 직접 등록
	 * @param data
	 * @param request
	 * @return
	 */

	@Transactional
	public void amDataDirectInsert(SttTrainDataMultipartSaveDto data, HttpServletRequest request) {
		log.info("============================= start amDataDirectInsert()  ============================== / " + profile);

		String homePath = getHomePath(request);
		Path tempDirectPath = Paths.get(homePath, "train", data.getDetailPath());
		log.info(">>>>> after tempPath : " + tempDirectPath);

		String basePath = "";
		if (data.getBasePath().contains("train")) {
			basePath = "trainData";
		}
		Path realPath = Paths.get(homePath, basePath, data.getDetailPath());

		log.info(">>>>>> original real path : " + realPath);

		data.setAmDataPath(realPath + "/");
		data.setDataSource(1); // 데이터 출처가 일반일 경우 1, 전사데이터일 경우 2
		sttTrainDataRepository.amDirectInsert(data);
	}

	private String getHomePath(HttpServletRequest request) {
		MasterSmpRequestHeaderDto header = MasterSmpRequestHeaderDto.generate(request);
		ConfigDto config = configService.getByProjectCode(header.getProjectCode());
		// return homePathResolver.resolve(config);
		return directoryHome;
	}

	private boolean hasVoiceFile(SttTrainDataMultipartSaveDto newData) {
		return newData.getVoiceFile() != null && !newData.getVoiceFile().isEmpty();
	}

	private boolean hasAnswerFile(SttTrainDataMultipartSaveDto newData) {
		return (newData.getAnswerFile() != null && !newData.getAnswerFile().isEmpty());
	}

	public boolean hasDirectVoiceFile(SttTrainDataMultipartSaveDto newData){
		return (newData.getAnswerFileName() != null
			&& !newData.getAnswerFileName().isEmpty());
	}

	public boolean hasDirectAnswerFile(SttTrainDataMultipartSaveDto newData){
		return (newData.getVoiceFileName() != null
			&& !newData.getVoiceFileName().isEmpty());
	}

	private String getDatasetDirectory(SttTrainDataMultipartSaveDto newData) {
		// VerifyDatasetDto dataset =
		// datasetRepository.findById(newData.getDatasetId());
		// return directoryService.getAbsolutePath(dataset.getDirectoryId());
		return "";
	}

	private Map<String, Object> saveVoiceFile(SttTrainDataMultipartSaveDto newData, Path receivePath) {
		log.info("=============================== start saveVoiceFile() ================================");
		String directory = receivePath + "/";
		log.info(">>>> receive directory : " + directory);
		MultipartFile wavFile = newData.getVoiceFile();
		// log.info(">>>>>> wavFile.getSize : "+wavFile.getSize()+" /
		// wavFile.getOriginalFilename : "+wavFile.getOriginalFilename()+" / directory :
		// "+directory);
		// new TarArchiveInputStream(new GzipCompressorInputStream(new
		// BufferedInputStream(wavFile.getInputStream())))
		try (TarArchiveInputStream tis = new TarArchiveInputStream(new GzipCompressorInputStream(new BufferedInputStream(wavFile.getInputStream())))) {
			Files.createDirectories(receivePath);
			FileUploadUtil.uploadFile(wavFile, directory);
			TarArchiveEntry tarEntry = tis.getNextTarEntry();
			int voicefileCnt = 0;
			while (tarEntry != null) { // 압축파일속 음성파일 갯수 파악하기 위함
				log.info(">>>>>>>> [" + voicefileCnt + "] 번째 파일명 : " + tarEntry.getName());
//    		if (isFile(tarEntry.getName())) {
//    			Path filePath = Paths.get(directory, tarEntry.getName());
//    			// 기존소스 Files.copy(zis, filePath, StandardCopyOption.REPLACE_EXISTING);
//    			File entryFile = new File(filePath.toString());
//    			
//    		}
				String tarInFile = tarEntry.getName();
				if (tarInFile.contains(".wav")) {
					voicefileCnt++;
				}

				tarEntry = tis.getNextTarEntry();
			}
			// log.info(">>>>>> voicefileCnt : "+voicefileCnt);
			Map<String, Object> resultSaveVoiceFile = new HashMap<>();
			log.info(">>> before put voicefileCnt : " + voicefileCnt);
			resultSaveVoiceFile.put("voiceFileCnt", voicefileCnt);
			resultSaveVoiceFile.put("wavFileName", wavFile.getOriginalFilename());

			return resultSaveVoiceFile;

		} catch (IOException ex) {
			ex.printStackTrace();
			throw new IllegalArgumentException("음원파일을 저장할 수 없습니다");
		}
	}

	private void amTrainDataFinalInsert(SttTrainDataMultipartSaveDto data) {
		sttTrainDataRepository.amInsert(data);
	}

	private boolean isFile(String path) {
		return !StringUtils.contains(path, "/");
	}

	private void saveAnswerVoiceFile(SttTrainDataMultipartSaveDto newData, Path receivePath) {
		log.info("================== start saveAnswerVoiceFile() ==================");
		String directory = receivePath + "/";
		List<SttTrainDataAnswerSheetDto> answerSheetList = parseAnswerSheetFile(newData);
		log.info(">>>>> parseAnswerSheetFile after | getWavFileName : " + answerSheetList.get(0).getWavFileName()
				+ " / directory : " + directory);
		validateAnswerSheet(directory, answerSheetList);

		MultipartFile answerFile = newData.getAnswerFile();
		Map<String, Object> resultVoiceFile = saveVoiceFile(newData, receivePath);
		newData.setTrainVoiceCount((Integer) resultVoiceFile.get("voiceFileCnt"));
		newData.setAmDataPath(directory);
		newData.setDataSource(1); // 데이터 출처가 일반일 경우 1, 전사데이터일 경우 2
		newData.setVoiceFileName((String) resultVoiceFile.get("wavFileName"));
		// newData.setAnswerFileName(answerFile.getOriginalFilename());

		Path filePath = Paths.get(directory, answerFile.getOriginalFilename());
		File newFile = new File(filePath.toString());
		try {
			Files.createDirectories(receivePath);
			FileUploadUtil.uploadFile(answerFile, directory);
			// newFile.createNewFile();

		} catch (Exception ex) {
			log.error("!!!!!!!!!!!!! answerfile create fail error !!!!!!!!!!!!!!!!!");
			ex.printStackTrace();
		}
		newData.setAnswerFileName(newFile.getName());
		log.info(">>>> before amInsert | newData.getAmDataPath : " + newData.getAmDataPath()
				+ " / newData.getAnswerFileName : " + newData.getAnswerFileName() + " / newData.getDatasetName : "
				+ newData.getDatasetName() + " / newData.getVoiceFileName : " + newData.getVoiceFileName()
				+ " / newData.getTrainVoiceCount : " + newData.getTrainVoiceCount());
		sttTrainDataRepository.amInsert(newData);
		// List<SttTrainDataAmSaveDto> newAnswerDataList = makeNewAnswerData(newData);
		// for (SttTrainDataAmSaveDto answerData : newAnswerDataList) {
		//
		// if (isNewWavFile(answerData)) {
		// //dataRepository.save(answerData);
		// }
		// else {
		// //updateDictatedText(answerData);
		// }
		// }
	}

	/**
	 * @MethodName : isNewWavFile
	 * @작성일 : 2023. 10. 13.
	 * @작성자 : munho.jang
	 * @변경이력 :
	 * @Method설명 : 기존 검증데이터 저장 처리에서 참조 부분, 여기서는 미사용
	 * @param verifyData
	 * @return
	 */
	private boolean isNewWavFile(VerifyDataSaveDto verifyData) {
		return true; // dataRepository.countByDatasetIdAndWavFileName(verifyData.getDatasetId(),
						// verifyData.getWavFileName()) < 1;
	}

	/**
	 * @MethodName : makeNewAnswerData
	 * @작성일 : 2023. 10. 13.
	 * @작성자 : munho.jang
	 * @변경이력 :
	 * @Method설명 : 기존 검증데이터 저장 처리에서 참조 부분, 여기서는 미사용
	 * @param newData
	 * @return
	 */
	private List<SttTrainDataAmSaveDto> makeNewAnswerData(SttTrainDataMultipartSaveDto newData) {
		String saveDirectory = "";
		List<SttTrainDataAnswerSheetDto> answerSheetList = parseAnswerSheetFile(newData);
		validateAnswerSheet(saveDirectory, answerSheetList);

		return answerSheetList.stream().map(answerSheet -> new SttTrainDataAmSaveDto(newData, answerSheet))
				.collect(Collectors.toList());
	}

	private List<SttTrainDataAnswerSheetDto> parseAnswerSheetFile(SttTrainDataMultipartSaveDto newData) {
		try {
			List<SttTrainDataAnswerSheetDto> answerSheetList = new ArrayList<>();
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(newData.getAnswerFile().getInputStream(), StandardCharsets.UTF_8));

			String line;
			while ((line = reader.readLine()) != null) {
				String[] split = line.split("\t");

				if (split.length != 2) {
					throw new IllegalArgumentException("정답지 양식에 맞지 않는 파일입니다 양식: 음원파일명[tab]전사데이터");
				}

				answerSheetList.add(new SttTrainDataAnswerSheetDto(split[0], split[1]));
			}

			return answerSheetList;
		} catch (IOException ex) {
			throw new IllegalArgumentException("정답지 파일을 읽을 수 없습니다");
		}
	}

	private void validateAnswerSheet(String saveDirectory, List<SttTrainDataAnswerSheetDto> answerSheetList) {
		for (SttTrainDataAnswerSheetDto answerSheet : answerSheetList) {
			if (StringUtils.isBlank(answerSheet.getWavFileName())) {
				throw new IllegalArgumentException("입력되지 않은 음원파일명이 있습니다");
			}

			if (StringUtils.isBlank(answerSheet.getAnswerText())) {
				throw new IllegalArgumentException("입력되지 않은 정답지 데이터가 있습니다");
			}

			File wavFile = Paths.get(saveDirectory, answerSheet.getWavFileName()).toFile();
			// if (!wavFile.exists()) { 기존소스, 우리쪽 서버에 파일 존재 유무 체크로 이 파일을 요청보내는게 아니기 때문에
			// 불필요하다 봄
			// throw new IllegalArgumentException(answerSheet.getWavFileName() + "은 존재하지 않는
			// 파일입니다");
			// }

		}
	}

	/**
	 * Insert int.
	 *
	 * @param sttTrainDataSaveReqDto the stt train data save request vo
	 * @return the int
	 */
	@Transactional
	public int insertBulk(SttTrainDataSaveReqDto sttTrainDataSaveReqDto) {
		return sttTrainDataRepository.insertBulk(sttTrainDataSaveReqDto);
	}

	/**
	 * Update int.
	 *
	 * @param sttTrainDataVO the stt train data vo
	 * @return the int
	 */
	@Transactional
	public int update(SttTrainDataVO sttTrainDataVO) {

		if (hasInvalidContents(sttTrainDataVO.getContents())) {
			throw new IllegalArgumentException("Error: Invalid update contents request");
		}
		if (!StringUtils.isEmpty((sttTrainDataVO.getDescription()))) {
			if (hasInvalidDescription(sttTrainDataVO.getDescription())) {
				throw new IllegalArgumentException("Error: Invalid insert description request");
			}
		}
		
		String projectCode = TenantContextHolder.getProjectCode();
		if (PreferenceValueHolder.textEncrypt.get(projectCode)) {
			sttTrainDataVO.setContents(textCrypto.encrypt(sttTrainDataVO.getContents()));
		}

		return sttTrainDataRepository.update(sttTrainDataVO);
	}

	@Transactional
	public int amUpdate(SttTrainAmDataVO sttTrainAmDataVO) {
		if (hasInvalidDatasetName(sttTrainAmDataVO.getDatasetName())) {
			throw new IllegalArgumentException("Error: Invalid update dataset name request");
		}

		if (sttTrainAmDataVO.getDescription() != null && !sttTrainAmDataVO.getDescription().isBlank()) {
			if (hasInvalidDescription(sttTrainAmDataVO.getDescription())) {
				throw new IllegalArgumentException("Error: Invalid insert description request");
			}
		}

		return sttTrainDataRepository.amUpdate(sttTrainAmDataVO);
	}

	/**
	 * Delete int.
	 *
	 * @param trainDataIdList the train data id list
	 * @return the int
	 */
	@Transactional
	public int delete(SttTrainDataListVO trainDataIdList) {
		return sttTrainDataRepository.delete(trainDataIdList);
	}

	@Transactional
	public int amDelete(SttTrainDataListVO trainAmDataIdList) {
		return sttTrainDataRepository.amDelete(trainAmDataIdList);
	}

//  @Transactional
//  public int datasetDelete(SttTrainDatasetListVO trainDatasetIdList) {
//    return sttTrainDataRepository.datasetDelete(trainDatasetIdList);
//  }

	/**
	 * hasDuplicateContents int.
	 *
	 * @param contents the train data content
	 * @return the int
	 */
	public boolean hasDuplicateContents(String contents, String serviceModelId) {
		String encryptedContents = textCrypto.encrypt(contents);
		
		return (sttTrainDataRepository.countDuplicateContents(contents, encryptedContents, serviceModelId) > 0);
	}

	/**
	 * @MethodName : hasDuplicateDatasetName
	 * @작성일 : 2023. 10. 13.
	 * @작성자 : munho.jang
	 * @변경이력 :
	 * @Method설명 : AM학습데이터 등록시 데이터셋명 중복체크
	 * @param dataset
	 * @param serviceModelId
	 * @return
	 */
	public boolean hasDuplicateDatasetName(String dataset, String serviceModelId) {
		return (sttTrainDataRepository.countDuplicateDatasetName(dataset, serviceModelId) > 0);
	}

	/**
	 * @MethodName : hasDuplicateDirectoryPath
	 * @작성일 : 2024. 1. 11.
	 * @작성자 : munho.jang
	 * @변경이력 :
	 * @Method설명 : AM학습데이터 등록시 하위 디렉토리경로 중복체크
	 * @param path
	 * @return
	 */
	public boolean hasDuplicateDirectoryPath(String path, String serviceModelId) {

		return (sttTrainDataRepository.countDuplicateDirectoryPath(path, serviceModelId) > 0);

	}

	/**
	 * @MethodName : hasServiceModelDirectPath
	 * @작성일 : 2024. 1. 19.
	 * @작성자 : chanmi.joo
	 * @변경이력 :
	 * @Method설명 : AM학습데이터 해당 서비스 모델에 이미 등록된 초기 데이터 경로가 있는지 체크
	 * @param serviceModelId
	 * @return
	 */
	public boolean hasServiceModelDirectPath(String serviceModelId) {

		return (sttTrainDataRepository.countServiceModelDirectPath(serviceModelId) > 0);

	}

	public int getTrainDataCountWithRepeatCount(SttTrainDataSearchCondition searchCondition) {
		log.info(">>>>> before listpage <<<<<<< ");
		Page<SttTrainDataVO> sttTrainDataVOs = listPage(searchCondition);
		int count = 0;

		for (SttTrainDataVO sttTrainDataVO : sttTrainDataVOs) {
			count += sttTrainDataVO.getRepeatCount();
		}

		return count;
	}

	public SttTrainDataListVO getListByServiceModel(SttTrainDataSearchCondition searchCondition) {
		Page<SttTrainDataVO> sttTrainDataVOs = listPage(searchCondition);
		SttTrainDataListVO idList = new SttTrainDataListVO();
		List<Long> list = new ArrayList<>();

		if (sttTrainDataVOs.size() != 0) {
			for (SttTrainDataVO sttTrainDataVO : sttTrainDataVOs) {
				list.add(sttTrainDataVO.getId());
			}
		}

		idList.setTrainDataIdList(list);
		return idList;
	}
	
	public Path getAnswerSheetAndWavFile(SttTrainAmDataVO detail) {
    	Path zipFilePath = null;
        EncUtil encUtil = new EncUtil();
    	if (detail == null) {
            log.info("알 수 없는 데이터셋입니다");
        }else if(detail.getFirstPathYn().equals("Y")) {
        	throw new IllegalArgumentException("초기 대용량데이터는 다운로드 할 수 없습니다.");
        } else {
        	
            zipFilePath = zipAnswerSheetAndWavFile(detail);
            //Files.deleteIfExists(answerSheetPath);
        }
        
        return zipFilePath;
    }
	
	private Path zipAnswerSheetAndWavFile(SttTrainAmDataVO detail) {
    	Path zipFilePath = null;
        try {
            zipFilePath = Paths.get(detail.getAmDataPath(), detail.getDatasetName()+ ".zip");
            List<Path> targetFilePaths = new ArrayList<>();
            Path answerPath = Paths.get(detail.getAmDataPath(), detail.getAnswerFileName());
            Path voicePath = Paths.get(detail.getAmDataPath(), detail.getVoiceFileName());
            log.info(">>>>>> answerPath : "+answerPath);
            log.info(">>>>>> voicePath : "+voicePath);
            targetFilePaths.add(answerPath);
            targetFilePaths.add(voicePath);

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
            //log.error("<정답지 + 음원파일>의 압축파일 생성에 실패했습니다."+detail.getAmDataPath()+"]해당 경로를 확인해주세요.");
            throw new IllegalArgumentException("<정답지 + 음원파일>의 압축파일 생성에 실패했습니다.["+detail.getAmDataPath()+"]해당 경로를 확인해주세요.");
        }
        return zipFilePath;
    }


	/**
	 * @MethodName : serviceModelResultAll
	 * @작성일 : 2024. 01. 17.
	 * @작성자 : chanmi.joo
	 * @변경이력 :
	 * @Method설명 : AM학습데이터 초기 데이터 직접 등록
	 * @param modelListNum
	 * @return
	 */
	public ArrayList<Integer> serviceModelResultAll(int modelListNum) {

		ArrayList<Integer> list = new ArrayList<>();

		for (int serviceId = 1; serviceId <= modelListNum ; serviceId++) {
			int num = sttTrainDataRepository.amDataServiceListResultAll(serviceId);
			list.add(num);
		}

		return list;
	}

	public SttTrainDataListVO getAmListByServiceModel(SttTrainDataAmSearchCondition searchCondition) {
		Page<SttTrainAmDataVO> sttTrainAmDataVOs = amDataSearch(searchCondition);
		SttTrainDataListVO idList = new SttTrainDataListVO();
		List<Long> list = new ArrayList<>();

		if (sttTrainAmDataVOs.size() != 0) {
			for (SttTrainAmDataVO sttTrainAmDataVO : sttTrainAmDataVOs) {
				list.add(sttTrainAmDataVO.getId());
			}
		}

		idList.setTrainDataIdList(list);
		return idList;
	}

	public boolean hasInvalidContents(String contents) {
		// 영문, 숫자, 특수문자 지원 안함.
		String regExp = "^[가-힣\\s]*$";
		if (!contents.matches(regExp)) {
			return true;
		}

		// 255자 이내
		if (contents.length() > 255 || contents.length() < 0) {
			return true;
		}
		return false;
	}

	public boolean hasInvalidDatasetName(String datasetName) {
		log.info("================ in hasInvalidDatasetName ===============");
		// 30자 이내
		if (datasetName.length() > 30 || datasetName.length() < 0) {
			return true;
		}

		return false;
	}

	public boolean hasInvalidDescription(String description) {
		// 255자 이내
		if (description.length() > 255 || description.length() < 0) {
			return true;
		}
		return false;
	}

	public Map<String, Object> getExcelMap(SttTrainDataSearchCondition searchCondition) {
		long listSize = sttTrainDataRepository.count(searchCondition);

		List<String> keys = Arrays.asList("NO", "DATA_TYPE", "SERVICE_MODEL", "CONTENTS", "REPEAT_COUNT", "DESCRIPTION",
				"REG_ID", "REG_DT");
		List<String> headers = Arrays.asList("No.", "데이터 구분", "서비스 모델", "데이터", "가중치", "설명", "작성자", "등록일시");

		List<String> widths = Arrays.asList("10", "20", "20", "20", "80", "50", "20", "20");

		return SxssfExcelBuilder.makeExcelDataMap(listSize, searchCondition, keys, headers, widths, null);
	}

	/**
	 * @title 대량등록. Async
	 * @author jieun.chang
	 * @param uploadKey 업로드키 (세션 키, UUID)
	 * @param reqId     작성자
	 * @param reqIp     작성IP
	 * @date 2022.05.02
	 * @see
	 * 
	 *      <pre></pre>
	 */
	@Async("taskExecutor")
	@Transactional
	public void insertBulk(String uploadKey, String reqId, String reqIp) {
		SttTrainDataBulkCache cache = SttCacheManager.getInstance().getTrainDataCache(uploadKey);
		List<ServiceModelVO> serviceModelList = serviceModelService.listAll();
		HashMap<String, Long> serviceModelMap = new HashMap<>();

		for (ServiceModelVO serviceModel : serviceModelList) {
			serviceModelMap.put(serviceModel.getServiceModelName(), Long.parseLong(serviceModel.getServiceCode()));
		}

		try {
			for (SttTrainDataBulkSaveReqDto article : cache.getInputList()) {
				cache.addCurrentCount();

				// 기본적인 유효성 검사 진행
				if (!article.isValidForInsert()) {
					article.setFailReason("요청 값이 유효하지 않습니다.");
					continue;
				}

				SttTrainDataSaveReqDto reqDto = new SttTrainDataSaveReqDto();
				reqDto.setDescription(!StringUtils.isEmpty(article.getDescription()) ? article.getDescription() : "");
				reqDto.setUploadedBy(reqId);
				reqDto.setRegIp(reqIp);

				// 데이터구분, 서비스모델 정보 찾기
				if (!TrainDataType.validEnum(article.getDataType().trim())) {
					article.setFailReason("잘못된 데이터구분 데이터입니다.");
					cache.getFailList().add(article);
					continue;
				}
				if (!serviceModelMap.containsKey(article.getServiceModel().trim())) {
					article.setFailReason("잘못된 서비스모델 데이터입니다.");
					cache.getFailList().add(article);
					continue;
				}

				// 학습데이터 유효성 체크
				if (hasInvalidContents(article.getContents().trim())) {
					article.setFailReason("학습데이터 입력조건에 맞지 않습니다(한글(자음/모음 제외)/255자 내외).");
					cache.getFailList().add(article);
					continue;
				}
				// 설명 유효성 체크
				if (!StringUtils.isEmpty(article.getDescription())) {
					if (hasInvalidDescription(article.getDescription().trim())) {
						article.setFailReason("설명 입력형식이 맞지 않습니다(255자 내외).");
						cache.getFailList().add(article);
						continue;
					}
				}

				// 가중치 유효성 체크
				final String REGEX = "[0-9]+";

				if (!article.getRepeatCount().matches(REGEX)) {
					article.setFailReason("가중치는 숫자만 입력 가능합니다.");
					cache.getFailList().add(article);
					continue;
				} else {
					if (Integer.valueOf(article.getRepeatCount()) > 100000
							|| Integer.valueOf(article.getRepeatCount()) < 1) {
						article.setFailReason("가중치 입력조건에 맞지 않습니다(1~10만).");
						cache.getFailList().add(article);
						continue;
					}
				}

				Integer trainDataId = null;
				reqDto.setDataType(TrainDataType.valueOfLabel(article.getDataType().trim()));

				reqDto.setServiceModelId(serviceModelMap.get(article.getServiceModel().trim()).intValue());
				// 학습데이터 공백 처리
				String newContents = article.getContents().trim().replaceAll("\\s+", " ");
				reqDto.setContents(newContents);

				// 가중치
				reqDto.setRepeatCount(Integer.valueOf(article.getRepeatCount()));
				// 문장 중복 체크 후 insert
				if (hasDuplicateContents(reqDto.getContents(), reqDto.getServiceModelId().toString())) {
					article.setFailReason("중복된 학습데이터가 이미 존재합니다.");
					cache.getFailList().add(article);
					continue;
				}

				try {
					trainDataId = this.insertBulk(reqDto);
					cache.getSuccessList().add(article);
				} catch (IllegalArgumentException e) {
					article.setFailReason("요청 값이 유효하지 않습니다.");
					cache.getFailList().add(article);
					continue;
				}
			}
		} catch (Exception e) {
			log.error("Error: Failed to insert train data - {}", e.getMessage());
			cache.setFinished(SttTrainDataBulkCache.ResultCode.FILE_SAVE_FAIL);
			return;
		} finally {
			cache.getInputList().clear(); // 엑셀 파일 클리어
		}

		cache.setFinished(SttTrainDataBulkCache.ResultCode.SUCCESS);
	}

	/**
	 * @title 대량등록 파일을 캐시에 저장한다
	 * @author jieun.chang
	 * @param uploadKey 업로드키 (UUID)
	 * @param excelFile excel file
	 * @return stt train data bulk cache
	 * @date 2022.05.02
	 * @see
	 * 
	 *      <pre></pre>
	 */
	public SttTrainDataBulkCache saveBulkInsertFilesToCache(String uploadKey, MultipartFile excelFile) {
		SttCacheManager.getInstance().initializeTrainDataCache(uploadKey);

		// 세션에 키&업로드 정보 세팅
		SttTrainDataBulkCache uploadResult = new SttTrainDataBulkCache();
		SttCacheManager.getInstance().put(uploadKey, uploadResult);

		if (ObjectUtils.isEmpty(excelFile)) {
			uploadResult.setFinished(SttTrainDataBulkCache.ResultCode.NO_FILE_REQUESTED);
			return uploadResult;
		}

		List<SttTrainDataBulkSaveReqDto> inputList = new ArrayList<>();

		// 학습데이터 업로드 엑셀 확인
		inputList = convertMultipartFileToExcelList(excelFile);

		if (CollectionUtils.isEmpty(inputList)) {
			uploadResult.setFinished(SttTrainDataBulkCache.ResultCode.EMPTY_FILE);
			return uploadResult;
		}

		if (inputList.size() > 500) {
			uploadResult.setFinished(SttTrainDataBulkCache.ResultCode.TOO_LARGE_FILE);
			return uploadResult;
		}

		uploadResult.setInputList(inputList);
		return uploadResult;
	}

	private List<SttTrainDataBulkSaveReqDto> convertMultipartFileToExcelList(MultipartFile excelFile) {
		Workbook workbook = ExcelFileType.getWorkbook(excelFile);

		ExcelReadOption option = ExcelReadOption.builder().startRow(1)
				.outputColumns(SttTrainDataBulkSaveReqDto.getColumnMap()).build();

		List<SttTrainDataBulkSaveReqDto> inputList = ExcelRead.read(workbook, option, SttTrainDataBulkSaveReqDto.class);

		return inputList;
	}
}
