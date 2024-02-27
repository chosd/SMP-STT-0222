package com.kt.smp.multitenancy.config;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.tomcat.util.codec.binary.Base64;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.kt.smp.common.util.EncUtil;
import com.zaxxer.hikari.HikariDataSource;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class MasterDatabaseConfig {
	@Value("${pagehelper.helper-dialect}")
	private String dbname;

	@Value("${spring.datasource.master.password}")
	private String dbPw;
	@Value("${cipher.secretKey}")
	private String secretKey;
	@Value("${cipher.test:}")
	private String testPw;

	@Bean
	@Primary
	@ConfigurationProperties("spring.datasource.master")
	public DataSourceProperties masterDataSourceProperties() {
		return new DataSourceProperties();
	}

	@Bean(name = "hikariDataSource")
	@ConfigurationProperties("spring.datasource.master.hikari")
	public HikariDataSource dataSource(DataSourceProperties properties) {
		if (!testPw.equals("")) {
			String encPw = EncUtil.encAES128(testPw, secretKey);
			log.info(">>> test encrypted to : " + encPw);
		}

		String decPw = properties.getPassword();
		String decUser = properties.getUsername();
		try {
			if(secretKey.equals("opaque")) {
				byte[] byteStr = Base64.decodeBase64(decPw.getBytes("UTF-8"));
				decPw = new String(byteStr, "UTF-8");
				
				byteStr = Base64.decodeBase64(decUser.getBytes("UTF-8"));
				decUser = new String(byteStr, "UTF-8");
			}
			else {
				decPw = EncUtil.decAES128(properties.getPassword(), secretKey);
			}
//			System.out.println(">>> master pw : " + properties.getPassword() + ", dec : " + decPw);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return properties.initializeDataSourceBuilder()
				.type(HikariDataSource.class)
				.username(decUser)
				.password(decPw).build();
	}

//	@Bean(name = "masterDataSource")
//	public DataSource masterDataSource() {
//		return masterDataSourceProperties()
//			.initializeDataSourceBuilder()
//			.build();
//	}

	@Bean(name = "masterSqlSessionFactory")
	public SqlSessionFactory masterSessionFactoryBean(@Qualifier("hikariDataSource") DataSource dataSource,
			ApplicationContext applicationContext) throws Exception {

		SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
		factoryBean.setDataSource(dataSource);
		factoryBean.setConfigLocation(applicationContext.getResource("classpath:mybatis/mybatis-config.xml"));
		factoryBean
				.setMapperLocations(applicationContext.getResources("classpath*:mapper/" + dbname + "/master/*.xml"));
		return factoryBean.getObject();
	}

	@Bean(name = "masterSqlSession")
	public SqlSession masterSqlSession(@Qualifier("masterSqlSessionFactory") SqlSessionFactory sessionFactory) {
		return new SqlSessionTemplate(sessionFactory);
	}
}
