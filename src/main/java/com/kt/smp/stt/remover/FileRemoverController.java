/**
 * 
 */
package com.kt.smp.stt.remover;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kt.smp.config.CommonConstants;

import lombok.RequiredArgsConstructor;

/**
* @FileName : FileRemoverController.java
* @Project : stt-smp-service
* @Date : 2024. 1. 26.
* @작성자 : rapeech
* @변경이력 :
* @프로그램설명 :
*/
@RestController
@RequiredArgsConstructor
@RequestMapping("${smp.service.uri.prefix}/api")
public class FileRemoverController {
	
	private final FileRemover fileRemover;

	@PostMapping("/remove")
	public ResponseEntity<Void> remove(HttpServletRequest request) {
        String projectCode = request.getHeader(CommonConstants.X_SMP_PROJECT_CODE);
		fileRemover.run(projectCode);
		return ResponseEntity.ok(null);
	}
	
}
