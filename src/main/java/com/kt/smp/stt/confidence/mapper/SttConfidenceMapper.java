package com.kt.smp.stt.confidence.mapper;

import com.kt.smp.stt.confidence.domain.SttConfidenceSearchCondition;
import com.kt.smp.stt.confidence.domain.SttConfidenceVO;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SttConfidenceMapper {

	public List<SttConfidenceVO> confidenceChartData(SttConfidenceSearchCondition searchCondition);
}
