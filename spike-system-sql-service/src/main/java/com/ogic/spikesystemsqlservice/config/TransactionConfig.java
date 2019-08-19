package com.ogic.spikesystemsqlservice.config;

import com.ogic.spikesystemsqlservice.component.DynamicDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

/**
 * @author ogic
 */
@Configuration
@EnableTransactionManagement
@Slf4j
@AutoConfigureAfter({ DataSourceConfig.class })
public class TransactionConfig {

    @Bean
    @Autowired
    public DataSourceTransactionManager transactionManager(AbstractRoutingDataSource dataSource) {
        log.info("事物配置");
        return new DataSourceTransactionManager(dataSource);
    }
}
