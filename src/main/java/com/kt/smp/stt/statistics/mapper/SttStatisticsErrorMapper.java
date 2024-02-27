package com.kt.smp.stt.statistics.mapper;

import com.github.pagehelper.Page;
import com.kt.smp.stt.statistics.domain.SttStatisticsErrorVO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SttStatisticsErrorMapper {

    /**
     * List page.
     *
     * @return the page
     */
    Page<SttStatisticsErrorVO> list();

    /**
     * Count int.
     *
     * @return the int
     */
    int count();

    /**
     * Insert int.
     *
     * @param sttStatisticsErrorVO the stt statistics error vo
     * @return the int
     */
    int insert(SttStatisticsErrorVO sttStatisticsErrorVO);
}
