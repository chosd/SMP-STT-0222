package com.kt.smp.stt.comm.directory.repository;

import com.kt.smp.stt.comm.directory.dto.DirectoryDto;
import com.kt.smp.stt.comm.directory.dto.DirectoryListDto;
import com.kt.smp.stt.comm.directory.dto.DirectorySaveDto;
import com.kt.smp.stt.comm.directory.mapper.DirectoryMapper;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public class DirectoryRepository {

    private final DirectoryMapper mapper;

    public DirectoryRepository(@Qualifier("tenantSqlSession") SqlSession sqlSession) {
        this.mapper = sqlSession.getMapper(DirectoryMapper.class);
    }

    public List<DirectoryListDto> findAll() {
        return mapper.findAll();
    }
    public List<DirectoryListDto> findTrainList() {
        return mapper.findTrainList();
    }
    public DirectoryDto findById(int id) {
        return mapper.findById(id);
    }
    public List<DirectoryListDto> findByGroupId(int groupId) {
        return mapper.findByGroupId(groupId);
    }

    public DirectoryDto findByGroupIdAndPriority(int groupId, int priority) {
        return mapper.findByGroupIdAndPriority(groupId, priority);
    }

    public DirectoryDto findByName(String name) {
        return mapper.findByName(name);
    }

    public int countByGroupIdAndName(int groupId, String name) {
        return mapper.countByGroupIdAndName(groupId, name);
    }

    public int countByPath(String path) {
        return mapper.countByPath(path);
    }

    public void save(DirectorySaveDto newDirectory) {
        mapper.save(newDirectory);
    }

    public void updateInUse(int id, String inUse) {
        mapper.updateInUse(id, inUse);
    }

    public void updateNameAndPath(int id, String name, String path) {
        mapper.updateNameAndPath(id, name, path);
    }

    public void updatePriority(int id, int priority) {
        mapper.updatePriority(id, priority);
    }

    public void delete(Integer id) {
        mapper.delete(id);
    }

    public int countByGroupIdAndExcludedDirIdAndName(int groupId, int excludedDirId, String name) {
        return mapper.countByGroupIdAndExcludedDirIdAndName(groupId, excludedDirId, name);
    }

    public int countByExcludedDirIdAndPath(int excludedDirId, String path) {
        return mapper.countByExcludedDirIdAndPath(excludedDirId, path);
    }
}
