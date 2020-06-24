package com.bytetube.config.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.TransactionManagementConfigurer;

import javax.sql.DataSource;

/**
 *
 *  <!扫描service包下所有使用注解的类型 -->
 *     <context:component-scan base-package="com.bytetube.service" />
 *     这个不需要配置，因为BtappApplication中@SpringBootApplication会做整个package的扫描
 * 对标spring-service里面的transactionManager
 * 继承TransactionManagementConfigurer是因为开启annotation-driven
 * 
 * @author bytetube
 *
 */
@Configuration
// 首先使用注解 @EnableTransactionManagement 开启事务支持后
// 在Service方法上添加注解 @Transactional 便可
@EnableTransactionManagement
public class TransactionManagementConfiguration implements TransactionManagementConfigurer {

	@Autowired
	// 注入DataSourceConfiguration里边的dataSource,通过createDataSource()获取
	private DataSource dataSource;

	@Override
	/**
	 * 关于事务管理，需要返回PlatformTransactionManager的实现
	 */
	public PlatformTransactionManager annotationDrivenTransactionManager() {
		return new DataSourceTransactionManager(dataSource);
	}

}
