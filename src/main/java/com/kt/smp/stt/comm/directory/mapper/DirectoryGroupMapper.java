package com.kt.smp.stt.comm.directory.mapper;

import com.kt.smp.stt.comm.directory.dto.*;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DirectoryGroupMapper {
    int count(DirectoryGroupSearchCondition searchCondition);

    List<DirectoryGroupListDto> search(DirectoryGroupSearchCondition searchCondition);

    int countByName(String name);
    
    int countByDefaultPath(String path);

    DirectoryGroupDto findById(int id);

    void save(DirectoryGroupSaveDto newGroup);

    void delete(Integer id);

    void update(DirectoryGroupUpdateDto modifiedGroup);
}
