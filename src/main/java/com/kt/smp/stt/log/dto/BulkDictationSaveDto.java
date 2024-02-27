package com.kt.smp.stt.log.dto;

import lombok.Getter;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Getter
public class BulkDictationSaveDto {

    private List<String> callKeyList;
    private String regId;
    private String regIp;

    public void audit(HttpServletRequest request) {

        String username = request.getUserPrincipal() == null ? "" : request.getUserPrincipal().getName();
        regId = username;
        regIp = request.getRemoteAddr();
    }
}
