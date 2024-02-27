package com.kt.smp.stt.recognition.mapper;

import com.kt.smp.stt.recognition.dto.SttRecognitionDetailVO;
import com.kt.smp.stt.recognition.dto.SttRecognitionSearchCondition;
import com.kt.smp.stt.recognition.dto.SttRecognitionVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SttRecognitionMapper {

    public List<SttRecognitionVO> recognitionChartData(SttRecognitionSearchCondition searchCondition);

    public List<SttRecognitionDetailVO> getRecognitionDetail(SttRecognitionSearchCondition searchParamDto);
    
}
