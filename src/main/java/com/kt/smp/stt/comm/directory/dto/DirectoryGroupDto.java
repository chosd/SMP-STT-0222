package com.kt.smp.stt.comm.directory.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class DirectoryGroupDto {

    private int id;
    private String name;
    private String description;
    private String regDt;
    private List<DirectoryListDto> directoryList;

    public void setId(int id) {
    	this.id = id;
    }
    
    public void setDirectoryList(List<DirectoryListDto> directoryList) {
        this.directoryList = directoryList;
    }
}
