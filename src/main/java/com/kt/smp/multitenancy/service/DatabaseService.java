
package com.kt.smp.multitenancy.service;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import com.kt.smp.multitenancy.dto.ConfigDto;
import com.kt.smp.multitenancy.dto.ConfigSaveDto;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DatabaseService {

    @Value("${spring.datasource.tenant.auto-ddl}")
    private boolean autoDdl;

    @Value("${pagehelper.helper-dialect}")
    private String dbname;

    public void create(ConfigSaveDto config) {

        if (!autoDdl) {
            return;
        }
        
        // mariadb, mysql 은 DB 생성도 해준다.
        if(isMysqlOrMariadb()) {
        	createDatabase(config);
        }
        createObjects(config);
    }

    public void update(ConfigDto originConfig, ConfigSaveDto newConfig) {

        if (!autoDdl) {
            return;
        }

        if (hasSameUrl(originConfig, newConfig)) {
            return;
        }

        createDatabase(newConfig);
        createObjects(newConfig);
    }

    private boolean hasSameUrl(ConfigDto originConfig, ConfigSaveDto newConfig) {
        return originConfig.getDbUrl().equals(newConfig.getDbUrl());
    }

    private void createDatabase(ConfigSaveDto config) {

        String hostUrl = extractHostUrl(config.getDbUrl());
        String username = config.getDbUsername();
        String password = config.getDbPassword();

        try(Connection conn = DriverManager.getConnection(hostUrl, username, password);

            Statement stmt = conn.createStatement()) {
            String dbName = extractDbName(config.getDbUrl());

            if (isExistDatabase(stmt, dbName)) {
                throw new IllegalArgumentException("데이터베이스를 생성할 수 없습니다(원인: 같은 이름의 데이터베이스가 존재합니다)");
            }

            String query = "CREATE DATABASE " + dbName;
            stmt.execute(query);

        } catch (SQLException ex) {
            ex.printStackTrace();
            throw new IllegalArgumentException("데이터베이스를 생성할 수 없습니다(원인: " + ex.getMessage() + ")");
        }
    }

    private boolean isExistDatabase(Statement stmt, String dbName) {
    	boolean checkExist = false;
        String checkQuery = "SELECT COUNT(*) FROM INFORMATION_SCHEMA.SCHEMATA WHERE SCHEMA_NAME ='" + dbName + "'";
        	
			try (ResultSet rs = stmt.executeQuery(checkQuery)){
				if (rs.next()) {
	            	checkExist = rs.getInt(1) > 0;
	            }	
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        return checkExist;
    }

    private void createObjects(ConfigSaveDto config) {

        String url = config.getDbUrl();
        String username = config.getDbUsername();
        String password = config.getDbPassword();

        Resource ddlResource = new ClassPathResource("ddl/table_ddl_" +dbname+ ".sql");
        try(Connection conn = DriverManager.getConnection(url, username, password);
            Statement smt = conn.createStatement();
            InputStreamReader reader = new InputStreamReader(ddlResource.getInputStream(), StandardCharsets.UTF_8)) {

            String ddlQuery = FileCopyUtils.copyToString(reader);
            log.info(">>> ddlQuery : " + ddlQuery);
            smt.execute(ddlQuery);

        } catch (SQLException | IOException ex) {
            ex.printStackTrace();
            throw new IllegalArgumentException("테이블을 생성할 수 없습니다(원인: " + ex.getMessage() + ")");
        }

    }

    private String extractDbName(String url) {

        String[] split = url.split("://");
        String tempUrl = "protocol://" + split[1];
        URI uri = URI.create(tempUrl);
        String path = uri.getPath();
        return path.replace("/", "");
    }

    private String extractHostUrl(String url) {

        String[] split = url.split("://");
        String tempUrl = "protocol://" + split[1];
        URI uri = URI.create(tempUrl);
        return split[0] + "://" + uri.getHost() + ":" + uri.getPort();
    }
    
    public boolean isMysqlOrMariadb() {
    	return dbname.equalsIgnoreCase("mysql") || dbname.equalsIgnoreCase("mariadb");
    }
    
}
