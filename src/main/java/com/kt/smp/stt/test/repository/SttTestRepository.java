package com.kt.smp.stt.test.repository;

import com.kt.smp.stt.test.domain.SttTestCallbackVO;
import com.kt.smp.stt.test.mapper.SttTestMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

/**
 * The interface Stt test mapper.
 *
 * @author jaime
 * @title SttTestMapper
 * @see\n <pre> </pre>
 * @since 2022 -05-05
 */
@Repository
public class SttTestRepository {

    private final SttTestMapper mapper;

    public SttTestRepository(@Qualifier("tenantSqlSession") SqlSession sqlSession) {
        this.mapper = sqlSession.getMapper(SttTestMapper.class);
    }

    public SttTestCallbackVO detail(String uuid) {
        return mapper.detail(uuid);
    }

    public int insert(SttTestCallbackVO sttTestCallbackVO) {
        return mapper.insert(sttTestCallbackVO);
    }

    public int delete(String uuid) {
        return mapper.delete(uuid);
    }
}
