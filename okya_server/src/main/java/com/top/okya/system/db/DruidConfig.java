package com.top.okya.system.db;

import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;
import javax.transaction.SystemException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.transaction.jta.JtaTransactionManager;

import com.alibaba.druid.filter.Filter;
import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.pool.xa.DruidXADataSource;
import com.alibaba.druid.support.http.WebStatFilter;
import com.alibaba.druid.wall.WallConfig;
import com.alibaba.druid.wall.WallFilter;
import com.atomikos.icatch.jta.UserTransactionImp;
import com.atomikos.icatch.jta.UserTransactionManager;
import com.atomikos.jdbc.AtomikosDataSourceBean;

@Configuration
@PropertySource("classpath:jdbc_mysql.properties")
public class DruidConfig {
	
    @Value("${spring.custom.datasource.mjq.url:#{null}}")
    private String dbUrlNbu;
    @Value("${spring.custom.datasource.mjq.username: #{null}}")
    private String usernameNbu;
    @Value("${spring.custom.datasource.mjq.password:#{null}}")
    private String passwordNbu;
    @Value("${spring.custom.datasource.mjq.driver-class-name:#{null}}")
    private String driverClassNameNbu;
    
    @Value("${spring.datasource.initialSize:#{null}}")
    private Integer initialSize;
    @Value("${spring.datasource.minIdle:#{null}}")
    private Integer minIdle;
    @Value("${spring.datasource.maxActive:#{null}}")
    private Integer maxActive;
    @Value("${spring.datasource.maxWait:#{null}}")
    private Integer maxWait;
    @Value("${spring.datasource.timeBetweenEvictionRunsMillis:#{null}}")
    private Integer timeBetweenEvictionRunsMillis;
    @Value("${spring.datasource.maxLifeTime:#{null}}")
    private Integer maxLifeTime;
    @Value("${spring.datasource.minEvictableIdleTimeMillis:#{null}}")
    private Integer minEvictableIdleTimeMillis;
    @Value("${spring.datasource.validationQuery:#{null}}")
    private String validationQuery;
    @Value("${spring.datasource.testWhileIdle:#{null}}")
    private Boolean testWhileIdle;
    @Value("${spring.datasource.testOnBorrow:#{null}}")
    private Boolean testOnBorrow;
    @Value("${spring.datasource.testOnReturn:#{null}}")
    private Boolean testOnReturn;
    @Value("${spring.datasource.removeAbandonedTimeout:#{null}}")
    private Integer removeAbandonedTimeout;
    @Value("${spring.datasource.removeAbandoned:#{null}}")
    private Boolean removeAbandoned;
    @Value("${spring.datasource.logAbandoned:#{null}}")
    private Boolean logAbandoned;
    @Value("${spring.datasource.poolPreparedStatements:#{null}}")
    private Boolean poolPreparedStatements;
    @Value("${spring.datasource.maxPoolPreparedStatementPerConnectionSize:#{null}}")
    private Integer maxPoolPreparedStatementPerConnectionSize;
    @Value("${spring.datasource.filters:#{null}}")
    private String filters;
    @Value("{spring.datasource.connectionProperties:#{null}}")
    private String connectionProperties;

    @Bean(name="dataSourceMjq")
	@Primary
    public DataSource dataSourceMjq(){
    	DruidXADataSource datasource = new DruidXADataSource();

        datasource.setUrl(dbUrlNbu);
        datasource.setUsername(usernameNbu);
        datasource.setPassword(passwordNbu);
        datasource.setDriverClassName(driverClassNameNbu);
        //configuration
        if(initialSize != null) {
            datasource.setInitialSize(initialSize);
        }
        if(minIdle != null) {
            datasource.setMinIdle(minIdle);
        }
        if(maxActive != null) {
            datasource.setMaxActive(maxActive);
        }
        if(maxWait != null) {
            datasource.setMaxWait(maxWait);
        }
        if(timeBetweenEvictionRunsMillis != null) {
            datasource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
        }
        if(minEvictableIdleTimeMillis != null) {
            datasource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
        }
        if(validationQuery!=null) {
            datasource.setValidationQuery(validationQuery);
        }
        if(testWhileIdle != null) {
            datasource.setTestWhileIdle(testWhileIdle);
        }
        if(testOnBorrow != null) {
            datasource.setTestOnBorrow(testOnBorrow);
        }
        if(testOnReturn != null) {
            datasource.setTestOnReturn(testOnReturn);
        }
        if(datasource.isOracle()){
            if(poolPreparedStatements != null) {
                datasource.setPoolPreparedStatements(poolPreparedStatements);
            }
            if(maxPoolPreparedStatementPerConnectionSize != null) {
                datasource.setMaxPoolPreparedStatementPerConnectionSize(maxPoolPreparedStatementPerConnectionSize);
            }
        }

        if(connectionProperties != null) {
            datasource.setConnectionProperties(connectionProperties);
        }

        if(removeAbandoned != null) {
            datasource.setRemoveAbandoned(removeAbandoned);
        }

        if(removeAbandonedTimeout != null) {
            datasource.setRemoveAbandonedTimeout(removeAbandonedTimeout);
        }

        if(logAbandoned != null) {
            datasource.setLogAbandoned(logAbandoned);
        }

        List<Filter> filters = new ArrayList<>();
        filters.add(statFilter());
        filters.add(wallFilter());
        datasource.setProxyFilters(filters);
        
        AtomikosDataSourceBean sourceBean = new AtomikosDataSourceBean();
		sourceBean.setXaDataSource(datasource);
        sourceBean.setMaxPoolSize(maxActive);
        sourceBean.setMinPoolSize(minIdle);
        sourceBean.setMaxLifetime(maxLifeTime);
        sourceBean.setMinPoolSize(minIdle);
        sourceBean.setMaxPoolSize(maxActive);
        sourceBean.setTestQuery(validationQuery);
        sourceBean.setBorrowConnectionTimeout(60);
		// 必须为数据源指定唯一标识
		sourceBean.setUniqueResourceName("dataSourceMjq");
        
        return sourceBean;
    }
    
    /*atomikos事务管理器*/
    public UserTransactionManager userTransactionManager() {
        UserTransactionManager userTransactionManager = new UserTransactionManager();
        userTransactionManager.setForceShutdown(true);
        return userTransactionManager;
    }

    public UserTransactionImp userTransactionImp() throws SystemException {
        UserTransactionImp userTransactionImp = new UserTransactionImp();
        userTransactionImp.setTransactionTimeout(5000);
        return userTransactionImp;
    }

    @Bean
    public JtaTransactionManager jtaTransactionManager() throws SystemException {
        JtaTransactionManager jtaTransactionManager = new JtaTransactionManager();
        jtaTransactionManager.setTransactionManager(userTransactionManager());
        jtaTransactionManager.setUserTransaction(userTransactionImp());
        jtaTransactionManager.setAllowCustomIsolationLevels(true);
        return jtaTransactionManager;
    }
    
    @Bean
	@Primary
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(new WebStatFilter());
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.addInitParameter("exclusions", "*.js,*.gif,*.jpg,*.png,*.css,*.ico,/druid/*");
        filterRegistrationBean.addInitParameter("profileEnable", "true");
        return filterRegistrationBean;
    }

    @Bean
	@Primary
    public StatFilter statFilter(){
        StatFilter statFilter = new StatFilter();
        statFilter.setLogSlowSql(true); //slowSqlMillis用来配置SQL慢的标准，执行时间超过slowSqlMillis的就是慢。
        statFilter.setMergeSql(true); //SQL合并配置
        statFilter.setSlowSqlMillis(1000);//slowSqlMillis的缺省值为3000，也就是3秒。
        return statFilter;
    }

    @Bean
	@Primary
    public WallFilter wallFilter(){
        WallFilter wallFilter = new WallFilter();
        //允许执行多条SQL
        WallConfig config = new WallConfig();
        config.setMultiStatementAllow(true);
        wallFilter.setConfig(config);
        return wallFilter;
    }
}