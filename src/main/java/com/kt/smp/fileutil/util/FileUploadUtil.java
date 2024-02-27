package com.kt.smp.fileutil.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.kt.smp.config.CommonConstants;
import com.kt.smp.fileutil.dto.UploadFileResponseDto;

import lombok.extern.slf4j.Slf4j;

/**
 * @author jaime
 * @title FileUploadUtil
 * @see\n <pre>
 * </pre>
 * @since 2022-10-04
 */
@Slf4j
public class FileUploadUtil {
	

    public static UploadFileResponseDto uploadFile(MultipartFile file, String filePath) {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            File newFile = new File(filePath + fileName);
            
            String newFilePath = newFile.getParentFile().getAbsolutePath();
            log.info(">>> newFilePath : " + newFilePath);
            File directory = new File(newFilePath);
            newFile.setReadable(true, false);
            newFile.setWritable(true, false);
            directory.setReadable(true, false);
            directory.setWritable(true, false);
            directory.mkdirs();
            file.transferTo(newFile);

            return UploadFileResponseDto.builder()
                    .uploadSuccess(true)
                    .fileName(fileName)
                    .filePath(filePath + fileName)
                    .fileType(file.getContentType())
                    .size(file.getSize())
                    .build();
        } catch (Exception e) {
            log.error("[FileUploadUtil.uploadFile] ERROR {}", e.getMessage());

            return UploadFileResponseDto.builder()
                    .uploadSuccess(false)
                    .build();
        }
    }
    
    
    public static UploadFileResponseDto uploadFileDeploy(MultipartFile file, String filePath, String profile) {
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        log.info(">>>>>> origin deploy fileName : "+fileName+" / profile : "+profile);
        String tarFile = getTarFile(file, filePath); // 배포모델을 등록할 때는 zip파일로 업로드하는게 맞고 배포 요청할때는 zip안에 tar파일로 요청해야 함
        log.info(">>>>> getTarFile :  "+tarFile);
        if(tarFile.equals("EMPTY")) {
        	tarFile = fileName;
        }
        
        try {
            File newFile = new File(filePath + fileName);
            
            String newFilePath = newFile.getParentFile().getAbsolutePath();
            log.info(">>> newFilePath : " + newFilePath);
            File directory = new File(newFilePath);
            newFile.setReadable(true, false);
            newFile.setWritable(true, false);
            directory.setReadable(true, false);
            directory.setWritable(true, false);
            directory.mkdirs();
            file.transferTo(newFile);

            return UploadFileResponseDto.builder()
                    .uploadSuccess(true)
                    .fileName(fileName)
                    .filePath(filePath+tarFile)
                    .fileType(file.getContentType())
                    .size(file.getSize())
                    .build();
        } catch (Exception e) {
            log.error("[FileUploadUtil.uploadFile] ERROR {}", e.getMessage());

            return UploadFileResponseDto.builder()
                    .uploadSuccess(false)
                    .build();
        }
    }
    
    private static String getTarFile(MultipartFile file, String filePath) {
    	String finalDeployFileName ="";    	
    	try (ZipInputStream zis = new ZipInputStream(file.getInputStream())) {
    		ZipEntry entry = zis.getNextEntry();
	      
    		while (entry != null) {
	        
	          //Path filePath = Paths.get(directory, entry.getName());
	          // 기존소스 Files.copy(zis, filePath, StandardCopyOption.REPLACE_EXISTING);
	    	  String unzipFileName = entry.getName();
	    	  if( unzipFileName.indexOf("tar") > 0 ) {
	    		  finalDeployFileName = unzipFileName;
	    		  
	    		  /* 2023.11.05 add begin */
	    		  int len = 0;
	    		  byte[] buffer = new byte[1024];  
	    		  File newFile = new File(filePath + File.separator + unzipFileName);
	    		  try (FileOutputStream fos = new FileOutputStream(newFile);){
					  log.info(">>> Unzipping to "+newFile.getAbsolutePath());
					  //create directories for sub directories in zip
//					  new File(newFile.getParent()).mkdirs();
					  while ((len = zis.read(buffer)) > 0) {
						  fos.write(buffer, 0, len);
					  }
					  fos.close();
	    		  } catch(Exception e) {
	    			  e.printStackTrace();
	    		  }
	    		  /* 2023.11.05 add end */
				  
	    	  }else {
	    		  finalDeployFileName = "EMPTY";
	    	  }
	      
	          entry = zis.getNextEntry();
	      }
	      
	    } catch (IOException ex) {
	      ex.printStackTrace();
	      throw new IllegalArgumentException("배포모델 파일을 읽을 수 없습니다");
	    }
    	
    	return finalDeployFileName;
    }
    
}
