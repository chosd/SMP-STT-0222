package com.kt.smp.stt.common;

import com.kt.smp.multitenancy.config.TenantContextHolder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Paths;

@Component
public class CallbackUrlResolver {

    @Value("${smp.service.endpoint}")
    private String endpoint;

    @Value("${smp.service.uri.prefix}")
    private String prefix;


    public String trainCallbackUrl() {
        return generate("train/api/callback");
    }

    public String deployCallbackUrl() {
        return generate("deploy/api/callback");
    }

    public String testCallbackUrl(String uuid) {
        return generate("sttTest/api/callback/" + uuid);
    }

    public String verifyCallbackUrl() {
        return generate("api/verify/callback");
    }
    
    public String semiRealtimeCallbackUrl() {
    	return generate("api/reprocess/callback");
    }

    private String generate(String path) {
    	URI absolute = null;
        String projectCode = TenantContextHolder.getProjectCode();
        
		try {
			URI base = new URI(endpoint+prefix+"/");
			URI param = new URI(path + "?projectCode=" + projectCode);
			absolute = base.resolve(param);
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
        
        //return Paths.get(endpoint, prefix, path) + "?projectCode=" + projectCode;
		return absolute.toString(); // Paths.get 함수에서의 URI 경로가 잘못세팅되어서 URI세팅으로 변경 
    }
}
