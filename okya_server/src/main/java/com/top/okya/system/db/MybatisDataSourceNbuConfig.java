package com.top.okya.system.db;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

@Configuration
@MapperScan(basePackages = "com.top.okya.dao", sqlSessionTemplateRef = "mjqSqlSessionTemplate")
public class MybatisDataSourceNbuConfig {

	@Bean(name="mjqSqlSessionFactory")
	@Primary
	public SqlSessionFactory mjqSqlSessionFactory(@Qualifier("dataSourceMjq") DataSource dataSource) throws Exception {
		SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
		bean.setDataSource(dataSource);
		bean.setMapperLocations(
				new PathMatchingResourcePatternResolver().getResources("classpath:mapper/*Mapper.xml"));
		return bean.getObject();
	}

	@Bean(name="mjqSqlSessionTemplate")
	@Primary
	public SqlSessionTemplate mjqSqlSessionTemplate(
			@Qualifier("mjqSqlSessionFactory") SqlSessionFactory sqlSessionFactory) throws Exception {
		return new SqlSessionTemplate(sqlSessionFactory);
	}
}
