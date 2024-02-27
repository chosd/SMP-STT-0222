package com.kt.smp.stt.test.mapper;

import com.kt.smp.stt.test.domain.SttTestCallbackVO;
import org.apache.ibatis.annotations.Mapper;

/**
 * The interface Stt test mapper.
 *
 * @author jaime
 * @title SttTestMapper
 * @see\n <pre> </pre>
 * @since 2022 -05-05
 */
@Mapper
public interface SttTestMapper {

    /**
     * Detail stt test callback vo.
     *
     * @param uuid the uuid
     * @return the stt test callback vo
     */
    SttTestCallbackVO detail(String uuid);

    /**
     * Insert int.
     *
     * @param sttTestCallbackVO the stt test callback vo
     * @return the int
     */
    int insert(SttTestCallbackVO sttTestCallbackVO);

    /**
     * Delete int.
     *
     * @param uuid the uuid
     * @return the int
     */
    int delete(String uuid);
}
