package com.kt.smp.common.service;

import com.kt.smp.common.domain.BaseModel;
import com.kt.smp.common.repository.BaseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class BaseService {

	private final BaseRepository baseRepository;

	public List<BaseModel> empList() {
		return baseRepository.empList();
	}

}
