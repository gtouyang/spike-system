package com.ogic.spikesystemsqlservice.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.ogic.spikesystemsqlservice.component.DynamicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertyResolver;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ogic
 */
@Configuration
public class DataSourceConfig {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private Environment environment;

    private DruidDataSource master(){
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUsername(environment.getProperty("datasource.master.username"));
        dataSource.setPassword(environment.getProperty("datasource.master.password"));
        dataSource.setUrl(environment.getProperty("datasource.master.url"));
        dataSource.setDbType(environment.getProperty("datasource.master.type"));
        dataSource.setDriverClassName(environment.getProperty("datasource.master.driver-class-name"));
        return dataSource;
    }

    private DruidDataSource slave(){
        DruidDataSource dataSource = new DruidDataSource();
        dataSource.setUsername(environment.getProperty("datasource.slave.username"));
        dataSource.setPassword(environment.getProperty("datasource.slave.password"));
        dataSource.setUrl(environment.getProperty("datasource.slave.url"));
        dataSource.setDbType(environment.getProperty("datasource.slave.type"));
        dataSource.setDriverClassName(environment.getProperty("datasource.slave.driver-class-name"));
        return dataSource;
    }

    @Primary
    @Bean(name = "dataSource")
    public AbstractRoutingDataSource dynamicDataSource(){
        DynamicDataSource dataSource = new DynamicDataSource();
        Map<Object, Object> targetDataSources = new HashMap<>(2);
        DruidDataSource masterDataSource = master();
        logger.info("create master datasource: " + masterDataSource.getUrl());
        DruidDataSource slaveDataSource = slave();
        logger.info("create slave datasource: " + slaveDataSource.getUrl());
        targetDataSources.put("master", masterDataSource);
        targetDataSources.put("slave", slaveDataSource);
        dataSource.setTargetDataSources(targetDataSources);
        dataSource.setDefaultTargetDataSource(masterDataSource);
        return dataSource;
    }

}
