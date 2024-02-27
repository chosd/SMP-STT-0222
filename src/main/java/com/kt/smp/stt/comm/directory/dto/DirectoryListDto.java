package com.kt.smp.stt.comm.directory.dto;

import lombok.Getter;

@Getter
public class DirectoryListDto {

    private Integer id;
    private String name;
    private String homePath;
    private String path;
    private Integer priority;
    private String inUse;
    private String regDt;
}
