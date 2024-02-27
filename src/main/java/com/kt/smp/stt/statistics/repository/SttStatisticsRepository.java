package com.kt.smp.stt.statistics.repository;

import com.github.pagehelper.Page;
import com.kt.smp.stt.statistics.domain.SttStatisticsDetailSearchConditionDto;
import com.kt.smp.stt.statistics.domain.SttStatisticsSearchCondition;
import com.kt.smp.stt.statistics.domain.SttStatisticsVO;
import com.kt.smp.stt.statistics.mapper.SttStatisticsMapper;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

@Repository
public class SttStatisticsRepository {

    private final SttStatisticsMapper mapper;

    public SttStatisticsRepository(@Qualifier("tenantSqlSession") SqlSession sqlSession) {
        this.mapper = sqlSession.getMapper(SttStatisticsMapper.class);
    }

    public SttStatisticsVO detail(SttStatisticsDetailSearchConditionDto searchCondition) {
        return mapper.detail(searchCondition);
    }

    public Page<SttStatisticsVO> list(SttStatisticsSearchCondition searchCondition) {
        return mapper.list(searchCondition);
    }

    public Page<SttStatisticsVO> listBusyCallCount(SttStatisticsSearchCondition searchCondition) {
        return mapper.listBusyCallCount(searchCondition);
    }

    public long count(SttStatisticsSearchCondition searchCondition) {
        return mapper.count(searchCondition);
    }

    public int exists(SttStatisticsDetailSearchConditionDto searchCondition) {
        return mapper.exists(searchCondition);
    }

    public int insert(SttStatisticsVO sttStatisticsVO) {
        return mapper.insert(sttStatisticsVO);
    }

	public String getMostNumerousServiceCode(SttStatisticsSearchCondition searchCondition) {
		return mapper.getMostNumerousServiceCode(searchCondition);
	}

	public int existsServiceCode(SttStatisticsSearchCondition searchCondition) {
		return mapper.existsServiceCode(searchCondition);
	}

	public List<SttStatisticsVO> getListAll(SttStatisticsSearchCondition searchCondition) {
		return mapper.getListAll(searchCondition);
	}

	public List<SttStatisticsVO> getListAllByMinute(SttStatisticsSearchCondition searchCondition) {
		return mapper.getListAllByMinute(searchCondition);
	}

}
