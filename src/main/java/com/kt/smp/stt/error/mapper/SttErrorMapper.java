package com.kt.smp.stt.error.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import com.github.pagehelper.Page;
import com.kt.smp.stt.error.dto.SttErrorInsertDto;
import com.kt.smp.stt.error.dto.SttErrorListDto;
import com.kt.smp.stt.error.dto.SttErrorSearchDto;
import com.kt.smp.stt.error.dto.SttErrorTypeDto;

@Mapper
public interface SttErrorMapper {

    public int insert(List<SttErrorInsertDto> dto);
    
    public List<SttErrorTypeDto> selectType();
    
    public Page<SttErrorListDto> list(SttErrorSearchDto sttErrorSearchDto);
}
