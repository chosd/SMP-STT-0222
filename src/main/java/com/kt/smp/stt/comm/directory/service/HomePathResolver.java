package com.kt.smp.stt.comm.directory.service;

import com.kt.smp.multitenancy.dto.ConfigDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.nio.file.Paths;

@Component
@RequiredArgsConstructor
public class HomePathResolver {

    @Value("${directory.home}")
    private String directoryHome;

    public String resolve(ConfigDto config) {

        Path homePath = Paths.get(directoryHome, config.getProjectCode());
        return homePath.toString();
    }
    
    public String resolveSuhyup() {
    	Path homePath = Paths.get(directoryHome);
    	return homePath.toString();
    }
}
