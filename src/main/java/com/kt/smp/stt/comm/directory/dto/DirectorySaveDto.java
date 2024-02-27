package com.kt.smp.stt.comm.directory.dto;

import com.kt.smp.multitenancy.dto.MasterSmpRequestHeaderDto;
import lombok.Getter;

import javax.servlet.http.HttpServletRequest;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;

@Getter
public class DirectorySaveDto {

    private String name;
    private String homePath;
    private String path;
    private int priority;
    private String inUse;
    private int groupId;
    private String regId;
    private String regIp;
    private String updId;
    private String updIp;

    public void audit(HttpServletRequest request) {

        MasterSmpRequestHeaderDto header = MasterSmpRequestHeaderDto.generate(request);
        regId = header.getUserId();
        regIp = request.getRemoteAddr();
        updId = header.getUserId();
        updIp = request.getRemoteAddr();
    }

    public void init(String homePath, DirectoryGroupDto group, int priority) {

        this.homePath = homePath;
        this.groupId = group.getId();
        this.priority = priority;
        this.inUse = "Y";

        try {
            //this.path = Paths.get("/", path).toString(); 기존 소스, 윈도우기준 경로가 실제값이랑 다르게 저장됨
            this.path = Paths.get(path).toString();
        } catch (InvalidPathException ex) {
            throw new IllegalArgumentException("유효하지 않은 경로입니다.");
        }
    }

    public Path getAbsolutePath() {
        return Paths.get(homePath, path);
    }
}
