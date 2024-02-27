package com.kt.smp.stt.comm.directory.repository;

import com.kt.smp.stt.comm.directory.dto.*;
import com.kt.smp.stt.comm.directory.mapper.DirectoryGroupMapper;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class DirectoryGroupRepository {

    private final DirectoryGroupMapper mapper;

    public DirectoryGroupRepository(@Qualifier("tenantSqlSession") SqlSession sqlSession) {

        this.mapper = sqlSession.getMapper(DirectoryGroupMapper.class);
    }

    public int count(DirectoryGroupSearchCondition searchCondition) {
        return mapper.count(searchCondition);
    }

    public List<DirectoryGroupListDto> search(DirectoryGroupSearchCondition searchCondition) {
        return mapper.search(searchCondition);
    }

    public int countByName(String name) {
        return mapper.countByName(name);
    }
    
    public int countByDefaultPath(String path) {
        return mapper.countByDefaultPath(path);
    }

    public DirectoryGroupDto findById(int id) {
        return mapper.findById(id);
    }

    public void save(DirectoryGroupSaveDto newGroup) {
        mapper.save(newGroup);
    }

    public void delete(Integer id) {
        mapper.delete(id);
    }

    public void update(DirectoryGroupUpdateDto modifiedGroup) {
        mapper.update(modifiedGroup);
    }
}
