package com.kt.smp.stt.test.service;

import com.kt.smp.stt.test.domain.SttTestCallbackVO;
import com.kt.smp.stt.test.domain.SttTestResult;
import com.kt.smp.stt.test.repository.SttTestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author jaime
 * @title SttTestService
 * @see\n <pre>
 * </pre>
 * @since 2022-05-05
 */
@Service
@RequiredArgsConstructor
public class SttTestService {

    private final SttTestRepository sttTestRepository;

    public SttTestCallbackVO detail(String uuid) {
        return sttTestRepository.detail(uuid);
    }

    @Transactional
    public int insert(SttTestCallbackVO sttTestCallbackVO) {    	
        return sttTestRepository.insert(sttTestCallbackVO);
    }

    @Transactional
    public int delete(String uuid) {
        return sttTestRepository.delete(uuid);
    }
}
