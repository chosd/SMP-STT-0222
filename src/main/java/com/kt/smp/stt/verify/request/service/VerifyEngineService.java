package com.kt.smp.stt.verify.request.service;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.kt.smp.common.util.ExternalApiRequester;
import com.kt.smp.common.util.JacksonUtil;
import com.kt.smp.common.util.crypto.TextCrypto;
import com.kt.smp.stt.comm.directory.service.DirectoryService;
import com.kt.smp.stt.comm.serviceModel.domain.ServiceModelVO;
import com.kt.smp.stt.comm.serviceModel.service.ServiceModelService;
import com.kt.smp.stt.common.CallbackUrlResolver;
import com.kt.smp.stt.common.dto.BaseResultDto;
import com.kt.smp.stt.verify.data.dto.VerifyDataDto;
import com.kt.smp.stt.verify.data.repository.VerifyDataRepository;
import com.kt.smp.stt.verify.dataset.dto.VerifyDatasetDto;
import com.kt.smp.stt.verify.dataset.repository.VerifyDatasetRepository;
import com.kt.smp.stt.verify.dataset.service.VerifyDatasetService;
import com.kt.smp.stt.verify.request.dto.EngineVerifyRequestDto;
import com.kt.smp.stt.verify.request.dto.VerifyMultipartRequestDto;
import com.kt.smp.stt.verify.request.dto.VerifyRequestDto;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.zip.GZIPOutputStream;

@Slf4j
@Service
@RequiredArgsConstructor
public class VerifyEngineService {

	private static final String VERIFY_PATH = "/stt/verify";
	private static final String VERIFY_MULTIPART_PATH = "/stt/verify/multipart";
	
    private final ServiceModelService serviceModelService;
    private final VerifyDatasetService verifyDatasetService;
    private final CallbackUrlResolver callbackUrlResolver;
    private final VerifyDatasetRepository datasetRepository;
    private final DirectoryService directoryService;
    private final VerifyDataRepository dataRepository;
    private final ExternalApiRequester externalRequestUtil;
    private final TextCrypto textCrypto;
    
    @Value("${directory.home}")
    private String directoryHome;
    
    @Value("${spring.profiles.active}")
    private String profile;

    /**
    *@MethodName : requestVerify
    *@작성일 : 2023. 11. 2.
    *@작성자 : wonyoung.ahn
    *@변경이력 :
    *@Method설명 :
    *@param verifyRequest
    */
    public BaseResultDto requestVerify(VerifyRequestDto verifyRequest) {

        EngineVerifyRequestDto engineVerifyRequest = makeEngineVerifyRequest(verifyRequest);
        if(engineVerifyRequest != null) {
            log.info("[VerifyEngineService.requestVerify] request body >>> {}", JacksonUtil.objectToJsonWithPrettyPrint(engineVerifyRequest));
            
            BaseResultDto resultDto = externalRequestUtil.requestPostWithSavingError(VERIFY_PATH, engineVerifyRequest);
            
            return resultDto;
        }
        return null;
    }
    
    public BaseResultDto requestMultipartVerify(VerifyRequestDto verifyRequest) {
    	String directory = getSaveDirectory(verifyRequest);
        
        StringBuffer stringBuffer = new StringBuffer(directory);
    	stringBuffer.append(File.separator);
    	stringBuffer.append(verifyRequest.getDatasetId());
        
        HttpHeaders headers = new HttpHeaders();
    	headers.setContentType(MediaType.MULTIPART_FORM_DATA);
    	
    	VerifyMultipartRequestDto requestDto = VerifyMultipartRequestDto.builder()
    			.serviceCode(serviceModelService.detail(verifyRequest.getServiceModelId()).getServiceCode())
    			.callbackUrl(callbackUrlResolver.trainCallbackUrl())
    			.build();
    	
    	File file = new File(directory+File.separator+verifyRequest.getDatasetId()+".tar.gz");
    	FileSystemResource fileResource = new FileSystemResource(file);
    	
    	MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
    	body.add("verifyInfoData", requestDto);
    	body.add("verifyData", fileResource);
        
    	HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
    	
         
        log.info("[VerifyEngineService.requestVerify] request body >>> {}", JacksonUtil.objectToJsonWithPrettyPrint(requestEntity));
         
        BaseResultDto resultDto = externalRequestUtil.requestPostWithSavingError(VERIFY_MULTIPART_PATH, requestEntity);
         
        return resultDto;
    }
    
    // tar.gz 파일 압축
    public void tarGzCompression(VerifyRequestDto verifyRequest) throws Exception {
    	String directory = getSaveDirectory(verifyRequest);
    	StringBuffer stringBuffer = new StringBuffer(directory);
    	stringBuffer.append(File.separator);
    	stringBuffer.append(verifyRequest.getDatasetId());

        File tarOutputFile = new File(directory, verifyRequest.getDatasetId()+".tar.gz");
        log.info("Path >>> {}",tarOutputFile.getPath());
        try (FileOutputStream fos = new FileOutputStream(tarOutputFile);
        		BufferedOutputStream bos = new BufferedOutputStream(fos);
        		GZIPOutputStream gzipOS = new GZIPOutputStream(bos);
        		ArchiveOutputStream aos = new ArchiveStreamFactory().createArchiveOutputStream(ArchiveStreamFactory.TAR, gzipOS);){
            File folder = new File(directory);
            addFolderToTarGz(folder, aos, "");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    
    private static void addFolderToTarGz(File folder, ArchiveOutputStream archiveOutputStream, String base) throws IOException {
        File[] files = folder.listFiles();

        if (files != null) {
            for (File file : files) {
                String entryName = base + file.getName();
                ArchiveEntry entry = archiveOutputStream.createArchiveEntry(file, entryName);
                archiveOutputStream.putArchiveEntry(entry);

                if (file.isFile()) {
                    try (FileInputStream fis = new FileInputStream(file);
                         BufferedInputStream bis = new BufferedInputStream(fis)) {
                        byte[] buffer = new byte[1024];
                        int read;
                        while ((read = bis.read(buffer)) != -1) {
                            archiveOutputStream.write(buffer, 0, read);
                        }
                        archiveOutputStream.closeArchiveEntry();
                    }
                } else if (file.isDirectory()) {
                    archiveOutputStream.closeArchiveEntry();
                    addFolderToTarGz(file, archiveOutputStream, entryName + "/");
                }
            }
        }
    }

    private EngineVerifyRequestDto makeEngineVerifyRequest(VerifyRequestDto verifyRequest) {

    	ServiceModelVO serviceModel = serviceModelService.detailS(verifyRequest.getServiceModelId() + "");
        //VerifyDatasetDto dataset = verifyDatasetService.get(verifyRequest.getDatasetId());//findByDatasetId
        List<VerifyDataDto> resultData = dataRepository.findByDatasetNameId(verifyRequest.getDatasetName(), verifyRequest.getDatasetId());
        
        // 검증데이터셋 빠지면서 아래로직 변경필요
        //List<VerifyDataDto> resultAnsFileName = dataRepository.findByDatasetId(verifyRequest.getDatasetId());
        List<VerifyDataDto> resultAnsFileName = dataRepository.findByDatasetName(verifyRequest.getDatasetName());
        
        String wavPath = "";
        String answerPath = "";
        String callbackUrl = "";

        if (resultAnsFileName.isEmpty()) {
        	log.error("해당 검증데이터셋에 검증데이터가 없습니다.");
        	return null;
        } else {
        	
//          dataset.setDirectoryPath(dataset.getDirectoryPath()+File.separator+verifyRequest.getDeployId());
        	String answerSheetPath = createTempAnswerSheet(resultData.get(0), resultAnsFileName);
        	log.info(">>>> answerSheetPath : "+answerSheetPath);
        	callbackUrl = callbackUrlResolver.verifyCallbackUrl();
          
//          dataset.setDirectoryHomePath("C:\\2nas_home\\stt\\test-project-code"); //로컬테스트를 위한 하드코딩
//          dataset.setDirectoryPath("\\verifyData\\callbot"); //로컬테스트를 위한 하드코딩
          
//  		log.info(">>>> before return wav path : /home/aicc/nas/verifiData/1208/test/wav");
//          log.info(">>>> before return answer path : /home/aicc/nas/verifiData/1208/test/answer.answer");
//          return new EngineVerifyRequestDto(
//                  serviceModel.getServiceCode(),
//                  "/home/aicc/nas/verifiData/1208/test/wav",
//                  "/home/aicc/nas/verifiData/1208/test/answer.answer",
//                  callbackUrl);
        	
	          log.info(">>>> before return wav path : "+resultData.get(0).getVerifyDataPath() + File.separator + "wav");
	          log.info(">>>> before return answer path : "+resultData.get(0).getVerifyDataPath() + File.separator + resultAnsFileName.get(0).getAnswerFileName());
	          	
	          wavPath = resultData.get(0).getVerifyDataPath() + File.separator + "wav";
//          	answerPath = dataset.getDirectoryPath() + File.separator + resultAnsFileName.get(0).getAnswerFileName();
          	
	          return new EngineVerifyRequestDto(
                  serviceModel.getServiceCode(),
                  wavPath,
                  answerSheetPath,
                  callbackUrl);
        }
    }

    private String createTempAnswerSheet(VerifyDataDto resultData, List<VerifyDataDto> dataList) {
    	Date today = new Date();
    	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
    	
        Path answerSheetPath = Paths.get(resultData.getVerifyDataPath(), "answer_"+dateFormat.format(today)+".answer");
//        List<VerifyDataDto> dataList = verifyDataService.getByDataset(dataset);

        try(BufferedWriter writer = new BufferedWriter(new FileWriter(answerSheetPath.toFile()))) {
            for (VerifyDataDto data : dataList) {
                //writer.write(data.getWavFileName() + "\t" + data.getDictatedText() + "\n");
                //24.03.05 CSD 검증데이터 복호화
                writer.write(data.getWavFileName() + "\t" + textCrypto.decrypt(data.getDictatedText()) + "\n");
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            log.error("정답지 파일을 생성할 수 없습니다(원인: " + ex.getMessage() + ")");
        }

        return answerSheetPath.toString();
    }

    //파일경로 select
    private String getSaveDirectory(VerifyRequestDto newData) {

        VerifyDatasetDto dataset = datasetRepository.findById(newData.getDatasetId());
        return directoryService.getAbsolutePath(dataset.getDirectoryId());
    }
}
