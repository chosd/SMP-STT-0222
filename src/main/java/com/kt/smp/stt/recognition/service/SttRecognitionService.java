package com.kt.smp.stt.recognition.service;

import com.kt.smp.stt.confidence.dto.SttRecognitionSearchResponseDto;
import com.kt.smp.stt.recognition.dto.SttRecognitionSearchCondition;
import com.kt.smp.stt.recognition.dto.SttRecognitionVO;

import java.util.List;

public interface SttRecognitionService {
    SttRecognitionSearchResponseDto recognitionChartData(SttRecognitionSearchCondition searchCondition);
}
