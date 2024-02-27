package com.kt.smp.stt.deploy.deploy.domain;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.kt.smp.common.domain.BaseModel;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

/**
 * @title STT 배포 이력 관리 VO
 * @since 2022.02.18
 * @author kyungtae
 * @see <pre></pre>
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class SttDeployMngVO extends BaseModel {
	
	// 설정파일에 기재되어 있는 배포서버 2대
	private String deployTarget;
	private String deployTargetSub;
	// 사용쟈가 선택한 배포서버
	private String deployTargetServer;
	
    // 서비스 모델
    private Long serviceModelId;

    // 결과모델 ID
    private String resultModelId;

    // 결과모델 설명
    private String resultModelDescription;
    
    // 결과모델 설명
    private String resultModelModeltype;

    // 설명
    private String description;

    // 작성자
    private String createdBy;

    // 요청 시간
//    private LocalDateTime requestedAt;
    private String requestedAt;

    // 완료 시간
//    private LocalDateTime completedAt;
    private String completedAt;

    // 소요시간
    private Long duration;

    // STT_API_연동규격서 결과코드 3.3 정의에 따름
    private String resultCode;

    /**
     * 배포 결과
     * 성공: "success"
     * 실패: 배포 실패 원인 메시지
     */
    private String resultMsg;
    
    // 배포 상태
    private String status;
    private String modelType;
    
    private String deployUrl;
    
    // serviceCode를 제공하는 SRU 서버들의 배포 상태
    private List<SttDeployStatus> deployList = new ArrayList<>();

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
