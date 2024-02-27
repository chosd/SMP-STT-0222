package com.kt.smp.multitenancy.config;

import com.kt.smp.multitenancy.repository.ConfigRepository;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class TenantDatabaseConfig {
	@Value("${pagehelper.helper-dialect}")
    private String dbname;
	
	@Value("${cipher.secretKey}")
	private String secretKey;
	
	@Bean
	public DynamicRoutingDataSource dynamicRoutingDataSource(ConfigRepository configRepository) {
		
		DynamicRoutingDataSource routingDatasource = new DynamicRoutingDataSource();
		routingDatasource.init(configRepository, secretKey);
		return routingDatasource;
	}

	@Bean
	public LazyConnectionDataSourceProxy lazyDataSource(DynamicRoutingDataSource routingDatasource) {
		return new LazyConnectionDataSourceProxy(routingDatasource);
	}

	@Bean
	public PlatformTransactionManager transactionManager(LazyConnectionDataSourceProxy lazyDataSource) {
		return new DataSourceTransactionManager(lazyDataSource);
	}

	@Bean(name = "tenantSqlSessionFactory")
	public SqlSessionFactory tenantSqlSessionFactoryBean(
		LazyConnectionDataSourceProxy lazyDataSource, ApplicationContext applicationContext) throws Exception {

		SqlSessionFactoryBean factoryBean = new SqlSessionFactoryBean();
		factoryBean.setDataSource(lazyDataSource);
		factoryBean.setConfigLocation(applicationContext.getResource("classpath:mybatis/mybatis-config.xml"));
		factoryBean.setMapperLocations(applicationContext.getResources("classpath*:mapper/" +dbname+ "/tenant/*.xml"));
		return factoryBean.getObject();
	}

	@Bean(name = "tenantSqlSession")
	public SqlSession tenantSqlSession(@Qualifier("tenantSqlSessionFactory") SqlSessionFactory sqlSessionFactory) {
		return new SqlSessionTemplate(sqlSessionFactory);
	}
}
