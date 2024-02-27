package com.kt.smp.stt.statistics.mapper;

import com.github.pagehelper.Page;
import com.kt.smp.stt.statistics.domain.SttStatisticsDetailSearchConditionDto;
import com.kt.smp.stt.statistics.domain.SttStatisticsSearchCondition;
import com.kt.smp.stt.statistics.domain.SttStatisticsVO;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SttStatisticsMapper {

    /**
     * Detail stt statistics vo.
     *
     * @param searchCondition the search condition
     * @return the stt statistics vo
     */
    SttStatisticsVO detail(SttStatisticsDetailSearchConditionDto searchCondition);

    /**
     * List page.
     *
     * @param searchCondition the search condition
     * @return the page
     */
    Page<SttStatisticsVO> list(SttStatisticsSearchCondition searchCondition);

    /**
     * List busy call count page.
     *
     * @param searchCondition the search condition
     * @return the page
     */
    Page<SttStatisticsVO> listBusyCallCount(SttStatisticsSearchCondition searchCondition);

    /**
     * Count int.
     *
     * @param searchCondition the search condition
     * @return the int
     */
    long count(SttStatisticsSearchCondition searchCondition);

    /**
     * Exists boolean.
     *
     * @param searchCondition the search condition
     * @return the boolean
     */
    int exists(SttStatisticsDetailSearchConditionDto searchCondition);

    /**
     * Insert int.
     *
     * @param sttStatisticsVO the stt statistics vo
     * @return the int
     */
    int insert(SttStatisticsVO sttStatisticsVO);

	String getMostNumerousServiceCode(SttStatisticsSearchCondition searchCondition);

	int existsServiceCode(SttStatisticsSearchCondition searchCondition);

	List<SttStatisticsVO> getListAll(SttStatisticsSearchCondition searchCondition);

	List<SttStatisticsVO> getListAllByMinute(SttStatisticsSearchCondition searchCondition);

}
