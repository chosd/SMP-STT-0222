package com.kt.smp.stt.statistics.service;

import com.github.pagehelper.Page;

import com.kt.smp.stt.statistics.domain.SttStatisticsErrorVO;
import com.kt.smp.stt.statistics.repository.SttStatisticsErrorRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author jaime
 * @title SttStatisticsErrorService
 * @see\n <pre>
 * </pre>
 * @since 2022-07-18
 */
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SttStatisticsErrorService {

    private final SttStatisticsErrorRepository sttStatisticsErrorRepository;

    public Page<SttStatisticsErrorVO> list() {
        return sttStatisticsErrorRepository.list();
    }

    public int count() {
        return sttStatisticsErrorRepository.count();
    }

    @Transactional
    public int insert(SttStatisticsErrorVO sttStatisticsErrorVO) {
        return sttStatisticsErrorRepository.insert(sttStatisticsErrorVO);
    }
}
