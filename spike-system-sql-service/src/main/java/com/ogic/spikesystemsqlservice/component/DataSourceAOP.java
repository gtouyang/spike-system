package com.ogic.spikesystemsqlservice.component;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

/**
 * @author ogic
 */
@Aspect
@Component
@Slf4j
public class DataSourceAOP {

    @Before("@annotation(com.ogic.spikesystemsqlservice.annotation.Master) && !@annotation(com.ogic.spikesystemsqlservice.annotation.Slave)")
    public void setWriteDataSourceType() {
        DynamicDataSource.master();
        log.info("dataSource切换到：master");
    }

    @Before("@annotation(com.ogic.spikesystemsqlservice.annotation.Slave) && !@annotation(com.ogic.spikesystemsqlservice.annotation.Master)")
    public void setReadDataSourceType() {
        DynamicDataSource.slave();
        log.info("dataSource切换到：slave");
    }

}